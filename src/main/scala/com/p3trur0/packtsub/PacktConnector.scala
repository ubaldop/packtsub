package com.p3trur0.packtsub

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import com.typesafe.config.ConfigFactory

import gigahorse.Gigahorse
import scala.concurrent.Future
import gigahorse.HeaderNames

object PacktConnector {
  def apply(implicit config: PacktConfig) = new PacktConnector
}

class PacktConnector(implicit config: PacktConfig) extends HtmlCrawler {

  lazy val headers: Map[String, List[String]] = Map(
    HeaderNames.ACCEPT -> List("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
    HeaderNames.ACCEPT_ENCODING -> List("gzip, deflate"),
    HeaderNames.CONNECTION -> List("keep-alive"));

  val titleInfo = crawlTitleInfo(config)

  def run(): String = {

    def httpExecution(titleInfo: TitleInfo): String = {

      val http = Gigahorse.http(Gigahorse.config)

      println(s"Current book is ${titleInfo.title}")

      val loginRequest = Gigahorse.url(config.crawlUrl).addHeaders(headers).
        post(
          Map(
            "email" -> List(config.email),
            "password" -> List(config.pwd),
            "op" -> List("Login"),
            "form_build_id" -> List(titleInfo.formId),
            "form_id" -> List("packt_user_login_form")))

      val futureResponse = http.run(loginRequest) flatMap { response =>

        val downloadRequest = for {
          sessionId <- crawlSessionId(response.body)
        } yield {
          Gigahorse.url(titleInfo.claimURL).get.addHeaders {
            headers
            (HeaderNames.REFERER, config.loginUrl)
            (HeaderNames.COOKIE, s"SESS_live=$sessionId;logged_in=0;")
          }
        }

        downloadRequest match {
          case Some(request) => http.run(request, Gigahorse.asEither)
          case None          => Future { Left("Not valid download request has been built") }
        }

      }

      val res = Await.result(futureResponse, 20.seconds)
      http.close
      res match {
        case Right(_) => 
                        send a OKMail(titleInfo.title)
                        s"""Today has been downloaded "${titleInfo.title}""""
        case Left(e)  =>
                        send a KOMail
                        s"Error while retrieving the title of today because of the following error: \n$e"
      }
    }

    titleInfo() match {
      case Some(data) => httpExecution(data)
      case None       => "No valid data retrieved today. See you tomorrow"
    }

  }

}
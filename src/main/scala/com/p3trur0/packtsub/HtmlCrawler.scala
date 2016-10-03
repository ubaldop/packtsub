package com.p3trur0.packtsub

import java.net.URL
import java.util.regex.Pattern
import org.filippodeluca.ssoup.{RichDocument, RichElements}
import org.filippodeluca.ssoup.SSoup._
import scala.concurrent.duration._
import scala.util.matching.Regex
import scala.util.{Try, Failure, Success}

case class TitleInfo(title: String, formId: String, claimURL: String)

trait HtmlCrawler {

  private def innerCrawl(html: RichDocument): Option[TitleInfo] = {
   
    val formId = for {
      div <- html.getElementById("packt-user-login-form")
    } yield div.getElementsByAttributeValue("name", "form_build_id").last.attr("id")

    val dividers = html.getElementById("deal-of-the-day")

    val titleDiv = html.getElementsByAttributeValueMatching("class", "^dotd-title$".r)
    
    val url = for {
      titles <- dividers.map(elem => elem.getElementsByTag("a"))
      if titles.hasClass("twelve-days-claim")
    } yield titles.last.attr("href")

    for {
      title <- titleDiv.headOption
      formId <- formId
      url <- url
    } yield TitleInfo(title.html, formId, url)

  }
  
  def crawlTitleInfo(config: PacktConfig) = () => {
    crawlFromURL(config.crawlUrl) match {
      case Some(titleInfo) => Some(titleInfo.copy(claimURL = config.loginUrl+titleInfo.claimURL))
      case None => None
    }
  }

  def crawlFromPage(page: HTML): Option[TitleInfo] = {
    innerCrawl(parse(page))
  }

  def crawlFromURL(url: String): Option[TitleInfo] = {
    innerCrawl(parse(new URL(url), 5 seconds))
  }

  def crawlSessionId(responseBody: String): Option[String] = {
    val retrievalPattern = new Regex("\"sid\"(:|: | :| : )\"(.*?)\"", "sid", "separator","sessionid")
    for {
      scripts <- parse(responseBody).headOpt.map(script => script.getElementsByTag("script"))
      elem <- retrievalPattern.findFirstMatchIn(scripts.html)
    } yield elem.group(2)
  }

}
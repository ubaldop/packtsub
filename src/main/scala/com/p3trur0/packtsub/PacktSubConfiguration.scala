package com.p3trur0.packtsub

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import com.typesafe.config.ConfigFactory

trait PacktSubConfig

case class PacktConfig(email: String, pwd: String, crawlUrl: String, loginUrl: String) extends PacktSubConfig
case class MailConfig(to: String, subject: String, bodyOK: String, bodyKO: String) extends PacktSubConfig
case class SMTPConfig(host: String, user: String, password: String) extends PacktSubConfig

object PacktSubConfiguration {

  val applicationConfig = ConfigFactory.load();

  private val packtConfigData: PacktConfig = {
    PacktConfig(
      applicationConfig.getString("packt.email"),
      applicationConfig.getString("packt.password"),
      applicationConfig.getString("packt.crawl_address"),
      applicationConfig.getString("packt.url"))
  }

  val mailConfiguration: MailConfig = {
    MailConfig(
      applicationConfig.getString("email.to"),
      applicationConfig.getString("email.subject"),
      applicationConfig.getString("email.body_OK"),
      applicationConfig.getString("email.body_KO"))
  }

  val smtpConfiguration: Try[SMTPConfig] = {
    Try (SMTPConfig(
        applicationConfig.getString("email.smtp.server"),
        applicationConfig.getString("email.smtp.user"),
        applicationConfig.getString("email.smtp.password")))
  }

  val packtConfiguration: Option[PacktConfig] =
    packtConfigData.email match {
      case "fake@address.com" => None
      case _                  => Some(packtConfigData)
    }
}
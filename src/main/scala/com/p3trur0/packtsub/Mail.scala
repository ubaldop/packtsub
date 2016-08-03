package com.p3trur0.packtsub

import org.apache.commons.mail.Email
import org.apache.commons.mail.HtmlEmail
import org.apache.commons.mail.DefaultAuthenticator
import scala.util.Success
import scala.concurrent.Future
import scala.util.Failure
import scala.concurrent.ExecutionContext.Implicits.global

sealed trait Mail {
	val to = PacktSubConfiguration.mailConfiguration.to
			val subject = PacktSubConfiguration.mailConfiguration.subject
			val body = ""
}

case class OKMail(title: String) extends Mail {
	override val body = PacktSubConfiguration.mailConfiguration.bodyOK.replace("${title}", title)
}
case object KOMail extends Mail {
	override val body = PacktSubConfiguration.mailConfiguration.bodyKO
}

/**
 * Singleton to send emails
 */
object send {

  /**
   * This method returns a Future execution to send an e-mail
   */
  def async(mail: Mail) = Future {
    a(mail)
  }
  
  /**
   * This method sends a mail synchronously if a proper SMTP configuration has been provided.
   */
	def a(mail: Mail) = {
		PacktSubConfiguration.smtpConfiguration match {
		  
		case Success(config) =>
			val email = new HtmlEmail()
			email.setHostName(config.host)
			email.setSmtpPort(465)
			email.setAuthenticator(new DefaultAuthenticator(config.user, config.password))
			email.setSSLOnConnect(true)
			email.setFrom("Notifier@PacktSub")
			email.setSubject(mail.subject)
      email.setHtmlMsg(s"<html>${mail.body}</html>")
			email.setTextMsg(mail.body)
			email.addTo(mail.to)
			email.send()
		
		  case Failure(e) =>  s"Valid SMTP configuration missing: ${e.getMessage}" 
		}

	}
}
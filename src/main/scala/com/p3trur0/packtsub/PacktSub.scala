package com.p3trur0.packtsub

import scala.concurrent.Await

object PacktSub extends App {

  println("""
|    
|╔═╗┌─┐┌─┐┬┌─┌┬┐╔═╗┬ ┬┌┐ 
|╠═╝├─┤│  ├┴┐ │ ╚═╗│ │├┴┐
|╩  ┴ ┴└─┘┴ ┴ ┴ ╚═╝└─┘└─┘    
|    
|   A PacktPub free content subscriber. 
|    """.stripMargin)

  val result = for {
    config <- PacktSubConfiguration.packtConfiguration
  } yield PacktConnector(config).run

  result match {
    case Some(x) => println(x)
    case None => println("""
      |Invalid configuration, PacktSub cannot run!
      |
      |  To configure it properly, please add your PacktPub login credential to the enviroment variable of this running machine.
      |  
      |  The email variable must be named PACKT_EMAIL
      |  The password variable must be named PACKT_PWD
      |  
      |  Thank you and have fun!
      """.stripMargin)
  }
  
  
}



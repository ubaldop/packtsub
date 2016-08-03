package com.p3trur0.packtsub

import org.scalatest.Matchers
import org.scalatest.FlatSpec

class PacktSubSpec extends FlatSpec with Matchers {

  it should "retrieve a None when crawling an empty string" in {
    val html: HTML = ""
    new HtmlCrawler {
      val crawledId = crawlFromPage(html)
      crawledId shouldBe None
    }
  }

  it should "retrieve a None when crawling a not containing packt-form-id" in {
    val html: HTML = """<!DOCTYPE html>
    |  <html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    |    <body class="with-logo"/>
    |</html>"""
    new HtmlCrawler {
      val crawledId = crawlFromPage(html)
      crawledId shouldBe None
    }
  }

  it should "retrieve a None if HTML does not contain one of the required attributes" in {
    val html: HTML = """<!DOCTYPE html>
    |  <html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    |    <body class="with-logo">
    |      <form accept-charset="UTF-8" method="post" id="packt-user-login-form">
		|        <div></div>
    |        <input type="hidden" name="form_build_id" id="form-918cfde5fb3eced7fdd6d479011d58e4" value="form-918cfde5fb3eced7fdd6d479011d58e4"/>
    |        <input type="hidden" name="form_id" id="edit-packt-user-login-form" value="packt_user_login_form"/> 
    |</form>
    |
    |<div class="cf" id="deal-of-the-day">
		|				<div class="dotd-title">
		|					Gradle Effective Implementation Guide
		|				</div>
		|				
		|					<!--a class="twelve-days-claim" href="/freelearning-claim/10748/21478"></a-->
		|</div>
		|	
    |    </body>
    |</html>"""
    new HtmlCrawler {
      val crawledId = crawlFromPage(html)
      crawledId shouldBe None
    }
  }

  it should "retrieve a valid TitleInfo when crawling a valid HTML" in {
    val html: HTML = """<!DOCTYPE html>
    |  <html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    |    <body class="with-logo">
    |      <form accept-charset="UTF-8" method="post" id="packt-user-login-form">
		|        <div></div>
    |        <input type="hidden" name="form_build_id" id="form-918cfde5fb3eced7fdd6d479011d58e4" value="form-918cfde5fb3eced7fdd6d479011d58e4"/>
    |        <input type="hidden" name="form_id" id="edit-packt-user-login-form" value="packt_user_login_form"/> 
    |</form>
    |
    |<div class="cf" id="deal-of-the-day">
		|				<div class="dotd-title">
		|					Gradle Effective Implementation Guide
		|				</div>
		|				
		|					<a class="twelve-days-claim" href="/freelearning-claim/10748/21478"></a>
		|</div>
		|	
    |    </body>
    |</html>"""
    new HtmlCrawler {
      val crawledId = crawlFromPage(html)
      crawledId shouldBe Some(TitleInfo("| Gradle Effective Implementation Guide |", "form-918cfde5fb3eced7fdd6d479011d58e4", "/freelearning-claim/10748/21478"))
    }
  }

  it should "retrieve a valid TitleInfo from the actual Packt page" in {
    pending  //to avoid unuseful HTTP calls
    new HtmlCrawler {
      val data = crawlFromURL("https://www.packtpub.com/packt/offers/free-learning")
      data shouldBe a[Some[_]]
    }
  }

  it should "retrieve the Packt session identifier" in {
    val html: HTML = """<!DOCTYPE html>
    |<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    |	<head>
    |		<title>Free Learning - Free Technology eBooks | PACKT Books</title>
    |		<script>
    |			dataLayer = [];
    |		</script>
    |		<script type="text/javascript">
    |			var GRANIFY_SITE_ID = 1318;(function(e,t,n){var r,i,s,o,u;u=false;try{o=new RegExp("(?:^|\\W)_gr_test_url=([^;]*)");s=document.cookie.match(o);if(s){if(s[1]==="1")u=true}else{r=Math.random();if(r>.95)u=true;i=new Date(+(new Date)+1e3*60*60*24*2);document.cookie="_gr_test_url="+ +u+";expires="+i.toGMTString()+";path=/"}}catch(a){}if(u)e.replace("javascript.js","javascript.next.js");try{o=new RegExp("(?:^|\\W)_gr_ep=([^;]*)");s=document.cookie.match(o);if(!s){document.cookie="_gr_ep_sent=;expires=Thu, 01-Jan-1970 00:00:01 GMT;path=/";document.cookie="_gr_er_sent=;expires=Thu, 01-Jan-1970 00:00:01 GMT;path=/";if(!window.location.origin){if(window.location.port)port=":"+window.location.port;else port="";window.location.origin=window.location.protocol+"//"+window.location.hostname+port}path=window.location.toString().replace(window.location.origin,"");referrer=document.referrer;i=new Date(+(new Date)+1e3*60*30);document.cookie="_gr_ep="+path+";expires="+i.toGMTString()+";path=/";document.cookie="_gr_er="+referrer+";expires="+i.toGMTString()+";path=/"}}catch(a){}a=new Date;a=""+a.getUTCFullYear()+(a.getUTCMonth()+1)+a.getUTCDate();e=e+"?id="+t+"&v="+a;window.Granify=n;n._stack=[];n.init=function(e,t,r){function i(e,t){e[t]=function(){Granify._stack.push([t].concat(Array.prototype.slice.call(arguments,0)))}}var s=n;h=["on","identify","addTag","trackPageView","trackCart","trackOrder"];for(a=0;a<h.length;a++)i(s,h[a])};n.init();var f,l,c,p=document.createElement("iframe");p.src="javascript:false";p.title="";p.role="presentation";(p.frameElement||p).style.cssText="width: 0 !important; height: 0 !important; border: 0 !important; overflow: hidden !important; position: absolute !important; top: -1000px !important; left: -1000px !important;";c=document.getElementsByTagName("script");c=c[c.length-1];c.parentNode.insertBefore(p,c);try{l=p.contentWindow.document}catch(a){f=document.domain;p.src="javascript:var d=document.open();d.domain='"+f+"';void(0);";l=p.contentWindow.document}l.open()._l=function(){var t=this.createElement("script");if(f)this.domain=f;t.id="js-iframe-async";t.src=e;this.body.appendChild(t)};l.write('<body onload="document._l();">');l.close()})("//cdn.granify.com/assets/javascript.js",GRANIFY_SITE_ID,[])
    |		</script>
    |    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    |    <link rel="shortcut icon" href="//d1ldz4te4covpm.cloudfront.net/misc/favicon.ico" type="image/x-icon" />
    |    <script type="text/javascript">
    |            var Packt = Packt || {}; Packt.user = {"uid":199999,"name":"Test user","mail":"fake@address.com","sid":"123456789aaaaaabkaja192934","cart_id":199999,"cart":[],"newsletters":{"111":true,"111":true},"country_code":"GB","selected_currency":"GBP","currency":"GBP","currency_symbol":"\u00a3"};
    |    </script>
    |    <script type="text/javascript">
    |      <!--//--><![CDATA[//><!--
    |        jQuery.extend(Drupal.settings, { "basePath": "\u002F", "dhtmlMenu": [ "doubleclick", "clone" ] });
    |      //--><!]]>
    |    </script>
    |    
    |  </head>
    |    <body>Hello</body>
    |</html>""".stripMargin

    new HtmlCrawler {
      val sessionId = crawlSessionId(html)
      sessionId shouldBe a[Some[_]]
      sessionId.get shouldBe "123456789aaaaaabkaja192934"
    }    
  }
  
  it should "return a None object because no valid sid token exist in the HTML" in {
    val html: HTML = """<!DOCTYPE html>
    |<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    |	<head>
    |		<title>Free Learning - Free Technology eBooks | PACKT Books</title>
    |    <script type="text/javascript">
    |            var Packt = Packt || {}; Packt.user = {"sud":"123456789aaaaaabkaja192934","country_code":"GB","selected_currency":"GBP","currency":"GBP","currency_symbol":"\u00a3"};
    |    </script>
    |    
    |  </head>
    |    <body>Hello</body>
    |</html>""".stripMargin

    new HtmlCrawler {
      val sessionId = crawlSessionId(html)
      sessionId shouldBe None
    }    
  }
  
   it should "return a None object because no valid split token exist in the HTML" in {
    val html: HTML = """<!DOCTYPE html>
    |<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    |	<head>
    |		<title>Free Learning - Free Technology eBooks | PACKT Books</title>
    |    <script type="text/javascript">
    |            var Packt = Packt || {}; Packt.user = {"sid";"123456789aaaaaabkaja192934" "country_code":"GB"};
    |    </script>
    |    
    |  </head>
    |    <body>Hello</body>
    |</html>""".stripMargin

    new HtmlCrawler {
      val sessionId = crawlSessionId(html)
      sessionId shouldBe None
    }    
  }
  
}
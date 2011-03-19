package net.foobarfreaks.akka.chat

import java.util.Date
import javax.ws.rs.core.MediaType
import akka.actor._
import akka.actor.Actor.actorOf
import akka.config.Supervision._
import akka.http._

class BoringActor extends Actor {

 
  var gets = 0
  var posts = 0
  var lastget: Option[Date] = None
  var lastpost: Option[Date] = None
 
  def receive = {
    // handle a get request
    case get: Get =>
      // the content type of the response.
      // similar to @Produces annotation
      get.response.setContentType(MediaType.TEXT_HTML)
 
      //
      // "work"
      //
      gets += 1
      lastget = Some(new Date)
 
      //
      // respond
      //
      val res = "<p>Gets: "+gets+" Posts: "+posts+"</p><p>Last Get: "+lastget.getOrElse("Never").toString+" Last Post: "+lastpost.getOrElse("Never").toString+"</p>"
      get.OK(res)
 
    // handle a post request
    case post:Post =>
      // the expected content type of the request
      // similar to @Consumes
      if (post.request.getContentType startsWith MediaType.APPLICATION_FORM_URLENCODED) {
        // the content type of the response.
        // similar to @Produces annotation
        post.response.setContentType(MediaType.TEXT_HTML)
 
        // "work"
        posts += 1
        lastpost = Some(new Date)
 
        // respond
        val res = "<p>Gets: "+gets+" Posts: "+posts+"</p><p>Last Get: "+lastget.getOrElse("Never").toString+" Last Post: "+lastpost.getOrElse("Never").toString+"</p>"
        post.OK(res)
      } else {
        post.UnsupportedMediaType("Content-Type request header missing or incorrect (was '" + post.request.getContentType + "' should be '" + MediaType.APPLICATION_FORM_URLENCODED + "')")
      }
      
    case other: RequestMethod =>
      other.NotAllowed("Invalid method for this endpoint")
  }
}
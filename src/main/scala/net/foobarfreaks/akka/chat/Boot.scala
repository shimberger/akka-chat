package net.foobarfreaks.akka.chat

import akka.actor._
import akka.actor.Actor.actorOf
import akka.config.Supervision._
import akka.http.RootEndpoint

class Boot {
  val factory = SupervisorFactory(
    SupervisorConfig(
      OneForOneStrategy(List(classOf[Exception]), 3, 100),
      Supervise(
        actorOf[RootEndpoint],
        Permanent) ::
      Supervise(
        actorOf[ChatNexus],
        Permanent) ::        
	    Supervise(
	      actorOf(new UploadService("/mist/upload")),
	      Permanent) ::        
        // More actors go here	
      Nil))
  factory.newInstance.start
}
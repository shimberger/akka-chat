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
        actorOf[ChatNexus],
        Permanent) ::        
        // More actors goe here	
      Nil))
  factory.newInstance.start
}
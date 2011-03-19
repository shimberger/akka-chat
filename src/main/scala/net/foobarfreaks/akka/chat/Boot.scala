package net.foobarfreaks.akka.chat

import akka.actor._
import akka.actor.Actor.actorOf
import akka.config.Supervision._
import akka.http.RootEndpoint

class Boot {
  val factory = SupervisorFactory(
    SupervisorConfig(
      OneForOneStrategy(List(classOf[Exception]), 3, 100),
        //
        // in this particular case, just boot the built-in default root endpoint
        //
      Supervise(
        actorOf[RootEndpoint],
        Permanent) ::
      Supervise(
        actorOf[WebActorManager],
        Permanent) ::        
      Supervise(
        actorOf[SimpleAkkaAsyncHttpService],
        Permanent)
      :: Nil))
  factory.newInstance.start
}
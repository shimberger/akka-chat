package net.foobarfreaks.akka.chat

import akka.actor._
import akka.actor.Actor.actorOf
import akka.config.Supervision._
import akka.http._

class SimpleAkkaAsyncHttpService extends Actor with Endpoint {
  final val ServiceRoot = "/mist/simple/"
  final val ProvideSameActor = ServiceRoot + "same"
  final val ProvideNewActor = ServiceRoot + "new"
 
    //
    // use the configurable dispatcher
    //
  self.dispatcher = Endpoint.Dispatcher
 
    //
    // there are different ways of doing this - in this case, we'll use a single hook function
    //  and discriminate in the provider; alternatively we can pair hooks & providers
    //
  def hook(uri: String): Boolean = ((uri == ProvideSameActor) || (uri == ProvideNewActor))
  def provide(uri: String): ActorRef = {
    if (uri == ProvideSameActor) same
    else actorOf[BoringActor].start
  }
 
    //
    // this is where you want attach your endpoint hooks
    //
  override def preStart = {
      //
      // we expect there to be one root and that it's already been started up
      // obviously there are plenty of other ways to obtaining this actor
      //  the point is that we need to attach something (for starters anyway)
      //  to the root
      //
      val root = Actor.registry.actorsFor(classOf[RootEndpoint]).head
      root ! Endpoint.Attach(hook, provide)
    }
 
    //
    // since this actor isn't doing anything else (i.e. not handling other messages)
    //  just assign the receive func like so...
    // otherwise you could do something like:
    //  def myrecv = {...}
    //  def receive = myrecv orElse _recv
    //
  def receive = handleHttpRequest
 
  //
  // this will be our "same" actor provided with ProvideSameActor endpoint is hit
  //
  lazy val same = actorOf[BoringActor].start
}
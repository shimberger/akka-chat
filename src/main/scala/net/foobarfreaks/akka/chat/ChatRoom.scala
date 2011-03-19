package net.foobarfreaks.akka.chat

import akka.actor._
import akka.actor.Actor.actorOf
import akka.config.Supervision._
import scala.collection.mutable._;

case class Say(val msg : String);
case class Join();
case class Leave();

class ChatRoom extends Actor {

	val clients = HashSet[ActorRef]();
	
	def receive = {
	    case Say(_) => {
	    	clients.foreach(client => {
	    		client ! _
	    	});
	    }
	    case Join => clients.add(self.sender.get);
	    case Leave => clients.remove(self.sender.get);
	    case _ => log.info("received unknown message")
	}
	
}
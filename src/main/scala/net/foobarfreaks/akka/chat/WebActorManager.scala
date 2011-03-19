package net.foobarfreaks.akka.chat

import akka.actor._
import akka.actor.Actor.actorOf
import scala.collection.mutable._;

class WebActorManager extends Actor {

	case class Connect();
	case class Disconnect();
	
	var actors = HashMap[String,ActorRef]();
	
	override def preStart = {
		actors = HashMap[String,ActorRef]();
	}
	
	def receive = {
		case Connect => {
			
		}
		case Disconnect => {
			
		}
	    case _ => log.info("Received unknown message.")
	}	
	

}
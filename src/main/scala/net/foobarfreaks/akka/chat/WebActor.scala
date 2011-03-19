package net.foobarfreaks.akka.chat

import akka.actor._
import akka.actor.Actor.actorOf
import org.eclipse.jetty.websocket.WebSocket._

class WebActor(val outbound : Outbound) extends Actor {

	case class TextMessage(val data : String);
	
	def receive = {
		case msg : TextMessage => {
			outbound.sendMessage(msg.data);
		}
	    case _ => log.info("Received unknown message.")
	}	
	
}
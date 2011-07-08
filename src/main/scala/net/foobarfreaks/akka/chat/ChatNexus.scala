package net.foobarfreaks.akka.chat

import akka.actor._
import akka.actor.Actor._
import scala.collection.mutable._;

class ChatNexus extends Actor with WebSocketNexus with Logging {

		// Since this is never exposed it can be mutable
		val clients = HashSet[ActorRef]()
	
		def receive = {
			case msg : ConnectMessage => {
				log.info("Received connect message");
				clients.add(msg.from);
			}
			case msg : DisconnectMessage => {
				log.info("Received disconnect message");
				clients.remove(msg.from);
			}
			case msg : ServerMessage => {
				log.info("Received server message" + msg.data);
				// We dont have to do anything so far
			}
			case msg : ClientMessage => {
				log.info("Received client message" + msg.data);
				clients.foreach(arg => arg ! ServerMessage(self,msg.data))
			}
		    case _ => log.info("Received unknown message.")
		}		
	
}
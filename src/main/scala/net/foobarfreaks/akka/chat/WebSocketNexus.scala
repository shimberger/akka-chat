package net.foobarfreaks.akka.chat

import akka.actor._

case class ClientMessage(val from: ActorRef, val data : String);
case class ServerMessage(val from: ActorRef, val data: String);
case class ConnectMessage(val from: ActorRef);
case class DisconnectMessage(val from: ActorRef);

trait WebSocketNexus {


	
}
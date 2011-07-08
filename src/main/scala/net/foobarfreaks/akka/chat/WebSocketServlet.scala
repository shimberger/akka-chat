package net.foobarfreaks.akka.chat

import org.eclipse.jetty.websocket._
import akka.actor._
import akka.actor.Actor._
import akka.util._
import org.eclipse.jetty.websocket.WebSocket.Connection
import javax.servlet.http._

class WebSocketServlet extends org.eclipse.jetty.websocket.WebSocketServlet {

    override def doGet(request : HttpServletRequest, response : HttpServletResponse) {
    	response.getWriter().write("Whatcha doin here?");
    }    
    
    override def doWebSocketConnect(request : HttpServletRequest, protocol : String )
    	: WebSocket = {
    	// Create a new web socket and provide the path
        return new ProxyWebSocket(request.getPathInfo());
    }	

    /**
     * This is the Jetty WebSocket integration bridge. It will
     * start an actor to send and receive messages on the sockets
     * behalf.
     */
	class ProxyWebSocket(val uri : String) extends WebSocket.OnTextMessage with Logging {
			
			var proxy : ActorRef = null;
		
	        override def onOpen(connection: Connection) {
	        	log.debug("Client connected to WebSocket");
	        	proxy = actorOf(new WebSocketProxy(uri,connection)).start;
	        }
	        
	        /*override def onMessage(frame : Byte, data : Array[Byte],offset : Int, length : Int) {
	        	// We will ignore this so far, since it was
	        	// done in the example too.
	        }
			*/
	
	        override def onMessage(data : String) {
	        	log.debug("Message received from WebSocket '" + data + "'");
	        	proxy ! data;
	        }
		
	        override def onClose(closeCode : Int, msg : String) {
	        	log.debug("Client disconnected from WebSocket");
	        	proxy.stop();
	        }
		
	}
	
	class WebSocketProxy(val uri : String, val connection : Connection) extends Actor with Logging {
		
		var nexusSeq : Seq[ActorRef] = null;
		
		override def preStart = {
			nexusSeq = Actor.registry.actorsFor(classOf[WebSocketNexus]);
			nexusSeq.foreach(arg => arg ! ConnectMessage(self))
		}		

		override def postStop = {
			nexusSeq.foreach(arg => arg ! DisconnectMessage(self))
		}				
		
		def receive = {
			case msg : ServerMessage => {
				log.info("Received server message" + msg.data);
				connection.sendMessage(msg.data);
			}
			case msg : String => {
				log.info("Received client message" + msg);
				nexusSeq.foreach(arg => arg ! ClientMessage(self,msg))
			}
		    case _ => log.info("Received unknown message.")
		}	
		
	}	
    
}
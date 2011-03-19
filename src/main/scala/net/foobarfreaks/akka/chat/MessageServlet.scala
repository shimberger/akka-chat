package net.foobarfreaks.akka.chat

import org.eclipse.jetty.websocket._;
import org.eclipse.jetty.websocket.WebSocket.Outbound;
import javax.servlet.http._

class MessageServlet extends WebSocketServlet {

    override def doGet(request : HttpServletRequest, response : HttpServletResponse) {
    	response.getWriter().write("Whatcha doin here?");
    }    
    
    override def doWebSocketConnect(request : HttpServletRequest, protocol : String )
    	: WebSocket = {
        return new MessageWebSocket();
    }	
	
}

class MessageWebSocket extends WebSocket {
	
		var outbound : Outbound = null;
	
        override def onConnect(outbound : Outbound) {
        	System.err.println("connect");
        	this.outbound = outbound;
        }
        
        override def onMessage(frame : Byte, data : Array[Byte],offset : Int, length : Int) {
        	
        }

        override def onMessage(frame : Byte, data : String) {
        	System.err.println("data" + data);
        	outbound.sendMessage(data);
        }
	
        override def onDisconnect() {
        	System.err.println("disconnect");
        }
	
}
import sbt._

class ChatProject(info: ProjectInfo) extends DefaultProject(info) 
  with AkkaProject
  {

  override def mainClass = Some("akka.kernel.Main")	

  // Dependencies
  val akkaKernel = akkaModule("kernel")
  val akkaHttp = akkaModule("http")  
  val jettyWebsocket = "org.eclipse.jetty"  % "jetty-websocket"   % "7.1.6.v20100715" % "compile" 
  val jettyWebapp = "org.eclipse.jetty"  % "jetty-webapp"   % "7.1.6.v20100715" % "compile" 

}

package net.foobarfreaks.akka.chat

import akka.actor._
import akka.actor.Actor._
import javax.ws.rs.core.MediaType
import akka.http._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.gridfs.Imports._
import org.apache.commons.fileupload._
import servlet.ServletFileUpload

/**
 * This service endpoint dispatches each request
 * to an UploadActor.
 */
class UploadService(val path : String) extends Actor with Endpoint {

  def hook(uri: String): Boolean = uri startsWith path
  def provide(uri: String): ActorRef =  actorOf(new UploadActor(mongoConnection,"test","files")).start
  def mongoConnection(): MongoConnection = MongoConnection()

  override def preStart() = {
    // Before starting attach this service to the root endpoint
    val root = Actor.registry.actorsFor(classOf[RootEndpoint]).head
    root ! Endpoint.Attach(hook, provide)
  }

  // We do nothing more than using the standard
  // MIST http handling i.e. dispatching to an
  // actor
  def receive = handleHttpRequest

}

/**
 * Actor to handle a single file upload.
 */
class UploadActor(val mongo : MongoConnection,val db : String, val collection : String) extends Actor {

  def receive = {

    // handle a get request
    case get: Get =>
      val newObj = MongoDBObject("foo" -> "bar",
        "x" -> "y",
        "pie" -> 3.14,
        "spam" -> "eggs")
      val uploads = mongo("test")("files")
      uploads += newObj
      get.response.setContentType(MediaType.TEXT_HTML)
      val res = "<p>Hello World</p>"
      get.OK(res)

    // handle a post request
    case post: Post =>
      post.response.setContentType(MediaType.TEXT_HTML)
      // Check if this is a file upload
      if (ServletFileUpload.isMultipartContent(post.request)) {
        val upload = new ServletFileUpload();
        val iter = upload.getItemIterator(post.request);
        var success = false;
        while (iter.hasNext()) {
          val item = iter.next();
          if (!item.isFormField) {
            val fileStream = item.openStream()
            val gridfs = GridFS(mongo(db))
            gridfs(fileStream) {
              fh =>
                fh.filename = item.getName()
                fh.contentType = item.getContentType()
            }
            success = true
          }
        }
        if (success) {
          post.OK("Upload Successfull")
        } else {
          post.Error("Upload failed")
        }
      } else {
        post.BadRequest("No multipart request")
      }

    // all other http requests
    case other: RequestMethod =>
      other.NotAllowed("Invalid method")

  }

}
import java.io.File

import akka.actor.Props
import akka.routing.FromConfig
import config.{ActorSystems, MyStaticHandlerConfig}
import main.scala.thrust._
import models.Autocomplete
import org.apache.commons.io.FileUtils
import org.mashupbots.socko.events.HttpResponseStatus
import org.mashupbots.socko.handlers.{StaticContentHandler, StaticFileRequest}
import org.mashupbots.socko.routes._
import org.mashupbots.socko.webserver.{WebServer, WebServerConfig}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Success, Try}
import argonaut._, Argonaut._

object Application extends App {
//  Window.create("http://localhost:8888/hello").foreach { w =>
  //De este modo no necesito al web server.. woop wopp !
  val classpathRoot = new File(ClassLoader.getSystemClassLoader.getResource("").getPath());
  Window.create(s"file://$classpathRoot/../../../src/main/www/index.html").foreach { w =>
    w.show
    w.maximize
    w.openDevtools
    w.focus(true)
    //w.onBlur(println("we were blurred"))
    //w.onFocus(println("we were focused"))
//    Menu.create("MyMenu").foreach { m =>
//      val i = MenuItem("Item1", _ => println("Item1 was clicked"))
//      m.addItem(i)
//      m.popup(w)
//    }
    w.onClosed {
      println("we were closed")
      System.exit(0)
    }
  }

//######################################################################################################################
//  WebServer
//######################################################################################################################

	val handlerConfig = MyStaticHandlerConfig(ActorSystems.fileActorSystem)

	val staticContentHandlerRouter = ActorSystems.fileActorSystem.actorOf(Props(new StaticContentHandler(handlerConfig))
	.withRouter(FromConfig()).withDispatcher("my-pinned-dispatcher"), "static-file-router")

	val routes = Routes({
		case HttpRequest(request) => request match {
			case GET(Path("/hello")) => {
				Future{
					request.response.write(HttpResponseStatus.OK, "hello !")
				}
			}

			case GET(PathSegments("autocomplete" :: line :: offset :: path :: fileName :: Nil)) => {
				implicit val CodecAutocomplete = casecodec4(Autocomplete.apply, Autocomplete.unapply)("line", "offset", "path", "fileName")
				val a = Autocomplete(line.toInt, offset.toInt, path, fileName).asJson
				//request.response.write(HttpResponseStatus.OK, s"autocomplete/$line/$offset/$path/$fileName")
				request.response.write(HttpResponseStatus.OK, s"$a ᕕ( ᐛ )ᕗ" )
			}

			case (POST(Path("/sketch"))) => {
				Future{
					Try(FileUtils.copyDirectory(new File(""), new File(""))) match {
						case Success(_) => request.response.write(HttpResponseStatus.CREATED, "Created ᕕ( ᐛ )ᕗ")
						case _ => request.response.write(HttpResponseStatus.INTERNAL_SERVER_ERROR, "Someting went wrong ಥ_ಥ")
					}
				}
			}

			case (GET(Path("/hello2"))) => {
				staticContentHandlerRouter ! new StaticFileRequest(request, new File("/usr/local/dev/proyectos/P7Server/src/main/resources/", "foo.html"))
			}
		}

	})


	val webServer = new WebServer(WebServerConfig(), routes, ActorSystems.fileActorSystem)
	Runtime.getRuntime.addShutdownHook(new Thread {
		override def run { webServer.stop() }
	})
	webServer.start()

}
import java.io.File

import akka.actor._
import akka.routing.FromConfig
import com.typesafe.config.ConfigFactory
import main.scala.thrust._
import org.mashupbots.socko.events.HttpResponseStatus
import org.mashupbots.socko.handlers.{StaticContentHandlerConfig, StaticContentHandler, StaticFileRequest}
import org.mashupbots.socko.routes._
import org.mashupbots.socko.webserver.{WebServer, WebServerConfig}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Application extends App {


  //val actorSystem = ActorSystem("RouteExampleActorSystem")



  val actorConfig = """
      my-pinned-dispatcher {
        type=PinnedDispatcher
        executor=thread-pool-executor
      }
      my-static-content-handler {
		    root-file-paths="/usr/local/dev/proyectos/P7Server/src/main/resources, /tmp/x2"
		  }
      akka {
        event-handlers = ["akka.event.slf4j.Slf4jEventHandler"]
        loglevel=DEBUG
        actor {
          deployment {
            /static-file-router {
              router = round-robin
              nr-of-instances = 5
            }
          }
        }
      }"""

  val actorSystem = ActorSystem("FileUploadExampleActorSystem", ConfigFactory.parseString(actorConfig))

  val handlerConfig = MyStaticHandlerConfig(actorSystem)

  val staticContentHandlerRouter = actorSystem.actorOf(Props(new StaticContentHandler(handlerConfig))
    .withRouter(FromConfig()).withDispatcher("my-pinned-dispatcher"), "static-file-router")


  object MyStaticHandlerConfig extends ExtensionId[StaticContentHandlerConfig] with ExtensionIdProvider {
    override def lookup = MyStaticHandlerConfig
    override def createExtension(system: ExtendedActorSystem) =
      new StaticContentHandlerConfig(system.settings.config, "my-static-content-handler")
  }


  //
  // STEP #2 - Define Routes
  // Each route dispatches the request to a newly instanced `TimeHandler` actor for processing.
  // `TimeHandler` will `stop()` itself after processing each request.
  //
  val routes = Routes({

    case HttpRequest(request) => request match {
      case (GET(Path("/hello"))) => {
        Future{
          request.response.write(HttpResponseStatus.OK, "hello !")
        }
      }

      case (GET(Path("/hello2"))) => {
        staticContentHandlerRouter ! new StaticFileRequest(request, new File("/usr/local/dev/proyectos/P7Server/src/main/resources/", "foo.html"))
      }
    }

  })


  val webServer = new WebServer(WebServerConfig(), routes, actorSystem)
  Runtime.getRuntime.addShutdownHook(new Thread {
    override def run { webServer.stop() }
  })
  webServer.start()


//  Window.create("http://localhost:8888/hello").foreach { w =>
  //De este modo no necesito al web server.. woop wopp !
  Window.create("file:///usr/local/dev/proyectos/P7Server/src/main/resources/foo.html").foreach { w =>
    w.show
    w.maximize
    //w.openDevtools
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
}
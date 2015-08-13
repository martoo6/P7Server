import akka.actor.ActorSystem
import main.scala.thrust._
import org.mashupbots.socko.events.HttpResponseStatus
import org.mashupbots.socko.routes._
import org.mashupbots.socko.webserver.{WebServer, WebServerConfig}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Application extends App {


  val actorSystem = ActorSystem("RouteExampleActorSystem")

  //
  // STEP #2 - Define Routes
  // Each route dispatches the request to a newly instanced `TimeHandler` actor for processing.
  // `TimeHandler` will `stop()` itself after processing each request.
  //
  val routes = Routes({

    case HttpRequest(request) => request match {
      // *** HOW TO EXTRACT QUERYSTRING VARIABLES AND USE CONCATENATION ***
      // If the timezone is specified on the query string, (like "/time?tz=sydney"), pass the
      // timezone to the TimeHandler
      case (GET(Path("/hello"))) => {
        Future{
          request.response.write(HttpResponseStatus.OK, "hello !")
        }
      }

      // If favicon.ico, just return a 404 because we don't have that file
      case Path("/favicon.ico") => {
        request.response.write(HttpResponseStatus.NOT_FOUND)
      }
    }

  })


  val webServer = new WebServer(WebServerConfig(), routes, actorSystem)
  Runtime.getRuntime.addShutdownHook(new Thread {
    override def run { webServer.stop() }
  })
  webServer.start()


  Window.create("http://localhost:8888/hello").foreach { w =>
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
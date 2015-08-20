package config

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

/**
 * Created by martin on 20/08/15.
 */
object ActorSystems {
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

	val fileActorSystem = ActorSystem("FileActorSystem", ConfigFactory.parseString(actorConfig))
}

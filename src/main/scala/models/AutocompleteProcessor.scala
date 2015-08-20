package models

import akka.actor.{Props, ActorRef, ActorSystem, Actor}
import org.mashupbots.socko.rest.{RestRequest, PathParam, Method}
import services.CompilerServiceObj

/**
 * Created by martin on 20/08/15.
 */
class AutocompleteProcessor extends Actor with akka.actor.ActorLogging {
	def receive = {
		case req: AutocompleteRequest =>
			sender ! AutocompleteResponse(req.context.responseContext, CompilerServiceObj.complete(req.autocomplete))
			context.stop(self)
	}
}

object AutocompleteProcessor extends AutocompleteProcessor {
	val method = Method.GET
	val path = "/autocomplete/{autocomplete}/"
	val requestParams = Seq(PathParam("petId", "ID of pet that needs to be fetched"))
	def processorActor(actorSystem: ActorSystem, request: RestRequest): ActorRef = actorSystem.actorOf(Props[AutocompleteProcessor])
}
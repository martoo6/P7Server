package config

import akka.actor.{ExtendedActorSystem, ExtensionIdProvider, ExtensionId}
import org.mashupbots.socko.handlers.StaticContentHandlerConfig

/**
 * Created by martin on 20/08/15.
 */
object MyStaticHandlerConfig extends ExtensionId[StaticContentHandlerConfig] with ExtensionIdProvider {
	override def lookup = MyStaticHandlerConfig
	override def createExtension(system: ExtendedActorSystem) = new StaticContentHandlerConfig(system.settings.config, "my-static-content-handler")
}

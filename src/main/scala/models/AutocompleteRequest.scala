package models

import org.mashupbots.socko.rest.{RestResponse, RestResponseContext, RestRequest, RestRequestContext}

/**
 * Created by martin on 08/04/15.
 */
case class Autocomplete(line :Int, offset :Int, path :String, fileName :String)

case class AutocompleteRequest(context: RestRequestContext, autocomplete: Autocomplete) extends RestRequest
case class AutocompleteResponse(context: RestResponseContext, res: String) extends RestResponse
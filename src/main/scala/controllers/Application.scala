//package controllers
//
//import models.AutocompleteRequest
//import play.api._
//import play.api.libs.json.{JsError, JsSuccess, Json}
//import play.api.mvc._
//import services.CompilerServiceObj
//
//object Application extends Controller {
//
//  def index = Action {
//    Ok(views.html.index("Your new application is ready."))
//  }
//
//  def codeComplete = Action(parse.json) { request =>
//    implicit val personReads = Json.reads[AutocompleteRequest]
//
//    request.body.validate[AutocompleteRequest].map { request=>
//      Ok(CompilerServiceObj.complete(request))
//    }.getOrElse(BadRequest)
//
////    request.body.validate[AutocompleteRequest] match {
////      case s:JsError=>
////      case s:JsSuccess[AutocompleteRequest]=>  {
////        Ok(CompilerServiceObj.complete(s.get))
////        //Ok(views.html.index("Your new application is ready."))
////      }
////    }
//
//  }
//}
package controllers

import javax.inject._
import play.api._
import play.api.mvc.Results.Ok
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class Application @Inject()(config: Configuration, val controllerComponents: ControllerComponents, assetsFinder: AssetsFinder)(implicit executionContext: ExecutionContext) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index("Tic Tac Toe", assetsFinder))
  }
}
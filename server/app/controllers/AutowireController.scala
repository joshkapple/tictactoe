package controllers

import com.google.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}
import play.mvc._
import shared.TicTacToeApi
import shared.serialization.Picklers._
import tictactoe.TicTacToeApiImpl

class AutowireController @Inject()(controllerComponents: ControllerComponents)(implicit env: play.Environment,
                                                                              executionContext: ExecutionContext) extends AbstractController(controllerComponents) {
  val autowireServer = new AutowireServer

  val routeList: List[AutowireServer#Router] = List(autowireServer.route[TicTacToeApi](new TicTacToeApiImpl))

  def autowireApiController(path: String) = Action.async(parse.json) { implicit request =>
    val b = request.body.toString()
    val autowireRequest = autowire.Core.Request(
      path.split("/"),
        ujson
          .read(b)
          .asInstanceOf[ujson.Obj]
          .value
          .toMap)

    val route = this.routeList
      .find(r => r.isDefinedAt(autowireRequest))
      .getOrElse(throw new Exception(
        s"route not defined for path: $path, ${autowireRequest.args} ${autowireRequest.path}"))

    val router = route(autowireRequest)
    val resultString = router.map(ujson.write(_))

    resultString.map((x: String) => Ok(x))
  }
}

package client

import client.Mode.PVP
import com.thoughtworks.binding.Binding
import com.thoughtworks.binding.Binding.{BindingSeq, Constants, Var}
import com.thoughtworks.binding.bindable.BindableSeq.ops.toAllBindableSeqOps
import org.lrng.binding.html
import org.scalajs.dom.document
import org.scalajs.dom.raw.{Event, Node}

object TicTacToeApp extends BindingHelpers {

  val game: Var[Option[Game]] = Var(None)
  val ajaxClient = new AjaxClient

  @html def gameSetup: Binding[Node] =
    <div class="col s12">
      <h4>Welcome to Tic Tac Toe! Please select a game mode:</h4>
      <div class="col s4">
        <button onclick={e: Event => game.value = Some(TicTacToeGame(Mode.PVP))}>Player vs. Player</button>
      </div>
      <div class="col s4">
        <button onclick={e: Event => game.value = Some(TicTacToeGame(Mode.AiMovesFirst))}>AI moves first</button>
      </div>
      <div class="col s4">
        <button onclick={e: Event => game.value = Some(TicTacToeGame(Mode.AiMovesSecond))}>AI moves second</button>
      </div>
    </div>

  @html def playArea: Binding[Node] =
    <div class="container">
      <div class="row">
        {game.bind match {
          case None => gameSetup
          case Some(game: Game) => game.render
        }}
      </div>
    </div>

  @html def root: Binding[BindingSeq[Node]] = Binding apply {
    Constants(playArea)
  }.bindSeq

  def main(args: Array[String]): Unit = {
    html.render(document.getElementById("scalajs-app"), root)
  }
}

package client

import com.thoughtworks.binding.Binding
import com.thoughtworks.binding.Binding.{Constants, Var}
import org.lrng.binding.html
import org.scalajs.dom.raw.{Event, Node}
import org.scalajs.dom.window
import autowire._
import client.Mode.{AiMovesFirst, AiMovesSecond, PVP}
import shared.TicTacToeSymbols.{PlayerOne, PlayerTwo}
import shared.serialization.Picklers._
import shared.{Coordinates, Move, TicTacToeApi, TicTacToeBoard, TicTacToeSymbols}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait Game {
  val mode: Mode
  def render: Binding[Node]
}

case class TicTacToeGame(override val mode: Mode) extends Game with BindingHelpers {
  val board: Var[TicTacToeBoard] = Var(TicTacToeBoard())

  val currentPlayer: Var[TicTacToeSymbols] = Var(PlayerOne)
  val thinking: Var[Boolean] = Var(false)

  mode match {
    case Mode.AiMovesFirst => {
      thinking.value = true
      TicTacToeApp.ajaxClient[TicTacToeApi].getFirstMove().call().map(m => {board.value = board.value.mutateBoard(m); thinking.value = false})
      pvpAdvanceTurn()
    }
    case _ =>
  }

  def pvpAdvanceTurn() = currentPlayer.value = currentPlayer.value match {
    case PlayerOne => PlayerTwo
    case PlayerTwo => PlayerOne
  }

  def aiMove(): Unit ={
    currentPlayer.value = if (mode == AiMovesFirst){
      PlayerOne
    } else PlayerTwo

    val opponent = mode match {
      case AiMovesFirst => PlayerOne
      case AiMovesSecond => PlayerTwo
    }

    thinking.value = true
    TicTacToeApp.ajaxClient[TicTacToeApi].getOpponentMove(opponent, board.value).call().map(m => {
      board.value = board.value.mutateBoard(m.copy(playerSymbol = opponent))
      val result = board.value.evaluateBoard


      result match {
        case Some(Some(PlayerOne)) if mode == AiMovesFirst => {
          handleConfirm(window.confirm(s"The AI beat you! Would you like to play again?"))
        }
        case Some(Some(PlayerTwo)) if mode == AiMovesSecond => {
          handleConfirm(window.confirm(s"The AI beat you! Would you like to play again?"))
        }
        case _ if mode == AiMovesFirst => {
          thinking.value = false
          currentPlayer.value = PlayerTwo
        }
        case _ if mode == AiMovesSecond => {
          thinking.value = false
          currentPlayer.value = PlayerOne
        }
      }
    })
  }

  def handleConfirm(b: Boolean): Unit ={
    if (b){
      TicTacToeApp.game.value = None
    } else {
      window.location.assign("https://www.joshkapple.com")
    }
  }

  def clickHandler(e: Event, row: Int, column: Int) = {
    if (thinking.value != true) {
      e.preventDefault()
      println(s"player ${currentPlayer.value.toString} clicked ${row.toString}, ${column.toString}")

      board.value = board.value.mutateBoard(Move(currentPlayer.value, Coordinates(row, column)))
      val result = board.value.evaluateBoard

      result match {
        case Some(Some(PlayerOne)) => {
          println(s"${PlayerOne.player.toString} won!")
          handleConfirm(window.confirm(s"Player X won! Would you like to play again?"))
        }
        case Some(Some(PlayerTwo)) => {
          println(s"${PlayerTwo.player.toString} won!")
          handleConfirm(window.confirm(s"Player O won! Would you like to play again?"))
        }
        case Some(None) => {
          handleConfirm(window.confirm(s"Game was a Draw. Would you like to play again?"))
        }
        case None => {
          mode match {
            case PVP => pvpAdvanceTurn()
            case AiMovesFirst | AiMovesSecond => aiMove()
          }

        }
      }
    }
  }

  @html def col(c: Char, rowNum: Int, colNum: Int) = {
    val disabled = c != '_'
    <td onclick={(e:Event) => if (!disabled) clickHandler(e, rowNum, colNum) } >
      {if (!disabled) "" else c.toString}
    </td>
  }

  @html def row(r: Vector[Char], rowNum: Int) = {
    <tr>
      {
      for {
        (c,colnum) <- r.zipWithIndex
      } yield {
        col(c, rowNum, colnum)
      }
      }
    </tr>
  }

  @html def render = Binding apply {
    val thinkingClass = if (thinking.bind) "thinking" else ""
    <div class="col s12">
      <h1>Player: {currentPlayer.bind.symbol.toString}</h1>
      <table class={s"centered $thinkingClass"}>
        {
        val elements = for {
          (r,i) <- board.bind.grid.zipWithIndex
        } yield {
          row(r, i)
        }
        Constants(elements: _*)
        }
      </table>
    </div>
  }.bind
}

sealed trait Mode
object Mode {
  case object PVP extends Mode
  case object AiMovesFirst extends Mode
  case object AiMovesSecond extends Mode
}


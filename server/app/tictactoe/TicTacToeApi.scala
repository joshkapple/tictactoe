package tictactoe

import shared.{Board, Coordinates, Move, PlayerSymbol, TicTacToeApi, TicTacToeBoard, TicTacToeSymbols}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

class TicTacToeApiImpl extends TicTacToeApi {
  override def getOpponentMove(aiPlayer: PlayerSymbol, board: TicTacToeBoard): Future[Move] = {
    Future{
      val validMoves = board.validMoves
      val r = new Random()
      val nextMove = validMoves(r.nextInt(validMoves.size))
      Move(aiPlayer, Coordinates(nextMove._1, nextMove._2))
    }
  }

  override def getFirstMove(): Future[Move] = {
    Future{Move(TicTacToeSymbols.PlayerX, Coordinates(1,1))}
  }
}

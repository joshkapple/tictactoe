package shared

import scala.concurrent.Future

trait TicTacToeApi {
  def getOpponentMove(aiPlayer: PlayerSymbol, board: TicTacToeBoard): Future[Move]
  def getFirstMove(): Future[Move]
}

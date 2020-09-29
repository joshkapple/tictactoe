package shared.serialization

import shared.{Coordinates, Move, Player, PlayerSymbol, TicTacToeBoard, TicTacToeSymbols}
import upickle.default.{Writer, macroRW, ReadWriter => RW}
import upickle.default._
import upickle._


object Picklers {
  implicit val coordinatesPickler: RW[Coordinates] = macroRW
  implicit val playerPickler: RW[Player] = macroRW
  implicit val ticTacToeSymbolsPickler: RW[TicTacToeSymbols] = macroRW
  implicit val playerSymbolPickler: RW[PlayerSymbol] = macroRW
  implicit val boardPickler: RW[TicTacToeBoard] = macroRW
  implicit val movePickler: RW[Move] = macroRW
}

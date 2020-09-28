package shared

import enumeratum._

sealed trait PlayerSymbol {
  val symbol: Char
  val player: Player
}

sealed trait Player
object Players {
  case object Player1 extends Player
  case object Player2 extends Player
}

sealed trait TicTacToeSymbols extends EnumEntry with PlayerSymbol
object TicTacToeSymbols extends Enum[TicTacToeSymbols] {
  val values = findValues
  case object PlayerOne extends PlayerSymbol with TicTacToeSymbols {val symbol = 'X'; val player: Player = Players.Player1}
  case object PlayerTwo extends PlayerSymbol with TicTacToeSymbols {val symbol = 'O'; val player: Player = Players.Player2}
}

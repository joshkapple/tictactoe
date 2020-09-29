package shared

case class Coordinates(val row: Int, val column: Int)

case class Move(playerSymbol: PlayerSymbol, coordinates: Coordinates)

sealed trait Board {
  val grid: Vector[Vector[Char]]
  val size: (Int,Int)
  val moveHistory: Seq[Move]

  def mutateBoard(move:Move): Board
  //def completionState: CompletionState

  def validMoves: List[(Int, Int)] = {
    val allMoves = for {
      (row, rowNum) <- grid.zipWithIndex
      (_, colNum) <- row.zipWithIndex
    } yield {
      grid(rowNum)(colNum) match {
        case ' ' | '_' => Some((rowNum), (colNum))
        case _         => None
      }
    }
    util.Random.shuffle(allMoves.toList.flatten)
  }
}

sealed trait CompletionState {
  val winningPlayer: Option[Option[PlayerSymbol]]
}


case class TicTacToeBoard(grid: Vector[Vector[Char]], moveHistory: Seq[Move]) extends Board {
  override val size: (Int, Int) = TicTacToeBoard.size

  override def mutateBoard(move:Move): TicTacToeBoard = {
    val newVector: Vector[Vector[Char]] = grid.updated(move.coordinates.row, grid(move.coordinates.row).updated(move.coordinates.column, move.playerSymbol.symbol))
    copy(grid = newVector, moveHistory = moveHistory :+ move)
  }

  def evaluateBoard: Evaluated = {

    // evaluate horizontal rows
    val horizontalStrings: List[String] = (for {
      row <- grid
    } yield {
      row.mkString
    }).toList

    // evaluate vertically
    val columnSize = grid(0).size - 1
    val verticalStrings: List[String] = {
      (for {
        i <- 0 to columnSize
      } yield {
        (for {
          j <- 0 to columnSize
        } yield grid(j)(i)).mkString
      }).toList
    }

    // diagonals
    val firstDiagonal: String = List(grid(0)(0), grid(1)(1), grid(2)(2)).mkString
    val secondDiagonal: String = List(grid(2)(0), grid(1)(1), grid(0)(2)).mkString

    val allStrings: List[String] = horizontalStrings ::: verticalStrings ::: List(firstDiagonal,secondDiagonal)

    var xScore = 0
    var oScore = 0

    allStrings.foreach {
      // score X
      case "XXX" => xScore += Int.MaxValue
      case "X_X" => xScore += 100
      case "XX_" => xScore += 100
      case "_XX" => xScore += 100
      case "OXO" => xScore += 200
      case "OOX" => xScore += 200
      case "XOO" => xScore += 200
      // score O
      case "OOO" => oScore += Int.MaxValue
      case "O_O" => oScore += 100
      case "OO_" => oScore += 100
      case "_OO" => oScore += 100
      case "XOX" => oScore += 200
      case "XXO" => oScore += 200
      case "OXX" => oScore += 200
      case _ =>
    }

    val winningPlayer = (xScore,oScore) match {
      case (Int.MaxValue, _) => Some(TicTacToeSymbols.PlayerX)
      case (_, Int.MaxValue) => Some(TicTacToeSymbols.PlayerO)
      case _ => None
    }

    val allMovesPlayed = !grid.flatten.toList.contains('_')

    Evaluated(winningPlayer, allMovesPlayed, xScore, oScore)
  }
}

case class Evaluated(winningPlayer: Option[TicTacToeSymbols], allMovesPlayed: Boolean, xScore: Int, oScore: Int)

object TicTacToeBoard{
  val size: (Int, Int) = (3,3)

  def apply(): TicTacToeBoard = {
    TicTacToeBoard(Vector.fill(size._1, size._2)('_'), Nil)
  }
}

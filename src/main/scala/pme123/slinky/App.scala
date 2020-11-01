package pme123.slinky

import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class Game extends Component {
  type Props = Unit

  case class State(history: Seq[HistoryEntry], xIsNext: Boolean)

  def initialState: State = State(Seq(HistoryEntry()), xIsNext = true)

  private def handleClick(squareIndex: Int) {
    val history: Seq[HistoryEntry] = state.history
    val current = history.last
    val existingValue = calculateWinner(current) orElse current.squares(squareIndex)
    if (existingValue.isEmpty) {
      val newSquares = HistoryEntry(current.squares.updated(
        squareIndex,
        existingValue orElse nextPlayer
      ))
      setState(State(history :+ newSquares, !state.xIsNext))
    }
  }

  def render(): ReactElement = {
    val history = state.history
    val current = history.last
    val status = calculateWinner(current)
      .map("Winner: " + _)
      .getOrElse(s"Next player ${nextPlayer.mkString}")

    div(className := "game")(
      div(className := "game-board")(
        Board(current.squares, i => handleClick(i))
      ),
      div(className := "game-info")(
        div(className := "status")(status)
      )
    )
  }

  private def calculateWinner(entry: HistoryEntry): Option[Char] = {
    val lines = List(
      (0, 1, 2),
      (3, 4, 5),
      (6, 7, 8),
      (0, 3, 6),
      (1, 4, 7),
      (2, 5, 8),
      (0, 4, 8),
      (2, 4, 6)
    )
    val sq = entry.squares
    lines.collectFirst {
      case (a, b, c)
        if sq(a).nonEmpty && sq(a) == sq(b) && sq(a) == sq(c) =>
        sq(a).get
    }
  }

  private def nextPlayer = {
    Some(if (this.state.xIsNext) 'X' else '0')
  }

}

@react class Board extends StatelessComponent {

  case class Props(squares: Seq[Option[Char]], onClick: Int => ())

  def render: ReactElement = div(
    for (r <- 0 to 2)
      yield div(key := s"row_$r", className := "board-row")(
        for (c <- 0 to 2)
          yield renderSquare(r * 3 + c)
      )
  )

  private def renderSquare(squareIndex: Int): ReactElement =
    Square(props.squares(squareIndex), () => props.onClick(squareIndex))
      .withKey(s"square_$squareIndex")

}

@react class Square extends StatelessComponent {

  case class Props(value: Option[Char], onClick: () => ())

  def render(): ReactElement =
    button(
      className := "square",
      onClick := (_ => props.onClick())
    )(this.props.value.mkString)

}

case class HistoryEntry(squares: Seq[Option[Char]] = List.fill(9)(None))

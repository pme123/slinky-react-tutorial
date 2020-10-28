package pme123.slinky

import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class Game extends StatelessComponent {
  type Props = Unit

  def render(): ReactElement =
    div(className := "game")(
      div(className := "game-board")(
        Board()
      ),
      div(className := "game-info")(
        //div("TODO status")
        // ol()
      )
    )
}

@react class Board extends Component {
  type Props = Unit

  case class State(squares: Seq[Option[Char]], xIsNext: Boolean)

  def initialState: State = State(List.fill(9)(None), xIsNext = true)

  def render: ReactElement = div(
    div(className := "status")(status) +: renderRows: _*
  )

  private def renderRows =
    for (r <- 0 to 2)
      yield div(className := "board-row")(
        for (c <- 0 to 2)
          yield renderSquare(r * 3 + c)
      )

  private def handleClick(squareIndex: Int)() {
    val existingValue = calculateWinner() orElse this.state.squares(squareIndex)
    if(existingValue.isEmpty)
      this.setState(State(this.state.squares.updated(
        squareIndex,
        existingValue orElse nextPlayer
      ), !state.xIsNext))
  }

  private def status =
    calculateWinner()
      .map("Winner: " + _)
      .getOrElse(s"Next player ${nextPlayer.mkString}")

  private def nextPlayer = {
    Some(if (this.state.xIsNext) 'X' else '0')
  }

  private def renderSquare(squareIndex: Int): ReactElement = {
    Square(state.squares(squareIndex), handleClick(squareIndex))
  }

  private def calculateWinner(): Option[Char] = {
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
    val squares = state.squares
    lines.collectFirst {
      case (a, b, c)
        if squares(a).nonEmpty && squares(a) == squares(b) && squares(a) == squares(c) =>
        squares(a).get
    }
  }

}

@react class Square extends StatelessComponent {

  case class Props(value: Option[Char], onClick: () => ())

  def render(): ReactElement =
    button(
      className := "square",
      onClick := (_ => props.onClick())
    )(this.props.value.mkString)

}

package pme123.slinky

import org.scalajs.dom.window
import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
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

  case class State(squares: Seq[String],  xIsNext: Boolean)

  def initialState: State = State(List.fill(9)(""), xIsNext = true)

  def render = div(
    div(className := "status")(status) +: renderRows: _*
  )

  private def renderRows = {
    (for (r <- 0 to 2)
      yield div(className := "board-row")(
        for (c <- 0 to 2)
          yield renderSquare(r * 3 + c)
      ))
  }

  private def handleClick(squareIndex: Int)() {
    val squares= this.state.squares.updated(squareIndex, nextPlayer)
    this.setState(State(squares, !state.xIsNext))
  }

  private def status = s"Next player: $nextPlayer"

  private def nextPlayer = {
    if (this.state.xIsNext) "X" else "O"
  }

  private def renderSquare(squareIndex: Int): ReactElement = {
    Square(state.squares(squareIndex), handleClick(squareIndex))
  }
}

@react class Square extends StatelessComponent {

  case class Props(value: String, onClick: () => ())


  def render(): ReactElement =
    button(
      className := "square",
      onClick := (_ => props.onClick())
    )(this.props.value)

}

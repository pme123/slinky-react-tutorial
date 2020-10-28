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

  case class State(squares: Seq[String])

  def initialState: State = State(List.fill(9)(""))

  val status = "Next player: X"

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
    val squares = this.state.squares.updated(squareIndex, "X")
    this.setState(State(squares))
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

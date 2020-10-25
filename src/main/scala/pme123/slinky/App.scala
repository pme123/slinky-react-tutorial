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

@react class Board extends StatelessComponent {
  type Props = Unit

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

  private def renderSquare(squareValue: Int): ReactElement =
    Square(value = squareValue)
}

@react class Square extends Component {

  case class Props(value: Int)
  case class State(value: String)

  def initialState: State = State("")

  def render(): ReactElement =
    button(className := "square",
      onClick := (_ => {
        setState(State("X"))
      })) (state.value)

}

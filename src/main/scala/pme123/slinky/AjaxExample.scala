package pme123.slinky

import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.Ajax
import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.{Fragment, ReactElement}
import slinky.web.html._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.{Failure, Success}

@react class AjaxExample extends Component {
  type Props = Unit

  case class State(persons: Seq[SwapiPerson],
                   error: Option[String],
                   isLoaded: Boolean)

  def initialState: State = State(Seq.empty, None, isLoaded = false)

  override def render(): ReactElement = {
    val State(items, error, isLoaded) = this.state
    Fragment(
      h1("Star Wars People"),
      (error, isLoaded) match {
        case (Some(error), _) =>
          div(s"Error: $error")
        case (_, false) =>
          div("Loading...")
        case _ =>
          ul(
            items.map(item =>
              li(key := item.name)(
                s"${item.name}: ${item.url}"
              )
            )
          )
      }
    )
  }

  override def componentDidMount(): Unit = {
    Ajax.get("https://swapi.dev/api/people/")
      .onComplete {
        case Success(xhr: XMLHttpRequest) =>
          val data = ujson.read(xhr.responseText)
          val results = data("results").arr.map { r => SwapiPerson(r("name").str, r("url").str) }.toSeq
          setState(State(results, None, isLoaded = true))
        case Failure(ex) =>
          setState(State(Nil, Some(ex.getMessage), isLoaded = true))
      }
  }

}

case class SwapiPerson(name: String, url: String)


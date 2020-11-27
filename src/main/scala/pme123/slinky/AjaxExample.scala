package pme123.slinky

import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.Ajax
import slinky.core._
import slinky.core.facade.Fragment
import slinky.core.facade.Hooks.{useEffect, useState}
import slinky.web.html._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.{Failure, Success}

object ajax {

  val SwapiPersons: FunctionalComponent[Unit] = FunctionalComponent[Unit] { _ =>
    val (error, setError) = useState[Option[String]](None)
    val (isLoaded, setIsLoaded) = useState(false)
    val (items, setItems) = useState(Seq.empty[SwapiPerson])

    // Note: the empty deps array [] means
    // this useEffect will run once
    useEffect(() => Ajax.get("https://swapi.dev/api/people/")
      .onComplete {
        case Success(xhr: XMLHttpRequest) =>
          val data = ujson.read(xhr.responseText)
          val results = data("results").arr.map { r => SwapiPerson(r("name").str, r("url").str) }.toSeq
          setIsLoaded(true)
          setItems(results)
        case Failure(ex) =>
          setIsLoaded(true)
          setError(Some(ex.toString))
      }, Seq.empty)

    Fragment(
      h1("Star Wars People"),
      (error, isLoaded) match {
        case (Some(msg), _) =>
          div(s"Error: $msg")
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
}

case class SwapiPerson(name: String, url: String)


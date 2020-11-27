package pme123.slinky

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportTopLevel, JSImport}
import scala.scalajs.LinkingInfo
import slinky.core._
import slinky.web.ReactDOM
import slinky.hot
import org.scalajs.dom
import pme123.slinky.ajax.SwapiPersons

@JSImport("resources/index.css", JSImport.Default)
@js.native
object IndexCSS extends js.Object

object Main {
  val css = IndexCSS

  @JSExportTopLevel("main")
  def main(): Unit = {
    if (LinkingInfo.developmentMode) {
      hot.initialize()
    }

    ReactDOM.render(Game(), dom.document.getElementById("root"))
    ReactDOM.render(SwapiPersons(), dom.document.getElementById("swapi"))
  }
}

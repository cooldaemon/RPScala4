package models

import scala.io.Source
import scala.xml.{Node, NodeSeq}
import scala.xml.parsing.XhtmlParser

class ParseAPIDoc {
  def getTitles: Seq[String] = {
    val source = Source.fromFile(
      "/opt/local/share/scala-2.8/doc/scala-devel-docs/api/index.html"
    )
    val xhtml = XhtmlParser(source)
    source.close

    List(
      "div" -> compareAttr("id", "browser"),
      "div" -> compareAttr("id", "tpl"),
      "ol"  -> compareAttr("class", "packages"),
      "li"  -> compareAttr("class", "pack"),
      "ol"  -> (
               compareAttr("class", "templates")
        orElse compareAttr("class", "packages")
      )
    ).foldLeft (
      xhtml \ "body"
    ) {
      (nodes, selector) => (nodes \ selector._1).filter(
        selector._2 orElse ignorePattern
      )
    } \ "li" map(node => (node \ "@title").text)
  }

  private def compareAttr(attrPrefix: String, text: String) = {
    val attr = "@" + attrPrefix
    var pf: PartialFunction[Node, Boolean] = {
      case node if (node \ attr).text == text => true
    }
    pf
  }

  private def ignorePattern: PartialFunction[Node, Boolean] = {case _ => false}
}

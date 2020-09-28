package client

import com.thoughtworks.binding.Binding
import com.thoughtworks.binding.Binding.BindingSeq
import org.lrng.binding.html.NodeBinding
import org.scalajs.dom.raw.{HTMLDivElement, Node}

import scala.xml.Elem

/**
 * Implicits that suppress IDE alerts to type mismatches.
 * Caused by the @html macro.
 */
trait BindingHelpers{
  implicit def makeIntellijHappy(x: Elem): NodeBinding[HTMLDivElement] = ???
  implicit def makeIntellijHappy(x: Binding.F[BindingSeq[Binding[Node]]]) = ???
}

package controllers

import ujson.Value
import upickle.default._
import upickle._

class AutowireServer()
  extends autowire.Server[Value,
    upickle.default.Reader,
    upickle.default.Writer] {

  def write[Result: upickle.default.Writer](r: Result) =
    upickle.default.writeJs(r)

  def read[Result: upickle.default.Reader](p: Value) = {
    val r = upickle.default.read[Result](p)
    r
  }
}
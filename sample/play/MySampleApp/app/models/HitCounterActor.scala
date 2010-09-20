package models

import se.scalablesolutions.akka.actor._

case object Increment
case object GetCount

class HitCounterActor extends Actor {
  private var counter = 0

  def receive = {
    case Increment => counter = counter + 1
    case GetCount  => reply(counter)
  }
}

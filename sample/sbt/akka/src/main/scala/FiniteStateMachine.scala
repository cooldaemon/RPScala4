import scala.util.logging.ConsoleLogger
import se.scalablesolutions.akka.actor.Actor

sealed trait Event
case object ToState1 extends Event
case object ToState2 extends Event
case object GetState extends Event

class FiniteStateMachine extends Actor with ConsoleLogger {
  type StateFunction = PartialFunction[Event, Unit]

  var currentState: StateFunction = state1

  def receive = {
    case event: Event => currentState(event)
    case other        => log("received unknown event: %s" format other.toString)
  }

  def state1: PartialFunction[Event, Unit] = {
    case GetState => self.reply("state1")
    case ToState2 => currentState = state2
    case other    => log("received unmatch event: %s" format other.toString)
  }

  def state2: PartialFunction[Event, Unit] = {
    case GetState => self.reply("state2")
    case ToState1 => currentState = state1
    case other    => log("received unmatch event: %s" format other.toString)
  }
}

object FiniteStateMachine {
  def run {
    val FSMActor = Actor.actorOf[FiniteStateMachine]
    FSMActor.start

    println(FSMActor !! (GetState, 1000))
    FSMActor ! ToState2
    println(FSMActor !! (GetState, 1000))
    FSMActor ! ToState1
    println(FSMActor !! (GetState, 1000))
    FSMActor ! ToState1
    println(FSMActor !! (GetState, 1000))
    FSMActor ! ToState2
    println(FSMActor !! (GetState, 1000))
    FSMActor.stop
  }
}

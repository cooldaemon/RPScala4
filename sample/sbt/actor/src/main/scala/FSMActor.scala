import scala.actors.Actor
import scala.util.logging.ConsoleLogger

case object ToState1
case object ToState2
case object GetState

class FSMActor extends Actor with ConsoleLogger {
  def act = react(state1 orElse stop)

  def state1: PartialFunction[Any, Unit] = {
    case GetState =>
      reply("state1")
      react(state1 orElse stop)
    case ToState2 =>
      react(state2 orElse stop)
  }

  def state2: PartialFunction[Any, Unit] = {
    case GetState =>
      reply("state2")
      react(state2 orElse stop)
    case ToState1 =>
      react(state1 orElse stop)
  }

  def stop: PartialFunction[Any, Unit] = {
    case Stop => log("stop")
  }
}

object FSMActor {
  def run {
    val fsmActor = new FSMActor
    fsmActor.start

    println(fsmActor !? (1000, GetState))
    fsmActor ! ToState2
    println(fsmActor !? (1000, GetState))
    fsmActor ! ToState1
    println(fsmActor !? (1000, GetState))
    fsmActor ! ToState1
    println(fsmActor !? (1000, GetState))
    fsmActor ! ToState2
    println(fsmActor !? (1000, GetState))

    fsmActor ! Stop
  }
}

/* result
TryActor Start!
Some(state1)
Some(state2)
Some(state1)
Some(state1)
Some(state1)
stop
TryActor Stop!
*/

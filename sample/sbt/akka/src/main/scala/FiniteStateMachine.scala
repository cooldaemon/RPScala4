import se.scalablesolutions.akka.actor.Actor

sealed trait Event
case object ToState1 extends Event
case object ToState2 extends Event
case object GetState extends Event

class FiniteStateMachine extends Actor {
  type StateFunction = PartialFunction[Event, Unit]

  var currentState: StateFunction = state1

  def receive = {
    case event: Event if currentState.isDefinedAt(event) =>
      currentState(event)
    case unknown =>
      log.warning("unknown message [%s], ignoring", unknown)
  }

  def state1: PartialFunction[Event, Unit] = {
    case GetState => self.reply("state1")
    case ToState2 => currentState = state2
  }

  def state2: PartialFunction[Event, Unit] = {
    case GetState => self.reply("state2")
    case ToState1 => currentState = state1
  }
}

object FiniteStateMachine {
  def run {
    val fsmActor = Actor.actorOf[FiniteStateMachine]
    fsmActor.start

    println(fsmActor !! (GetState, 1000))
    fsmActor ! ToState2
    println(fsmActor !! (GetState, 1000))
    fsmActor ! ToState1
    println(fsmActor !! (GetState, 1000))
    fsmActor ! ToState1 // warning
    println(fsmActor !! (GetState, 1000))
    fsmActor ! ToState2
    println(fsmActor !! (GetState, 1000))

    fsmActor.stop
  }
}

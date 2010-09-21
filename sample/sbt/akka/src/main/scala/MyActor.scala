import se.scalablesolutions.akka.actor.Actor

class MyActor extends Actor {
  def receive = {
    case "test"  => log.info("received test")
    case unknown => log.warning("unknown message [%s], ignoring", unknown)
  }
}

object MyActor {
  def run {
    val myActor = Actor.actorOf[MyActor]
    myActor.start

    myActor ! "test"
    myActor ! "foo"

    Thread.sleep(2000)
    myActor.stop
  }
}

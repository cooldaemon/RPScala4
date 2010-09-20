import se.scalablesolutions.akka.actor.Actor

class MyActor extends Actor {
  def receive = {
    case "test" => println("received test")
    case _ =>      println("received unknown message")
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

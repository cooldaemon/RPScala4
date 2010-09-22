import scala.actors.Actor
import scala.util.logging.ConsoleLogger

class MyActor extends Actor with ConsoleLogger {
  def act = loop {
    react {
      case "test"  => log("received test")
      case "stop"  => exit()
      case unknown => log("unknown message [%s], ignoring" format unknown)
    }
  }
}

object MyActor {
  def run {
    val myActor = new MyActor
    myActor.start

    myActor ! "test"
    myActor ! "foo"

    myActor ! "stop"
  }
}

/* result
TryActor Start!
TryActor Stop!
received test
unknown message [foo], ignoring
*/

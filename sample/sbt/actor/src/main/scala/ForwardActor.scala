import scala.actors.Actor
import Actor.actor
import scala.util.logging.ConsoleLogger

case object Get
//case object Stop

class ForwardActor extends Actor with ConsoleLogger {
  def act = loop {
    react {
      case Get =>
        val replyActor = new ReplyActor;
        replyActor.start
        replyActor.forward(Reply)
      case Stop =>
        exit()
      case unknown =>
        log("unknown message [%s], ignoring" format unknown)
    }
  }
}

case object Reply
class ReplyActor extends Actor with ConsoleLogger {
  def act = react {
    case Reply => 
      Thread.sleep(500)
      reply("hello")
  } 
}

object ForwardActor {
  def run {
    val forwardActor = new ForwardActor
    forwardActor.start

    val futures = (1 to 10).map(n => forwardActor !! Get)
    while (futures.forall(!_.isSet)) {
      Thread.sleep(250)
    }
    futures.foreach(f => println(f()))

    forwardActor ! Stop
  }
}

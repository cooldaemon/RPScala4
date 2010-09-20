import se.scalablesolutions.akka.actor.Actor
import se.scalablesolutions.akka.dispatch.Dispatchers

class WorkstealingActor(name: String) extends Actor {
  self.dispatcher = WorkstealingActor.dispatcher
  def receive = {
    case "test" =>
      Thread.sleep(500)
      println("received test:" + name)
    case _ =>
      println("received unknown message")
  }
}

object WorkstealingActor {
  val dispatcher = Dispatchers.newExecutorBasedEventDrivenWorkStealingDispatcher(
    "pooled-dispatcher"
  )

  def run {
    val actors = List("foo", "bar", "baz").map(
      name => Actor.actorOf(new WorkstealingActor(name)).start
    )

    for (i <- 1 to 10) {
      actors(0) ! "test"
    }

    Thread.sleep(2000)
    actors.foreach(_.stop)
  }
}

import se.scalablesolutions.akka.actor.{
  Actor,
  MaximumNumberOfRestartsWithinTimeRangeReached
}
import se.scalablesolutions.akka.actor.Actor.actorOf
import se.scalablesolutions.akka.config.ScalaConfig.{LifeCycle, Permanent}
import se.scalablesolutions.akka.config.AllForOneStrategy

class SupervisorActor extends Actor {
  self.lifeCycle = Some(LifeCycle(Permanent))
  self.trapExit = List(classOf[Throwable])
  self.faultHandler = Some(AllForOneStrategy(5, 5000))

  def receive = {
/*  // Can't restart an actor that has been shut down with 'stop' or 'exit'
    case MaximumNumberOfRestartsWithinTimeRangeReached(child, _, _, _) => child.start
*/
    case message => log.info("received message: %s", message)
  }
}

case class Div(n: Int)
class ChildActor(name: String) extends Actor {
  def receive = {
    case Div(n)  => self.reply_?(1/n)
    case unknown => log.warning("unknown message [%s], ignoring", unknown)
  }

  override def preRestart(reason:Throwable) =
    log.info("pre restart called(%s): %s", name, reason)
  override def postRestart(reason:Throwable) =
    log.info("post restart called(%s): %s", name, reason)
}


object SupervisorActor {
  def run {
    val supervisor = actorOf[SupervisorActor]
    supervisor.start 

    val childFirst = actorOf(new ChildActor("first"))
    supervisor.startLink(childFirst)

    val childSecond = actorOf(new ChildActor("second"))
    supervisor.startLink(childSecond)

    println(childFirst !! (Div(1), 1000))
    println(childSecond !! (Div(1), 1000))

//  println(childFirst !! (Div(0), 1000)) // doesn't return.
    childFirst ! Div(0)
    println(childFirst !! (Div(1), 1000))

/*
    childFirst ! Div(0)
    childFirst ! Div(0)
    childFirst ! Div(0)
    childFirst ! Div(0)
    childFirst ! Div(0)
    childFirst ! Div(0)

    Thread.sleep(2000)
    childFirst ! Div(1) // doesn't return.
*/

    supervisor.shutdownLinkedActors
    supervisor.stop
  }
}

import scala.actors.{Actor, Exit}
import scala.util.logging.ConsoleLogger

case class Link(childActor: Actor)
class SupervisorActor extends Actor with ConsoleLogger {
  trapExit = true

  def act = loop {
    react {
      case Link(child :Actor) =>
        link(child)
      case Exit(child :Actor, reason) =>
        log("receive Exit: %s" format reason)
        child.restart
      case Stop =>
        exit()
      case unknown =>
        log("unknown message [%s], ignoring" format unknown)
    }
  }
}

case class Div(n: Int)
class ChildActor extends Actor with ConsoleLogger {
  trapExit = true

  def act = loop {
    react {
      case Exit(_, reason) =>
        log("receive Exit: %s" format reason)
        exit()
      case Div(n) =>
        reply(10 / n)
      case Stop =>
        exit()
    } 
  }
}

object SupervisorActor {
  def run {
    alone()
    supervisor()
  }

  def alone() = {
    val childActor = new ChildActor
    childActor.start

    println(childActor.getState)

    println(childActor !? (1000, Div(10)))
    println(childActor !? (1000, Div(0)))

    println(childActor.getState)

    childActor.restart
    println(childActor !? (1000, Div(10)))
    childActor ! Stop
  }

  def supervisor() = {
    val supervisorActor = new SupervisorActor
    supervisorActor.start

    val childActor = new ChildActor
    childActor.start
    supervisorActor ! Link(childActor)

    println(childActor !? (1000, Div(10)))
    println(childActor !? (1000, Div(0)))
    println(childActor !? (1000, Div(10)))

    supervisorActor ! Stop
  }
}

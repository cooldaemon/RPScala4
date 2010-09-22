import scala.actors.Actor
import scala.util.logging.ConsoleLogger

case class Info(i: Map[String, Int])
case object GetInfo
case class SetInfo(n: String, v: Int)
case class Update(n: String, f: Option[Int] => Int)
case object Stop

class AtomicActor extends Actor with ConsoleLogger {
  private var info: Map[String, Int] = Map()

  def act = loop {
    react {
      case GetInfo       => reply(Info(info))
      case SetInfo(n, v) => info += n -> v
      case Update(n, f)  => info += n -> f(info.get(n))
      case Stop          => exit()
      case unknown       => log("unknown message [%s], ignoring" format unknown)
    }
  }
}

object AtomicActor {
  def run {
    val atomicActor = new AtomicActor
    atomicActor.start

    atomicActor ! SetInfo("Apple", 4)
    atomicActor ! SetInfo("Orange", 5)
    println(atomicActor !? (1000, GetInfo))

    atomicActor ! "Unknown Message!!"

    atomicActor ! Update("Apple", v => (v getOrElse 0) + 2)
    println(atomicActor !? (1000, GetInfo))

    atomicActor ! Stop
  }
}

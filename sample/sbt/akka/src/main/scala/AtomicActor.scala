import se.scalablesolutions.akka.actor.Actor

case class Info(i: Map[String, Int])
case object GetInfo
case class SetInfo(n: String, v: Int)
case class Update(n: String, f: Option[Int] => Int)

class AtomicActor extends Actor {
  private var info: Map[String, Int] = Map()

  def receive = {
    case GetInfo       => self.reply(Info(info))
    case SetInfo(n, v) => info += n -> v
    case Update(n, f)  => info += n -> f(info.get(n))
    case unknown       => log.warning("unknown message [%s], ignoring", unknown)
  }
}

object AtomicActor {
  def run {
    val atomicActor = Actor.actorOf[AtomicActor]
    atomicActor.start

    atomicActor ! SetInfo("Apple", 4)
    atomicActor ! SetInfo("Orange", 5)
    println(atomicActor !! (GetInfo, 1000))

    atomicActor ! "Unknown Message!!"

    atomicActor ! Update("Apple", v => (v getOrElse 0) + 2)
    println(atomicActor !! (GetInfo, 1000))

    atomicActor.stop
  }
}

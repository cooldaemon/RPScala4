import scala.actors.{Actor, TIMEOUT}
import Actor.actor
import scala.util.logging.ConsoleLogger

case object Lock
case object Unlock
case object Locked

class MutexActor extends Actor with ConsoleLogger {
  def act = react(free orElse stop)

  def free: PartialFunction[Any, Unit] = {
    case Lock =>
      log("lock")
      reply(Locked)
      reactWithin(500)(busy orElse stop)
  }

  def busy: PartialFunction[Any, Unit] = {
    case Unlock  =>
      log("unlock")
      react(free orElse stop)
    case TIMEOUT =>
      log("timeout")
      react(free orElse stop)
  }

  def stop: PartialFunction[Any, Unit] = {
    case Stop => log("stop")
  }
}

object MutexActor {
  def run {
    val mutexActor = new MutexActor
    mutexActor.start

    mutexActor !? (1000, Lock)
    mutexActor ! Unlock

    mutexActor !? (1000, Lock)
    actor {
      mutexActor !? (1000, Lock)
      mutexActor ! Unlock
    } 
    Thread.sleep(250)
    mutexActor ! Unlock

    mutexActor !? (1000, Lock)
    Thread.sleep(750)

    mutexActor ! Stop
  }
}

/* result
TryActor Start!
lock
unlock
lock
unlock
lock
unlock
lock
timeout
TryActor Stop!
stop
*/

import se.scalablesolutions.akka.actor.Actor

object TryAkka {
  def main (args:Array[String]) {
    println("TryAkka Start!")
 
    MyActor.run
    WorkstealingActor.run
    AtomicActor.run
    FiniteStateMachine.run
    SupervisorActor.run

    println("TryAkka Stop!")
  }
}

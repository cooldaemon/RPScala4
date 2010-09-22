object TryActor {
  def main (args:Array[String]) {
    println("TryActor Start!")
 
    MyActor.run
    AtomicActor.run
    ForwardActor.run
    FSMActor.run
    MutexActor.run
    SupervisorActor.run

    println("TryActor Stop!")
  }
}

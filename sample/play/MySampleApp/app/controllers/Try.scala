package controllers

import play._
import play.mvc._

object Try extends Controller {
  def seqBench = {
    import scala.testing.Benchmark

    def forTest (count: Long): IndexedSeq[Long] =
      (for (i <- 0L to count if i % 2 == 0) yield i * 2)

    def filterTest (count: Long): IndexedSeq[Long] =
      (0L to count).filter(_ % 2 == 0).map(_ * 2)

    def viewTest (count: Long): Seq[Long] =
      (0L to count).view.filter(_ % 2 == 0).map(_ * 2).force

    <ul>{
      List(forTest _, filterTest _, viewTest _).map(
        fun => (new Benchmark {def run = fun(10000)}) runBenchmark(20)
      ).map(results => <li>{results.toString}</li>)
    }</ul>
  }

  def rabbitmq = {
    import com.rabbitmq.client.ConnectionFactory

    val factory = new ConnectionFactory()
    factory.setUsername("guest")
    factory.setPassword("guest")
    factory.setVirtualHost("/")
    factory.setHost("localhost")
    factory.setPort(5672)

    val conn = factory.newConnection()
    val channel = conn.createChannel()

    val queueName = channel.queueDeclare().getQueue()
    channel.basicPublish("", queueName, null, "Hello RabbitMQ.".getBytes())

    val response = channel.basicGet(queueName, true)
    val message =
      if (response != null) new String(response.getBody(), "UTF-8")
      else "response is null."

    channel.close()
    conn.close()

    <h1>{message}</h1>
  }

  def traitPuzzle = {
    import models.TraitPuzzle._

    val archer  = new Dog("archer") with Athlete with Runner with Male
    val dpp     = new Person("David") with Athlete with Biker with Male
    val john    = new Person("John") with Athlete with Runner with Male
    val annette = new Person("Annette") with Athlete with Runner with Female 

    <ul>
      <li>{archer.name}</li>
      <li>{archer.bodyTemperature}</li>
      <li>{archer.walk}</li>
      <li>{archer.run}</li>
      <li>{dpp.ride}</li>
      <li>{john.run}</li>
    </ul>
  }

  def parseAPIDoc = {
    import models.ParseAPIDoc
    <ul>{((new ParseAPIDoc).getTitles).map(title => <li>{title}</li>)}</ul>
  }
}


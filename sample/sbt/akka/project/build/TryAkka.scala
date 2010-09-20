import sbt._

class TryAkka(info: ProjectInfo)
  extends DefaultProject(info)
  with AkkaProject {
  val scalaTest = "org.scalatest" % "scalatest" % "1.2"
  val akkaAMQP = akkaModule("amqp")
/*
  val akkaCamel = akkaModule("camel")
  val akkaHttp = akkaModule("http")
  val akkaJTA = akkaModule("jta")
  val akkaKernel = akkaModule("kernel")
  val akkaCassandra = akkaModule("persistence-cassandra")
  val akkaMongo = akkaModule("persistence-mongo")
  val akkaRedis = akkaModule("persistence-redis")
  val akkaSpring = akkaModule("spring")
*/
}

package controllers

import play._
import play.mvc._

import se.scalablesolutions.akka.actor.Actor.actorOf

import models._

object TestAkka extends Controller {
  val hitCounter = actorOf[HitCounterActor].start

  def index = {
    hitCounter ! Increment
    var count = (hitCounter !! (GetCount, 1000)).getOrElse(0)
    <h1>{count}</h1>
  }
}


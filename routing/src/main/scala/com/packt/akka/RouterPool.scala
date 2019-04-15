package com.packt.akka

import akka.actor.{Actor, ActorRef, Props}
import com.packt.akka.Worker.Work

class RouterPool extends Actor {

  var routees: List[ActorRef] = _

  override def preStart() = {
    routees = List.fill(5)(
    context.actorOf(Props[Worker])
    )
  }

  def receive() = {
    case msg: Work =>
      println("I'm A Router and I received a Message.....")
      routees(util.Random.nextInt(routees.size)) forward msg
  }
}

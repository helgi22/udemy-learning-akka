package com.packt.akka

import akka.actor.{Actor, ActorRef, Props}
import com.packt.akka.Worker._



class RouterGroup(routees: List[String]) extends Actor {

//  var routees: List[ActorRef] = _
//
//  override def preStart(): Unit = {
//    routees = List.fill(5)(
//      context.actorOf(Props[Worker])
//    )
//  }

  def receive = {
    case msg: Work =>
      println(s"I'm a Router Group and I receive Work Message....")
      context.actorSelection(routees(util.Random.nextInt(routees.size))) forward msg
  }
}
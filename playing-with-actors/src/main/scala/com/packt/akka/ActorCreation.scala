package com.packt.akka

import akka.actor.{Actor, ActorSystem, Props}
import com.packt.akka.MusicController.{Play, Stop}
import com.packt.akka.MusicPlayer.{StartMusic, StopMusic}

//Music Controller Messages
object MusicController {
  sealed trait ControllerMsg
  case object Play extends ControllerMsg
  case object Stop extends ControllerMsg

  def props: Props = Props[MusicController]
}

//Music controller
class MusicController extends Actor {
  override def receive: Receive = {
    case Play => println("Music started .....")
    case Stop => println("Music stopped ......")
  }
}

//Music player Messages
object MusicPlayer {
  sealed trait PlayMsg
  case object StopMusic extends PlayMsg
  case object StartMusic extends PlayMsg

  def props: Props = Props[MusicPlayer]
}

//Music Player
sealed class MusicPlayer extends Actor {
  override def receive: Receive = {
    case StartMusic =>
      val controller = context.actorOf(MusicController.props, "controller")
      controller ! Play
    case StopMusic => println("I don't wont stop music ")
    case _ => println("Unknown Message")
  }
}

object Creation extends App {
  //  Create the creation System actor
  val system = ActorSystem("creation")
  //  Create player actor
  val player = system.actorOf(MusicPlayer.props, "player")
  //  Sent Start music Message to actor
  player ! StartMusic

  //  Setd StopMusic to actor
  player ! StopMusic

  //  Shutdown system
  system.terminate()
}

package com.packt.akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.packt.akka.Checker.{BlackUser, CheckUser, WhiteUser}
import com.packt.akka.Recorder.NewUser
import com.packt.akka.Storage.AddUser

import scala.concurrent.duration._

case class User(username: String, email: String)

object Recorder {
  sealed trait RecorderMsg
  //  Recorder messages
  case class NewUser(user: User) extends RecorderMsg
  def props(checker: ActorRef, storage: ActorRef) =
    Props(new Recorder(checker, storage))
}

class Recorder(cheker: ActorRef, storage: ActorRef) extends Actor {
  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val timeout = Timeout(5 seconds)

  override def receive = {
    case NewUser(user) =>
      println(s"Recorder receives NewUser for ${user}")
      cheker ? CheckUser(user) map {
        case WhiteUser(user) =>
          storage ! AddUser(user)
        case BlackUser(user) =>
          println(s"Recorder: $user in the blacklist")
      }

  }
}

object Checker {
  sealed trait CheckerMsg
  // Checker Messages
  case class CheckUser(user: User) extends CheckerMsg
  sealed trait ChekerResponse
  //  CheckerResponse
  case class BlackUser(user: User) extends CheckerMsg
  case class WhiteUser(user: User) extends CheckerMsg
}

class Checker extends Actor {
  val blackList = List(
    User("Adam", "adam@mail.com")
  )

  override def receive: PartialFunction[Any, Unit] = {
    case CheckUser(user) if blackList.contains(user) =>
      println(s"Chesker: $user in the blacklist")
      sender() ! BlackUser(user)
    case CheckUser(user) =>
      println(s"Check: $user is not in the blacklist")
      sender() ! WhiteUser(user)
  }
}

object Storage {
  case class AddUser(user: User)
}

class Storage extends Actor {
  var users = List.empty[User]

  override def receive: PartialFunction[Any, Unit] = {
    case AddUser(user) =>
      println(s"Storage: $user stored")
      users = user :: users
  }
}


object TalkToActor extends App {
  // Create the 'talk-to-actor' actor system
  val system = ActorSystem("talk-to-actor")
  // Create the 'checker' actor
  val checker = system.actorOf(Props[Checker], "checker")
  // Create the 'storage' actor
  val storage = system.actorOf(Props[Storage], "storage")
  // Create the 'recorder' actor
  val recorder = system.actorOf(Recorder.props(checker, storage), "recorder")
  //send NewUser Message to Recorder
  recorder ! Recorder.NewUser(User("Jon", "jon@packt.com"))

  Thread.sleep(100)

  //shutdown system
  system.terminate()

}
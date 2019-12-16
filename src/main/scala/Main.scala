/* I am trying to find an example where I can use Observer class from
 * https://courses.fit.cvut.cz/BI-OOP/resources/lecture-9.html#/a-better-way
 *
 * I want to ask if my design is "nice" for this particular made up example
 * */

sealed abstract class Person // [K] adding sealed to make sure we have all the definitions of Person in this file

class FreeMember extends Person {
  /* ... */
}

/* Some class representing a premium member who has credit */
class PremiumMember extends Person {
  val m_Id: Int = 0
  var m_credit: Int = 100
  var m_Username: String = ""

  /* method to change Virtual Credit*/
  def changeCredit(ammount: Int): Unit = { m_credit += ammount }

  /* method to change Username*/
  def changeUsername(name: String): Unit = { m_Username = name }
}

trait Dispatcher { // [K] This isn't an observer, this is a dispatcher

  /* intuitively i wrote this one, however I cant make it work  */
  def _change/*(member: PremiumMember)*/(property: Symbol, value: Any) : Unit // [K] we generally leave the word "clazz" to refer to the Class object, see below why i removed it

  /* I dont like that it takes a class as a parameter
   *  Ideally I wouldn't even need to pass the class as a parameter at all
   *  however I am uncertain how else to approach this*/
  def change(/*member: PremiumMember, */property: Symbol, value: Any) : Unit

  // [K] An observer generally will have a list of objects to which it sends an update. So instead of having a (member: PremiumMember) argument,
  // you could have a list of members in the class:
  val members: List[PremiumMember]
}

class ConcreteDispatcher(override val members: List[PremiumMember]) extends Dispatcher { // This is
  def change/*(member: PremiumMember,*/ (property: Symbol, value: Any) : Unit =
    members foreach { member =>
      property match {
        case 'changeCredit => member.changeCredit(value.asInstanceOf[Int])
        case 'changeUsername => member.changeUsername(value.asInstanceOf[String])
      }
    }

  def _change/*(member: PremiumMember)*/(property: Symbol, value: Any) : Unit =
    members foreach { member =>
      property match {
        case 'changeCredit => member.changeCredit(value.asInstanceOf[Int])
        case 'changeUsername => member.changeUsername(value.asInstanceOf[String])
      }
    }
}

object Main extends App {

  // let there be a premium member
  val member1 = new PremiumMember

  // let there be an Observer
  val changer = new ConcreteDispatcher(List(member1))

  // lets assume the member is buying more Virtual credit
  val property = 'changeCredit

  // lets assume he bought 500 Virtual credit
  val new_credit = 500

  println("Credit before observer: " + member1.m_credit) // before

  //Lets update his credit with the observer
  val toChange = changer.change(property, new_credit)
  println("Credit after observer: " + member1.m_credit) // after


  /* ------------ Part I am Struggling with ------------

   * I commented the code so that u can Run the code without errors :) */

  /* Now im trying to make the method I intuitively first wrote
  * Ideally I want to give the Observer the member1 instance
  * And everytime i would call member1_observer I would only specify what to change
  * without needing to specify which member
  */

 //val member1_observer = changer (_change _)

  /* later on if the member adds more credit I would just member1_observer.apply()
  * However I think I am mixing up stuff here */

  changer._change(property, new_credit)
}

// [K] I am really confused what it is that you want to happen here though. Let me try to re-write some of this.

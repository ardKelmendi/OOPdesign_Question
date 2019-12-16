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
class PremiumMember(dispatcher: Dispatcher) extends Person {
  val m_Id: Int = 0
  var m_credit: Int = 100
  var m_Username: String = ""

  def changeCredit(amount: Int): Unit = { m_credit += amount; dispatcher.change(m_credit) } // When something changes in the data, we do dispatch

  def changeUsername(name: String): Unit = { m_Username = name; dispatcher.change(name) }
}

trait Dispatcher { // [K] This isn't an observer, this is a dispatcher
  val listeners: List[Listener]
  def change(credit: Int) : Unit
  def change(name: String) : Unit
}

class ConcreteDispatcher(override val listeners: List[Listener]) extends Dispatcher { // This is
  def change(credit: Int) : Unit =
    listeners foreach { listener => listener.creditChanged(credit) }

  def change(name: String) : Unit =
    listeners foreach { listener => listener.usernameChanged(name) }
}

trait Listener {
  def creditChanged(amount: Int): Unit
  def usernameChanged(name: String): Unit
}

class ConcreteListener extends Listener {
  override def creditChanged(amount: Int): Unit = println(s"Member changed amount to $amount")

  override def usernameChanged(name: String): Unit = println(s"Member changed username to $name")
}

object Main extends App {

  val listener1 = new ConcreteListener

  val dispatcher = new ConcreteDispatcher(List(listener1))

  val member1 = new PremiumMember(dispatcher)

  // lets assume the member is buying more Virtual credit
  val property = 'changeCredit

  // lets assume he bought 500 Virtual credit
  val new_credit = 500

  member1.changeCredit(100)
  member1.changeUsername("sausage")
}

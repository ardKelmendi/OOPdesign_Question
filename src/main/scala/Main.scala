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
  protected val m_Id: Int = 0
  protected var m_credit: Int = 100
  protected var m_Username: String = ""

  def changeCredit(amount: Int): Unit = { m_credit += amount }

  def changeUsername(name: String): Unit = { m_Username = name }
}

class ObservableMember(actualMemeber: PremiumMember, dispatcher: Dispatcher) extends PremiumMember { // This is a wrapper class that does the dispatch, so as not to mess with the PremiumMember class (Decorator pattern)
  override def changeCredit(amount: Int): Unit = { actualMemeber.changeCredit(amount); dispatcher.change(m_credit) } // When something changes in the data, we do dispatch

  override def changeUsername(name: String): Unit = { actualMemeber.changeUsername(name); dispatcher.change(name) }
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

  val member1 = new ObservableMember(new PremiumMember, dispatcher)

  // lets assume the member is buying more Virtual credit
  val property = 'changeCredit

  // lets assume he bought 500 Virtual credit
  val new_credit = 500

  member1.changeCredit(100)
  member1.changeUsername("sausage")
}

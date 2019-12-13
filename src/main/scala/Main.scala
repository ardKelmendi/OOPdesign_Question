/* I am trying to find an example where I can use Observer class from
 * https://courses.fit.cvut.cz/BI-OOP/resources/lecture-9.html#/a-better-way
 *
 * I want to ask if my design is "nice" for this particular made up example
 * */

abstract class Person

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

trait Observer {

  /* intuitively i wrote this one, however I cant make it work  */
  def _change(clazz: PremiumMember)(property: Symbol, value: Any) : Unit


  /* I dont like that it takes a class as a parameter
   *  Ideally I wouldn't even need to pass the class as a parameter at all
   *  however I am uncertain how else to approach this*/
  def change(clazz: PremiumMember, property: Symbol, value: Any) : Unit

}

class ConcreteObserver extends Observer{
  def change(clazz: PremiumMember, property: Symbol, value: Any) : Unit = property match {
    case Symbol("changeCredit")   => clazz.changeCredit(value.asInstanceOf[Int])
    case Symbol("changeUsername") => clazz.changeUsername(value.asInstanceOf[String])
  }


  def _change(clazz: PremiumMember)(property: Symbol, value: Any) : Unit = property match {
    case Symbol("changeCredit")   => clazz.changeCredit(value.asInstanceOf[Int])
    case Symbol("changeUsername") => clazz.changeUsername(value.asInstanceOf[String])
  }

}

object Main extends App {

  // let there be a premium member
  val member1 = new PremiumMember

  // let there be an Observer
  val changer = new ConcreteObserver

  // lets assume the member is buying more Virtual credit
  val property = Symbol("changeCredit")

  // lets assume he bought 500 Virtual credit
  val new_credit = 500

  println("Credit before observer: " + member1.m_credit) // before

  //Lets update his credit with the observer
  val toChange = changer change(member1, property, new_credit)
  println("Credit after observer: " + member1.m_credit) // after


  /* ------------ Part I am Struggling with ------------

   * I commented the code so that u can Run the code without errors :) */

  /* Now im trying to make the method I intuitively first wrote
  * Ideally I want to give the Observer the member1 instance
  * And everytime i would call member1_observer I would only specify what to change
  * without needing to specify which member
  */

 // val member1_observer = changer _change(member1) _

  /* later on if the member adds more credit I would just member1_observer.apply()
  * However I think I am mixing up stuff here */

  //member1_observer.apply(property, new_credit)
}

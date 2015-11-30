package exercises

object Chapter8 extends App {
  class BankingAccount(_balance: BigDecimal) {
    private var balance = _balance
  
    def deposit(amount: BigDecimal) = {
      balance += amount
      balance
    }
  
    def withdraw(amount: BigDecimal) = {
      balance -= amount
      balance
    }
  
    def currentBalance = balance
  
    override def toString: String = "Balance: " + currentBalance
  }
  
  // 1
  class CheckingAccount(_balance: BigDecimal) extends BankingAccount(_balance) {
    val FEE = 1
  
    override def deposit(amount: BigDecimal): BigDecimal = super.deposit(amount - FEE)
  
    override def withdraw(amount: BigDecimal): BigDecimal = super.withdraw(amount + FEE)
  }
  
  val regular = new BankingAccount(100)
  regular.deposit(100)
  regular.withdraw(50)
  println(regular)
  
  // 2
  class SavingAccount(_balance: BigDecimal) extends BankingAccount(_balance) {
    val MONTHLY_SAVING_RATE = 10
    val FEE = 2
  
    val FREE_TX_COUNT = 3
  
    private var txCount = 0
  
    override def deposit(amount: BigDecimal): BigDecimal = {
      txCount += 1
      super.deposit(amount - (if (txCount > FREE_TX_COUNT) FEE else 0))
    }
  
    override def withdraw(amount: BigDecimal): BigDecimal = {
      txCount += 1
      super.withdraw(amount + (if (txCount > FREE_TX_COUNT) FEE else 0))
    }
  
    def earnMonthlyInterest() = {
      super.deposit(currentBalance * (BigDecimal(MONTHLY_SAVING_RATE) / 100))
      txCount = 0
      
      currentBalance
    }
  }

  val saving = new SavingAccount(100)
  saving.deposit(11)
  saving.earnMonthlyInterest()
  println(saving)

  // 3
  // Trying to come up with a relatively original example, and some sensible members?
  // Manuscript
  // + BoundText
  //   + Book
  //   + Magazine
  // + Poster
  class Manuscript(
      val wordCount: Long,
      val year: Int,
      var condition: String)
  class BoundText(
      wordCount: Long,
      val pageCount: Int,
      year: Int,
      condition: String) extends Manuscript(wordCount, year, condition) {
    def avgWordsPerPage = wordCount / pageCount
  }
  class Book(
      wordCount: Long,
      pageCount: Int,
      val blankPageCount: Int,
      year: Int,
      condition: String) extends BoundText(wordCount, pageCount, year,condition) {
    override def avgWordsPerPage = wordCount / (pageCount - blankPageCount)
  }
  class Magazine(
      wordCount: Long,
      pageCount: Int,
      year: Int,
      condition: String) extends BoundText(wordCount, pageCount, year,condition)
  class Poster(
      wordCount: Long,
      year: Int,
      condition: String,
      val xDim: Float,
      val yDim: Float) extends Manuscript(wordCount, year,condition) {
    val area = xDim * yDim
  }
  // Feel free to flesh this out some more. I'm lazy.


  // 4
  abstract class Item {
    def price: BigDecimal
    def description: String
    
    override def toString = description + ": " + price
  }
  
  class SimpleItem(override val price: BigDecimal, override val description: String) extends Item
  
  class Bundle(val items: ListBuffer[Item] = ListBuffer[Item]()) extends Item {
    def +=(item: Item) { items += item }
  
    def price: BigDecimal = items.foldLeft(BigDecimal(0))((sum, item: Item) => sum + item.price)
    def description: String = items.map(_.description).mkString(", ")
  }
  {
    val a = new SimpleItem(100, "test 100")
    val b = new SimpleItem(200, "test 200")
    val composite = new Bundle()
    composite += a
    composite += b
    composite.price
    composite.description
  }

  // 5
  class Point(val x: Double, val y: Double) {
    override def toString = "(%f, %f)".format(x, y)
  }
  class LabeledPoint(val desc: String, x: Double, y: Double) extends Point(x, y) {
    override def toString = desc + " " + super.toString
  }
  {
    println(new Point(3.5, -7.12))
    println(new LabeledPoint("Pointless", 0, 0))
  }


  // 6
  // re-using Point here for the hell of it
  abstract class Shape(x: Double, y: Double) extends Point(x, y) {
    def centerPoint: (Double, Double)
    override def toString = "Center:" + centerPoint
  }

  // modeling such that (x,y) is the top left corner. The coordinate system increases left-right, up-down
  class Rectangle(x: Double, y: Double, val width: Double, val height: Double) extends Shape(x, y) {
    def centerPoint = (x + width / 2, y - height / 2)
    override def toString = "Location:(%f,%f), Height:%f, Width:%f, ".format(x, y, width, height) + super.toString
  }

  // modeling such that (x,y) is the center.
  class Circle(x: Double, y: Double, val radius: Double) extends Shape(x, y) {
    def centerPoint = (x, y)
    override def toString = "Location:(%f,%f), Radius:%f, ".format(x,y,radius) + super.toString()
  }

  {
    println(new Rectangle(1.5, 6.0, 3, 4.5))
    println(new Circle(5, 5, 2.5))
  }


  // 7
  class Square(x: Int, y: Int, width: Int) extends java.awt.Rectangle(x, y, width, width) {
    // This actually creates specifically, a no-arg constructor and one that only takes a width. With the primary
    // constructor, we have the three requested constructor forms.
    def this(width: Int = 0) {
      this(0, 0, width)
    }
  }
  {
    println(new Square)
    println(new Square(5))
    println(new Square(2, 3, 1))
  }


  // 8
  // Copied from the text on page 91
  class Person(val name: String) {
    override def toString = getClass.getName + "[name=" + name + "]"
  }
  class SecretAgent(codename: String) extends Person(codename) {
    override val name = "secret" // Don’t want to reveal name . . . 
    override val toString = "secret" // . . . or class name
  }
  /*
I'm not sure what you're supposed to get out of the -c option here. Anyways here's the output.

C:\workspace\Scala for the Impatient\bin\exercises>javap -private Chapter8$Person
Compiled from "Chapter8.scala"
public class exercises.Chapter8$Person extends java.lang.Object implements scala.ScalaObject{
    private final java.lang.String name;
    public java.lang.String name();
    public java.lang.String toString();
    public exercises.Chapter8$Person(java.lang.String);
}

C:\workspace\Scala for the Impatient\bin\exercises>javap -private Chapter8$SecretAgent
Compiled from "Chapter8.scala"
public class exercises.Chapter8$SecretAgent extends exercises.Chapter8$Person implements scala.ScalaObject{
    private final java.lang.String name;
    private final java.lang.String toString;
    public java.lang.String name();
    public java.lang.String toString();
    public exercises.Chapter8$SecretAgent(java.lang.String);
}

*/


  // 9
  { // original text, added toString
    class Creature {
      val range: Int = 10
      val env: Array[Int] = new Array[Int](range)
      override def toString = super.toString + " Range:" + range + " Environment:" + env.toBuffer
    }
    class Ant extends Creature {
      override val range = 2
    }
    println(new Creature)
    println(new Ant)
    // notice Ant's env was constructed with range=0 as explained in the text, even though 2 is in the println 
  }
  { // replaced Creature's 'val range' with 'def', added toString, in Ant still using 'val'
    class Creature {
      def range: Int = 10
      val env: Array[Int] = new Array[Int](range)
      override def toString = super.toString + " Range:" + range + " Environment:" + env.toBuffer
    }
    class Ant extends Creature {
      override val range = 2
    }
    println(new Creature)
    println(new Ant) // Ant's env is again constructed with range=0, even though range shows 2 in the println
  }
  { // replaced Creature's 'val range' with 'def', added toString, in Ant used 'def'
    class Creature {
      def range: Int = 10
      val env: Array[Int] = new Array[Int](range)
      override def toString = super.toString + " Range:" + range + " Environment:" + env.toBuffer
    }
    class Ant extends Creature {
      override def range = 2
    }
    println(new Creature)
    println(new Ant) // finally this does what we wanted
  }
  // This is already explained in the text (page 95), but essentially by overriding the getter directly (def), we're
  // ensuring the correct overridden range value is returned to the super class during construction.


  // 10
  /* I think the 'protected' outside the parens () refers to making the primary constructor only visible to extending
   * classes. The 'protected' inside the parens on the field (hence the 'val'), make the field only visible to extending
   * classes. All in the family, no outsiders.
   */
}

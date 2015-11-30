package exercises
import scala.reflect.BeanProperty

// Not doing the massive '.' omission this time. Going for best balance of concise (Scala generally good for this) and
// most readable (Scala generally not so good for this), in my humble and perhaps naïve opinion.
object Chapter5 extends App {
  // 1
  // copied and modified from page 52
  class Counter {
    private var value = 0
    def increment() { if (value < Int.MaxValue) value += 1 }
    def current() = value
  
    override def toString() = "Counter:" + value
  }
  val c = new Counter
  println(c)
  c.increment()
  println(c)
  c.increment()
  println(c)

  // 2 seems kind of silly that balance is read only though we provide two mutators? Assuming "read-only" means only
  // public getters and no setters, here's a possibility.
  class BankAccount {
    private var balance: BigDecimal = 0
    def deposit(amount: BigDecimal) { balance += amount }
    def withdraw(amount: BigDecimal) { balance -= amount }
  
    override def toString = "Balance: " + balance
  }
  val ba = new BankAccount
  println(ba)
  ba.withdraw(500)
  println(ba)
  ba.deposit(1000)
  println(ba)

  // 3
  {
    class Time(val hrs: Int, val min: Int) {
      def before(that: Time) = hrs < that.hrs || (hrs == that.hrs && min < that.min)
    }
    object Time {
      def apply(hrs: Int, min: Int) = new Time(hrs, min)
    }
    assert(Time(12, 20).before(Time(12, 25)))
    assert(Time(12, 20).before(Time(13, 25)))
    assert(!Time(14, 20).before(Time(13, 25)))
  }

  // 4
  // I think this keeps the public API the same
  {
    class Time(hrs: Int, min: Int) {
      private val time = hrs * 60 + min
      def before(that: Time) = time < that.time
    }
    object Time {
      def apply(hrs: Int, min: Int) = new Time(hrs, min)
    }
    assert(Time(12, 20).before(Time(12, 25)))
    assert(Time(12, 20).before(Time(13, 25)))
    assert(!Time(14, 20).before(Time(13, 25)))
  }

  // 5
  class Student(@BeanProperty var name: String, @BeanProperty var id: Long)
  // Can you? Yes
  // Should you? Who knows. The pair of getters or setters seem identical in function according to the text.
  val s = new Student("The Hulk", 9001)
  println(s.name + " " + s.id)
  println(s.getName + " " + s.getId)
  s.name = "Dennis the Menace"
  s.id = 155239
  println(s.name + " " + s.id)
  s.setName("No, The Hulk")
  s.setId(9002)
  println(s.name + " " + s.id)

  // 6
  // Errata: There is no 'class Person' in 5.1 . There are many versions of 'class Person' beyond that. So forget it.
  // We'll just roll our own.
  {
    class Person(val name: String, ageParam: Int) {
      val age = if (ageParam < 0) 0 else ageParam
    }
    val p = new Person("Adrian Person", -5)
    println(p.name + " " + p.age)
  }

  // 7
  {
    class Person(name: String) { // plain, so we can do validation on it, and it can't become a field
      // mad google-fu  http://stackoverflow.com/questions/2381666/multiple-assignment-of-non-tuples-in-scala
      val Array(firstName, lastName) = name.split(' ')
    }
    val p = new Person("Adrian Person") // throws exception if doesn't split exactly to two element Array, which I guess is good.
    println(p.lastName + ", " + p.firstName)
  }

  // 8
  {
    class Car(
      val manufacturer: String,
      val modelName: String,
      val modelYear: Int = -1,
      var licensePlate: String = "") {
      override def toString() = {
        "Manufacturer: " + manufacturer + " Model: " + modelName + " Year: " + modelYear + " Plate: " + licensePlate
      }
    }
    println(new Car("Ford", "Taurus"))
    println(new Car("Toyota", "Tundra", 2006))
    println(new Car("Fiat", "European", licensePlate="ABC-123"))
    // I really don't know much about cars
    val c = new Car("Mystery", "Machine", 1969, "ZOINKS")
    println(c)
    c.licensePlate = "ZOINKS!"
    println(c)
    // Awkward question. There is only 1 primary constructor? Anyways, all the reqs can be satisfied in the primary
    // constructor.
  }

  //9
  {
    // see accompanying Java definition. Sometimes Scala IDE freaks out on this mixed code :( Have to project clean
    println(new Chapter5_Java.Car("Ford", "Taurus"))
    println(new Chapter5_Java.Car("Toyota", "Tundra", 2006))
    println(new Chapter5_Java.Car("Fiat", "European", licensePlate="ABC-123"))
    val c = new Chapter5_Java.Car("Mystery", "Machine", 1969, "ZOINKS")
    println(c)
    c.licensePlate = "ZOINKS!"
    println(c)
  }

  // 10
  { // copied from text for reference
    class Employee(val name: String, var salary: Double) {
      def this() { this("John Q. Public", 0.0) }
    }
  }

  { // primary constructor + explicit fields version
    class Employee(defName: String = "John Q. Public", defSalary: Double = 0.0) {
      val name: String = defName
      var salary: Double = defSalary
    }
    // These constructors are implicating different interfaces. Specifically, that you can construct an Employee with
    // only one of either default. e.g. These are not allowed in the first form.
    new Employee("Frank Sinatra")
    new Employee(defSalary = 500.55)
    // If this is not desired, then the first/former might be preferred.
  }

}

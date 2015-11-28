package exercises

object Chapter2 extends App {
  // 1
  def signum(x: Int) = {
    if (x < 0) -1 else if (x == 0) 0 else 1
  }
  println(signum(-10), signum(0), signum(15))

  // 2
  // Type = Unit, value = () 'no useful value'

  // 3
  // This forces x to receive the Unit () value, which is the return of the assignment of y = 1
  val x = y = 1
      
  // 4
  for (i <- 10 to (0, -1)) println(i)

  // 5
  def countdown(n: Int) = {
    for (n <- n to (0, -1)) print(n)
  }
  countdown(5)
  println()

  // 6
  var prod = 1L
  for (i <- "Hello") { prod *= i }
  println(prod)

  // 7
  prod = "Hello".foldLeft(1L)(_ * _)
  println(prod)

  // 8
  def product(s: String) = s.foldLeft(1L)(_ * _)
  println(product("Hello"))

  // 9
  def productRec(s: String): Long = {
    if (s.length == 1) s.head else s.head * productRecursive(s.tail)
  }
  println(productRec("Hello"))

  // 10
  def intPow(x: Double, n: Int): Double = {
    if (n > 0 & n % 2 == 0) math.pow(intPow(x, n >> 1), 2)
    // n is odd...
    else if (n > 0) x * intPow(x, n - 1)
    else if (n == 0) 1
    else /*(n < 0)*/ 1 / intPow(x, -n) 
  }
  println(math.pow(2, 2), intPow(2, 2))
  println(math.pow(2, -2), intPow(2, -2))
}

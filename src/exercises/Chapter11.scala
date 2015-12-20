object Chapter11 extends App{
  // 1
  {
    println(3 + 4 -> 5)
    // Tuple2 (7, 5)

    // println(3 -> 4 + 5)
    // type mismatch
  }

  // 2
  {
    // In order to reflect normal usage, addition, subtraction, multiplication, and division operators are usually left-associative
    // while an exponentiation operator (if present) is right-associative (c) Wiki

    // in scala only operators which end with ":" or "=" are right-associative

    import java.math.BigInteger

    val x = new BigInt(new BigInteger("10")) {
      def **(p:Int) = pow(p)
    }

    // works as expected
    println(x ** 10)
    // 10000000000
    // doesn't
    println(x ** 9 + 1)
    // 1000000001

    // however char as a first symbol in operator name means the lowest precedence, that's why it works
    println (x pow 9 + 1)
    // 10000000000

    // thought would work with following name
    val z = new BigInt(new BigInteger("10")) {
      def =**(p:Int) = pow(p)
    }
    println (z =** 9 + 1)
    // 10000000000
  }

  // 3
  {
    class Fraction(n: Int, d: Int) {
      val num: Int = if (d == 0) 1 else n * sign(d) / gcd(n, d)
      val den: Int = if (d == 0) 0 else d * sign(d) / gcd(n, d)

      override def toString = num + "/" + den

      def sign(a: Int) = if (a > 0) 1 else if (a < 0) -1 else 0
      def gcd(a: Int, b: Int): Int = if (b == 0) math.abs(a) else gcd(b, a % b)

      def +(that: Fraction) = new Fraction(num * that.den + that.num * den, den * that.den)
      def -(that: Fraction) = new Fraction(num * that.den - that.num * den, den * that.den)

      def *(that: Fraction) = new Fraction(num * that.num, den * that.den)
      def /(that: Fraction) = new Fraction(num * that.den, den * that.num)

      override def equals(obj: scala.Any): Boolean = obj match {
        case Fraction(n, d) => n == num && d == den
        case _ => false
      }
    }

    object Fraction {
      def apply(n: Int, d: Int) = new Fraction(n, d)
      def unapply(input: Fraction) = Some((input.num, input.den))
    }

    // 1/2 + 1/4 == 3/4
    assert(Fraction(1, 2) + Fraction(1, 4) == Fraction(3, 4))
    // 1 - 1/3 == 2/3
    assert(Fraction(1, 1) - Fraction(1, 3) == Fraction(2, 3))
  }

  // 4
  {
    class Money(val cents: Int) extends Ordered[Money] {
      def this(d: Int, c: Int) {
        this(d * 100 + c)
      }

      def + (that: Money) = new Money (that.cents + cents)
      def - (that: Money) = new Money (cents - that.cents)

      def compare(that: Money): Int = cents compare that.cents
      override def equals(obj: scala.Any): Boolean = obj match {
        case that: Money => that.cents == cents
        case _ => false
      }
    }

    object Money {
      def apply(d: Int, c: Int) = new Money(d, c)
    }

    assert(Money(1, 75) + Money (0, 50) == Money(2, 25))
    assert(Money(1, 75) > Money (0, 50))
  }

  // 5
  {
    import scala.collection.mutable.ArrayBuffer

    trait HtmlEl {
      val tagName: String
      val innerHTML: String = ""

      override def toString: String = "<%s>%s</%s>".format(tagName, innerHTML, tagName)
    }

    trait HtmlContainer[T] extends HtmlEl {
      // TODO check that T is type extended from HtmlEl
      val children: ArrayBuffer[T]

      override def toString: String = "<%s>%s</%s>".format(tagName, children.map(_.toString).mkString, tagName)
    }

    class Table extends HtmlContainer[Row] {
      val children: ArrayBuffer[Row] = ArrayBuffer(new Row)
      val tagName: String = "table"

      def |(html: String) = { children.last.children += new Cell(html); this }
      def || (html: String) = { children += new Row(); |(html) }
    }

    class Row extends  HtmlContainer[Cell] {
      val children: ArrayBuffer[Cell] = new ArrayBuffer[Cell]
      val tagName: String = "tr"
    }

    class Cell(html: String) extends HtmlEl{
      val tagName: String = "td"
      override val innerHTML = html
    }

    object Table {
      def apply() = new Table()
    }

    val t = Table() | "Java" | "Scala" || "Gosling" | "Odersky" || "JVM" | "JVM,.NET"
    println(t)
  }
}

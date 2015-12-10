
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
}

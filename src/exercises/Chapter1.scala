package exercises

object Chapter1 extends App {
  // 1
  // Do it.

  // 2
  {
    println(math.pow(math.sqrt(3), 2))
    // 2.9999999999999996
  }

  // 3
  // val

  // 4
  {
    println("crazy" * 3)
    // crazycrazycrazy
  }

  // 5
  {
    println(10 max 2)
    // 10
  }

  // 6
  {
    println(BigInt(2) pow 1024)
    // 17976931348623159077293051907890247336179769789423065
    // 72734300811577326758055009631327084773224075360211201138798713933576587897688144
    // 16622492847430639474124377767893424865485276302219601246094119453082952085005768
    // 83815068234246288147391311054082723716335051068458629823994724593847971630483535
    // 6329624224137216
  }

  // 7
  {
    import BigInt.probablePrime, util.Random

    probablePrime(100, Random)
    // 1206450779766957706122532417051
  }

  // 8
  {
    import util.Random

    println(BigInt(100, Random) toString 36)
    // 1jcs6reaxvvbh449e765
  }

  // 9
  {
    println("astring" (0), "astring".last)
    // (a,g)
  }

  // 10
  {
    println("abcdefg" take 3, "abcdefg" drop 3, "abcdefg" takeRight 3, "abcdefg" dropRight 3)
    // (abc,defg,efg,abcd)
  }
}

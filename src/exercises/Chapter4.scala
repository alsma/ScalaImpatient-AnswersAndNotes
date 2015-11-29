package exercises

// Simply for the sake of pure torture, I've eliminated as many '.' as possible in this exercise. Just awful. Let's just
// see what the consequences are through this exercise.
object Chapter4 extends App {
  // 1
  val prices = Map("Mac Book pro" -> 2300, "Mac Book air" -> 1330)
  val withDiscounts = prices.mapValues(_ * 0.9)
  println(prices.toList)
  println(withDiscounts.toList)

  // 2
  {
    val in = new java.util.Scanner(new java.io.File("files/scanned-file.txt"))
    val occurrences = scala.collection.mutable.Map[String, Int]() withDefaultValue 0  // awesome
    while(in.hasNext) occurrences(in.next) += 1 // previous line's "awesome" makes this easy
    println(occurrences.toList)
  }

  // 3
  {
    val in = new java.util.Scanner(new java.io.File("files/scanned-file.txt"))
    var occurrences = Map[String, Int]() withDefaultValue 0
    while(in.hasNext) {
      val word = in.next
      occurrences = occurrences.updated(word, occurrences(word) + 1)
    }
    println(occurrences.toList)
  }

  // 4
  {
    val in = new java.util.Scanner(new java.io.File("files/scanned-file.txt"))
    var occurrences = Map[String, Int]() withDefaultValue 0
    while(in.hasNext) {
      val word = in.next
      occurrences = occurrences.updated(word, occurrences(word) + 1)
    }
    println(occurrences.toList.sortBy({_._2}).reverse)
  }

  // 5
  {
    val in = new java.util.Scanner(new java.io.File("files/scanned-file.txt"))
    var occurrences = new java.util.TreeMap[String, Int]()
    while (in.hasNext) {
      val word = in.next
      val current = Option(occurrences.get(word))
      occurrences.put(word, current.getOrElse(0) + 1)
    }

    println(occurrences)
  }

  // 6
  import java.util.Calendar._
  val stringsToCalConst = collection.mutable.LinkedHashMap(
    "Monday" -> MONDAY,
    "Tuesday" -> TUESDAY,
    "Wednesday" -> WEDNESDAY,
    "Thursday" -> THURSDAY,
    "Friday" -> FRIDAY,
    "Saturday" -> SATURDAY,
    "Sunday" -> SUNDAY)
  println(stringsToCalConst)

  // 7
  val props = collection.JavaConversions.propertiesAsScalaMap(System.getProperties)
  var longest = props.keys.maxBy(_.length).length
  for((key, v) <- props) println(key.padTo(longest, " ").mkString + " | " + v)

  // 8
  val arr = Array(-5, 0, 5, 9, -2, 17, 3)
  def minmax(values: Array[Int]) = values.min -> values.max
  println(minmax(arr))

  // 9
  def lteqgt(values: Array[Int], v: Int) = {
    val ltAndOther = values.partition(_ < v)
    val eqAndGt = ltAndOther._2.partition(_ == v)
    (ltAndOther._1.length, eqAndGt._1.length, eqAndGt._2.length)
  }
  println(lteqgt(arr, 0))

  // 10
  println("Hello".zip("World"))
  // Encryption?

  // Allowing so many '.' to be omitted can easily become detrimental as above. If it is to be, it is up to me... D.A.R.E
}

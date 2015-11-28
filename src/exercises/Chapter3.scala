package exercises
import scala.collection.mutable.ArrayBuffer

// All the toList inside the println's are just so that the automatic toString produces something informative.
object Chapter3 extends App {
  // 1
  def fill(n: Int) = Array(0 to n - 1: _*)
  val a = fill(10)
  println(a.toList)

  // 2
  def switch(a: Array[Int]) = {
    for (i <- a.indices; current = a(i); nextI = i + 1 if i != a.length - 1 && i % 2 == 0) {
      a(i) = a(nextI)
      a(nextI) = current
    }
  
    a
  }
  println(switch(a.clone).toList)
  // odd one test
  println(switch(Array.concat(a, Array(2))).toList)

  // 3
  def swapPairs(arr: Array[Int]) = {
    for (i <- a.indices; last = a.indices.last)
      yield if (i == last & i % 2 == 0) a(i)
        else if (i % 2 == 0) a(i + 1)
        else a(i - 1)
  }
  val q2 = swapPairs(a)
  println(q2.toList)
  // odd one test
  println(swapPairs(Array.concat(a, Array(3))).toList);

  // 4
  def separateNegative(a: Array[Int]) = {
    Array.concat(
      for (el <- a if el < 0) yield el,
      for (el <- a if el == 0) yield el,
      for (el <- a if el > 0) yield el
    )
  }
  println(separateNegative(a).toList)

  // 5
  val dbl = Array(10.2, 3.2, 4)
  def avg(a: Array[Double]) = a.sum / a.length
  println(avg(dbl))

  // 6
  println(a.sorted.reverse.toList) // Warn: This creates a copy 
  println(a.toBuffer.sortWith(_ > _)) // Warn: Book example does not compile. aBuf.sortBy(-_) also works

  // 7
  println(a.distinct.toList)

  // 8
  // Warn: Maybe an older version of scala, drop behaved differently? This question is nonsense to all of 2.9.1's
  // IndexedSeqOptimized.drop(Int) .dropRight .dropWhile  as I understand them.
  // Here's the example. Warn again!: Text does not compile, s.b. 'if elem' not 'if a'
//  println((for (elem <- a if elem % 2 == 0) yield 2 * elem).toList)
  // Here's a filter (to pick even numbers) and map-with-anonymous-function (to double them) solution.
  // _ * 2   is the shorthand for   (x: Int) => 2 * x   ?
  val c = new ArrayBuffer[Int]
  c ++= Array(1, 3, 5, -2, 3, -4, 2)
  (for (i <- c.indices if c(i) < 0) yield i).drop(1).foreach(i => c.remove(i))
  println(c.toList)

  // 9
  val timeZones = java.util.TimeZone.getAvailableIDs()
    .filter(_.startsWith("America/"))
    .map(_.stripPrefix("America/"))
    .sorted
  println(timeZones.toList)

  // 10
  import java.awt.datatransfer._
  val flavors = SystemFlavorMap.getDefaultFlavorMap().asInstanceOf[SystemFlavorMap]
  println(collection.JavaConversions.asBuffer(flavors.getNativesForFlavor(DataFlavor.imageFlavor)))
  // Found this other fat way, but probably noms up extra cycles for no reason (fat). Interestingly you get an explicit
  // ArrayBuffer instead of just a Buffer like the previous line.
//  println(flavors.getNativesForFlavor(DataFlavor.imageFlavor).toArray.toBuffer)
}

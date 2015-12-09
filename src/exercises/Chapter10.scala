import java.beans.{PropertyChangeEvent, PropertyChangeListener}

object Chapter10 extends App {
  // 1
  {
    import java.awt.geom.RectangularShape
    import java.awt.geom.Ellipse2D.{Double => Ellipse}

    val ellipse = new Ellipse(2, 3, 4, 5) with RectangleLike
    println(ellipse)
    ellipse.grow(2, 3)
    println(ellipse)
    ellipse.translate(2, 3)
    println(ellipse)

    trait RectangleLike {
      this: RectangularShape =>

      def translate(x: Double, y: Double) = setFrame(x + getX, y + getY, getWidth, getHeight)
      def grow(w: Double, h: Double) = setFrame(getX, getY, w + getWidth, h + getHeight)

      def setFrame(x: Double, y: Double, w: Double, h: Double)

      override def toString: String = {
        "X: %.2f Y: %.2f W: %.2f H: %.2f" format(getX, getY, getWidth, getHeight)
      }
    }

    // 2
    {
      import java.awt.Point

      class OrderedPoint(x: Int, y: Int) extends Point(x, y) with Ordered[Point] {
        def compare(that: Point): Int = {
          if (getX == that.getX && getY == that.getY) 0
          else if (getX > that.getX || getX == that.getX && getY > that.getY) 1
          else -1
        }
      }

      val a = new OrderedPoint(1, 5)
      val b = new OrderedPoint(2, 5)
      val c = new OrderedPoint(2, 6)
      val d = new OrderedPoint(2, 6)
      assert(a < b)
      assert(c > b)
      assert(c == d)
    }
  }

  // 3
  {
    /*
       lin(BitSet) =
       BitSet >> lin(AbstractSet) >> lin(SortedSet) >> lin (scala.collection.BitSet) >> lin(BitSetLike) >> lin (Serializable) =
       BitSet >> (AbstractSet >> Iterable >> GenSet >> GenericSetTemplate >> SetLike)
       ... >> ... and so on
     */
  }

  // 4
  {
    trait Logger {
      def log(msg: String)
    }

    class CryptoLogger(val key: Int = 3) extends Logger {
      private val alphaU = 'A' to 'Z'
      private val alphaL = 'a' to 'z'

      println('a'.toInt.toString)

      override def log(msg: String): Unit = {
        println(encode(msg, key))
      }

      def encode(text: String, key: Int) = text.map {
        case c if alphaU.contains(c) => rot(alphaU, c, key)
        case c if alphaL.contains(c) => rot(alphaL, c, key)
        case c => c
      }

      private def rot(a: IndexedSeq[Char], c: Char, key: Int) = a((c - a.head + key + a.size) % a.size)
    }

    val cryptoLogger = new CryptoLogger()
    cryptoLogger.log("Hello world!")
    val anotherLogger = new CryptoLogger(4)
    anotherLogger.log("Hello world!")
  }

  // 5
  {
    import java.beans.PropertyChangeSupport

    trait PropertyChangeSupportLike {
      val delegate = new PropertyChangeSupport(this)
    }

    implicit def propertyChangeSupportLike2PropertyChangeSupport(slt: PropertyChangeSupportLike): PropertyChangeSupport = slt.delegate

    class BeansPoint(x: Int, y: Int) extends java.awt.Point(x, y) with PropertyChangeSupportLike {
      def this() = this(0, 0)
    }

    val point  = new BeansPoint(2, 5)
    point.addPropertyChangeListener("x", new PropertyChangeListener {
      override def propertyChange(evt: PropertyChangeEvent): Unit = println("New property value X: %d" format evt.getNewValue)
    })

    point.x = 3
    point.firePropertyChange("x", 2, 3)
  }
}

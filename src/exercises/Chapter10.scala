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
    import java.beans.{PropertyChangeSupport, PropertyChangeEvent, PropertyChangeListener}

    trait PropertyChangeSupportLike {
      val delegate = new PropertyChangeSupport(this)
    }

    implicit def propertyChangeSupportLike2PropertyChangeSupport(slt: PropertyChangeSupportLike): PropertyChangeSupport = slt.delegate

    class BeansPoint(x: Int, y: Int) extends java.awt.Point(x, y) with PropertyChangeSupportLike {
      def this() = this(0, 0)
    }

    val point = new BeansPoint(2, 5)
    point.addPropertyChangeListener("x", new PropertyChangeListener {
      override def propertyChange(evt: PropertyChangeEvent): Unit = println("New property value X: %d" format evt.getNewValue)
    })

    point.x = 3
    point.firePropertyChange("x", 2, 3)
  }

  // 6
  {
    import java.awt.{Component, Container}

    trait JComponent extends Component
    trait JContainer extends Container with JComponent

    class JButton extends JComponent
    class JPanel extends JContainer

    assert((new JButton).isInstanceOf[Component])
    assert((new JButton).isInstanceOf[JComponent])
    assert(!(new JButton).isInstanceOf[JContainer])
    assert(!(new JButton).isInstanceOf[Container])

    assert((new JPanel).isInstanceOf[Component])
    assert((new JPanel).isInstanceOf[Container])
    assert((new JPanel).isInstanceOf[JComponent])
    assert((new JPanel).isInstanceOf[JContainer])
  }

  // 7
  {
    trait PaymentSystem {
      val provider: String
      val checkoutURL: String = "/checkout"

      println("PaymentSystem constructor")
    }

    trait Operation {
      val fee: Double
      println("Operation constructor")
    }

    trait Withdraw extends Operation {
      println("Withdraw constructor")
    }

    trait Deposit extends Operation {
      println("Deposit constructor")
    }

    trait ExpressCheckout extends PaymentSystem {
      override val checkoutURL: String = "/checkout/express"
      println("ExpressCheckout constructor")
    }

    class YandexMoney extends PaymentSystem with Withdraw with Deposit with ExpressCheckout {
      val provider: String = "advcash.yandex_money"
      val fee: Double = 0.03

      println("Checkout URL " + checkoutURL)
    }

    val ps = new YandexMoney
    // PaymentSystem constructor
    // Operation constructor
    // Withdraw constructor
    // Deposit constructor
    // ExpressCheckout constructor
    // Checkout URL /checkout/express
  }

  // 8 and 9
  {
    import java.io.{InputStream, ByteArrayInputStream}

    trait Logger { def log(msg: String) }

    trait PrintLogger extends Logger { def log(msg: String) = println(msg) }

    trait Buffering {
      this: InputStream with Logger =>

      val BUF_SIZE = 4
      private var prevRead = 0
      private var position = 0
      private val buffer = new Array[Byte](BUF_SIZE)

      override def read(): Int = {
        if (position >= prevRead) {
          prevRead = this.read(buffer, 0, BUF_SIZE)
          position = 0

          log("read: " + prevRead)
          if (prevRead == -1) return -1
        }

        position += 1
        buffer(position - 1)
      }
    }

    val is = new ByteArrayInputStream("123456789".getBytes) with Buffering with PrintLogger
    println(Stream.continually(is.read).takeWhile(_ != -1).map(_.toChar).toList)
    // read: 4
    // read: 4
    // read: 1
    // read: -1
    // List(1, 2, 3, 4, 5, 6, 7, 8, 9)
  }

  // 10
  {
    import java.io.{InputStream, ByteArrayInputStream}

    trait IterableInputStream extends InputStream with Iterable[Byte] {
      def iterator: Iterator[Byte] = new InputStreamIterator(this)

      class InputStreamIterator(is: IterableInputStream) extends Iterator[Byte] {
        def hasNext: Boolean = is.available() > 0

        def next(): Byte = is.read.toByte
      }
    }

    val is = new ByteArrayInputStream("123456789".getBytes) with IterableInputStream
    println({for (b <- is) yield b toChar} toList)
    // List(1, 2, 3, 4, 5, 6, 7, 8, 9)
  }
}

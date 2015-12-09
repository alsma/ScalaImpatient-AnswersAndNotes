
object Chapter10 extends App {
  // 1
  {
    import java.awt.geom.Ellipse2D.{Double => Ellipse}

    val ellipse = new Ellipse(2, 3, 4, 5) with RectangleLike
    println(ellipse)
    ellipse.grow(2, 3)
    println(ellipse)
    ellipse.translate(2, 3)
    println(ellipse)

    trait RectangleLike {
      def translate(x: Double, y: Double) = setFrame(x + getX, y + getY, getWidth, getHeight)
      def grow(w: Double, h: Double) = setFrame(getX, getY, w + getWidth, h + getHeight)

      def setFrame(x: Double, y: Double, w: Double, h: Double)
      def getX: Double
      def getY: Double
      def getWidth: Double
      def getHeight: Double

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
}

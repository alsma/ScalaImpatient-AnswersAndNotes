object Chapter10 extends App {
  {
    import java.awt.geom.Ellipse2D.{Double => Ellips}

    var test = new Ellips(2, 3, 4, 5) with RectangleLike
    println(test)
    test.grow(2, 3)
    println(test)
    test.translate(2, 3)
    println(test)

    trait RectangleLike {
      def translate(x: Double, y: Double) = setFrame(x + getX, y + getY, getWidth, getHeight)
      def grow(w: Double, h: Double) = setFrame(getX, getY, w + getWidth, h + getHeight)

      def setFrame(x: Double, y: Double, w: Double, h: Double)
      def getX: Double
      def getY: Double
      def getWidth: Double
      def getHeight: Double

      override def toString: String = {
        "X: " + getX.toString + " Y:" + getY.toString + " W: " + getWidth.toString + " H:" + getHeight.toString
      }
    }
  }
}

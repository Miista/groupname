import java.awt.Point
import java.io.FileNotFoundException
import java.util.Collections;

throws(FileNotFoundException::class)
public fun main(args: Array<String>) {
    val points = CPParser.readPoints(args[0])
    //        new ArrayList<>(  );
    //        points.add( new Point( 0, 0 ) );
    ////        points.add( new Point( 1, 2 ) );
    //        points.add( new Point( 2, -11 ) );
    //        points.add( new Point( 3, -10 ) );
    ////        points.add( new Point( 4, 90 ) );
    //        points.add( new Point( 5, 100 ) );
    val pointPointPair = ClosestPair( points.sortBy { it.x } )
    System.out.printf("Pair 1: (%d, %d)\n", pointPointPair.left?.x, pointPointPair.left?.y)
    System.out.printf("Pair 2: (%d, %d)\n", pointPointPair.right?.x, pointPointPair.right?.y)
}

public tailRecursive fun ClosestPair(points: List<Point>): CP.EuclideanPair {
    return when (points.size()) {
        3  -> getClosestPairOf3(points)
        2 -> CP.EuclideanPair(points.get(0), points.get(1))
        else -> {
            val median = points.size() / 2
            val left = points.subList(0, median)
            val right = points.subList(median, points.size())
            val closestLeftPair = ClosestPair(left)
            val closestRightPair = ClosestPair(right)

            val delta = Math.min(closestLeftPair.distance, closestRightPair.distance)

            val lowerBound = (median - delta).toInt()
            val upperBound = (median + delta).toInt()

            val list = points.toArrayList()
            val iterator = list.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (next.x < lowerBound || next.x > upperBound) {
                    iterator.remove()
                }
            }

            return ClosestPair( list.sortBy { it.y } )
        }
    }
}

private fun getClosestPairOf3(points: List<Point>): CP.EuclideanPair {
    val A = points.get(0)
    val B = points.get(1)
    val C = points.get(2)
    val ab = A.distance(B)
    val bc = B.distance(C)
    val ca = C.distance(A)
    var left: Point
    var right: Point
    if (ab < bc) {
        left = A
        right = B
    } else {
        left = B
        right = C
    }
    if (ca < ab) {
        left = A
        right = C
    }

    return CP.EuclideanPair(left, right)
}
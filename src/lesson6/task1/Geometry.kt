@file:Suppress("UNUSED_PARAMETER")
package lesson6.task1

import lesson1.task1.sqr

/**
 * Точка на плоскости
 */
data class Point(val x: Double, val y: Double) {
    /**
     * Пример
     *
     * Рассчитать (по известной формуле) расстояние между двумя точками
     */
    fun distance(other: Point): Double = Math.sqrt(sqr(x - other.x) + sqr(y - other.y))

    // Дальше идут методы Ktlo

    fun sqrDistance(other: Point): Double {
        val deltaX = x - other.x
        val delta_y = y - other.y
        return deltaX*deltaX + delta_y*delta_y
    }

    operator fun plus(other: Point) = Point(x + other.x, y + other.y)

    operator fun minus(other: Point) = Point(x - other.x, y - other.y)

    operator fun times(other: Point) = x * other.x + y * other.y

    operator fun div(scalar: Double) = Point(x / scalar, y / scalar)

    val length get() = Math.hypot(x, y)
}

/**
 * Треугольник, заданный тремя точками (a, b, c, см. constructor ниже).
 * Эти три точки хранятся в множестве points, их порядок не имеет значения.
 */
class Triangle private constructor(private val points: Set<Point>) {

    private val pointList = points.toList()

    val a: Point get() = pointList[0]

    val b: Point get() = pointList[1]

    val c: Point get() = pointList[2]

    constructor(a: Point, b: Point, c: Point): this(linkedSetOf(a, b, c))
    /**
     * Пример: полупериметр
     */
    fun halfPerimeter() = (a.distance(b) + b.distance(c) + c.distance(a)) / 2.0

    /**
     * Пример: площадь
     */
    fun area(): Double {
        val p = halfPerimeter()
        return Math.sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
    }

    /**
     * Пример: треугольник содержит точку
     */
    fun contains(p: Point): Boolean {
        val abp = Triangle(a, b, p)
        val bcp = Triangle(b, c, p)
        val cap = Triangle(c, a, p)
        return abp.area() + bcp.area() + cap.area() <= area()
    }

    override fun equals(other: Any?) = other is Triangle && points == other.points

    override fun hashCode() = points.hashCode()

    override fun toString() = "Triangle(a = $a, b = $b, c = $c)"
}

/**
 * Окружность с заданным центром и радиусом
 */
data class Circle(val center: Point, val radius: Double) {
    /**
     * Простая
     *
     * Рассчитать расстояние между двумя окружностями.
     * Расстояние между непересекающимися окружностями рассчитывается как
     * расстояние между их центрами минус сумма их радиусов.
     * Расстояние между пересекающимися окружностями считать равным 0.0.
     */
    fun distance(other: Circle): Double {
        val result = center.distance(other.center) - radius - other.radius
        return if (result < .0) .0 else result
    }

    /**
     * Тривиальная
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     */
    fun contains(p: Point): Boolean = center.sqrDistance(p) <= radius * radius
}

/**
 * Отрезок между двумя точками
 */
data class Segment(val begin: Point, val end: Point) {
    override fun equals(other: Any?) =
            other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
            begin.hashCode() + end.hashCode()
}

/**
 * Средняя
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {
    require(points.size >= 2)
    var X = .0
    var Y = .0
    for ((x, y) in points) {
        X += x
        Y += y
    }
    val center = Point(X / points.size, Y / points.size)
    var a = center
    var dist = 0.0
    for (point in points) {
        val curr_dist = center.sqrDistance(point)
        if (dist < curr_dist) {
            a = point
            dist = curr_dist
        }
    }
    dist = .0
    var b = a
    for (point in points) {
        val curr_dist = a.sqrDistance(point)
        if (dist < curr_dist) {
            b = point
            dist = curr_dist
        }
    }
    return Segment(a, b)
}

/**
 * Простая
 *
 * Построить окружность по её диаметру, заданному двумя точками
 * Центр её должен находиться посередине между точками, а радиус составлять половину расстояния между ними
 */
fun circleByDiameter(diameter: Segment): Circle =
        Circle((diameter.begin + diameter.end) / 2.0, diameter.begin.distance(diameter.end) / 2)

/**
 * Прямая, заданная точкой point и углом наклона angle (в радианах) по отношению к оси X.
 * Уравнение прямой: (y - point.y) * cos(angle) = (x - point.x) * sin(angle)
 * или: y * cos(angle) = x * sin(angle) + b, где b = point.y * cos(angle) - point.x * sin(angle).
 * Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
 */
class Line private constructor(val b: Double, val angle: Double) {
    init {
        assert(angle >= 0 && angle < Math.PI) { "Incorrect line angle: $angle" }
    }

    constructor(point: Point, angle: Double): this(point.y * Math.cos(angle) - point.x * Math.sin(angle), angle)

    /**
     * Средняя
     *
     * Найти точку пересечения с другой линией.
     * Для этого необходимо составить и решить систему из двух уравнений (каждое для своей прямой)
     */
    fun crossPoint(other: Line): Point {
        val a1_sin = Math.sin(angle)
        val a2_sin = Math.sin(other.angle)
        val a1_cos = Math.cos(angle)
        val a2_cos = Math.cos(other.angle)

        val x = (b / a1_cos - other.b / a2_cos) / (a2_sin / a2_cos - a1_sin / a1_cos)

        return Point(x, (x * a2_sin + other.b)/a2_cos)
    }

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${Math.cos(angle)} * y = ${Math.sin(angle)} * x + $b)"
}

fun normalizeAngle(angle: Double): Double = when {
    angle < 0 -> angle + Math.PI
    angle >= Math.PI -> angle - Math.PI
    else -> angle
}

/**
 * Средняя
 *
 * Построить прямую по отрезку
 */
fun lineBySegment(s: Segment): Line = Line(s.begin, normalizeAngle(Math.atan((s.begin.y - s.end.y)/(s.begin.x - s.end.x))))

/**
 * Средняя
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line = lineBySegment(Segment(a, b))

/**
 * Сложная
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line {
    val delta = a - b
    val center = (a + b) / 2.0
    val angle = normalizeAngle(Math.acos(delta.x/delta.length) + Math.PI / 2)
    return Line(center, angle)
}

/**
 * Средняя
 *
 * Задан список из n окружностей на плоскости. Найти пару наименее удалённых из них.
 * Если в списке менее двух окружностей, бросить IllegalArgumentException
 */
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> {
    require(circles.size >= 2)
    var a = circles[0]
    var b = circles[1]
    var dist = Double.MAX_VALUE
    for (circle1 in circles) {
        for (circle2 in circles) {
            if (circle1 === circle2) continue
            val curr_dist = circle1.distance(circle2)
            if (curr_dist < dist) {
                a = circle1
                b = circle2
                dist = curr_dist
            }
        }
    }
    return a to b
}

/**
 * Сложная
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle {
    // My oldest fear...
    val x = ( ((a.x*a.x-b.x*b.x) + (a.y*a.y-b.y*b.y))/(a.y-b.y) - ((b.x*b.x-c.x*c.x) + (b.y*b.y-c.y*c.y))/(b.y-c.y) ) / (2*((c.x-b.x)/(b.y-c.y) - (b.x-a.x)/(a.y-b.y)))
    val y = ( 2*x*(b.x - a.x) + (a.x*a.x - b.x*b.x) + (a.y*a.y - b.y*b.y) )/(2*(a.y - b.y))
    val r = Math.hypot(x - a.x, y - a.y)
    return Circle(Point(x, y), r)
}

/**
 * Очень сложная
 *
 * Дано множество точек на плоскости. Найти круг минимального радиуса,
 * содержащий все эти точки. Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит одну точку, вернуть круг нулевого радиуса с центром в данной точке.
 *
 * Примечание: в зависимости от ситуации, такая окружность может либо проходить через какие-либо
 * три точки данного множества, либо иметь своим диаметром отрезок,
 * соединяющий две самые удалённые точки в данном множестве.
 */
fun minContainingCircle(vararg points: Point): Circle {
    require(points.isNotEmpty())
    when (points.size) {
        1 -> return Circle(points[0], .0)
        2 -> return Circle((points[0] - points[1]) / 2.0, points[0].distance(points[1]))
    }

    // Находим центральную точку между точками (это не центр окружности)
    var X = .0
    var Y = .0
    for ((x, y) in points) {
        X += x
        Y += y
    }
    val center = Point(X / points.size, Y / points.size)

    // Находим три самые далёкие точки от центральной
    val P = Array(3) { center }
    val dists = Array(3) { .0 }
    for (i in 0..2) {
        for (point in points) {
            val curr_dist = center.sqrDistance(point)
            if (point !in P && dists[i] < curr_dist) {
                dists[i] = curr_dist
                P[i] = point
            }
        }
    }

    return circleByThreePoints(a = P[0], b = P[1], c = P[2])
}


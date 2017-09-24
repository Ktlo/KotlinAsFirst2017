@file:Suppress("UNUSED_PARAMETER")
package lesson6.task2

import java.util.TreeSet

/**
 * Клетка шахматной доски. Шахматная доска квадратная и имеет 8 х 8 клеток.
 * Поэтому, обе координаты клетки (горизонталь row, вертикаль column) могут находиться в пределах от 1 до 8.
 * Горизонтали нумеруются снизу вверх, вертикали слева направо.
 */
data class Square(val column: Int, val row: Int) {
    /**
     * Пример
     *
     * Возвращает true, если клетка находится в пределах доски
     */
    fun inside(): Boolean = column in 1..8 && row in 1..8

    /**
     * Простая
     *
     * Возвращает строковую нотацию для клетки.
     * В нотации, колонки обозначаются латинскими буквами от a до h, а ряды -- цифрами от 1 до 8.
     * Для клетки не в пределах доски вернуть пустую строку
     */
    fun notation(): String = if (inside()) "${(column + ('a' - 1).toInt()).toChar()}$row" else ""
}

/**
 * Простая
 *
 * Создаёт клетку по строковой нотации.
 * В нотации, колонки обозначаются латинскими буквами от a до h, а ряды -- цифрами от 1 до 8.
 * Если нотация некорректна, бросить IllegalArgumentException
 */
fun square(notation: String): Square {
    require(notation.length == 2 && notation[0] in 'a'..'h' && notation[1] in '1'..'8')
    return Square(notation[0] - 'a' + 1, notation[1] - '0')
}

/**
 * Простая
 *
 * Определить число ходов, за которое шахматная ладья пройдёт из клетки start в клетку end.
 * Шахматная ладья может за один ход переместиться на любую другую клетку
 * по вертикали или горизонтали.
 * Ниже точками выделены возможные ходы ладьи, а крестиками -- невозможные:
 *
 * xx.xxххх
 * xх.хxххх
 * ..Л.....
 * xх.хxххх
 * xx.xxххх
 * xx.xxххх
 * xx.xxххх
 * xx.xxххх
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: rookMoveNumber(Square(3, 1), Square(6, 3)) = 2
 * Ладья может пройти через клетку (3, 3) или через клетку (6, 1) к клетке (6, 3).
 */
fun rookMoveNumber(start: Square, end: Square): Int {
    require(start.inside() && end.inside())

    return when {
        start == end -> 0
        start.column == end.column || start.row == end.row -> 1
        else -> 2
    }
}

/**
 * Средняя
 *
 * Вернуть список из клеток, по которым шахматная ладья может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов ладьи см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры: rookTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          (здесь возможен ещё один вариант)
 *          rookTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(3, 3), Square(6, 3))
 *          (здесь возможен единственный вариант)
 *          rookTrajectory(Square(3, 5), Square(8, 5)) = listOf(Square(3, 5), Square(8, 5))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun rookTrajectory(start: Square, end: Square): List<Square> = when(rookMoveNumber(start, end)) {
    0 -> listOf(start)
    1 -> listOf(start, end)
    else -> listOf(start, Square(start.column, end.row), end)
}

/**
 * Простая
 *
 * Определить число ходов, за которое шахматный слон пройдёт из клетки start в клетку end.
 * Шахматный слон может за один ход переместиться на любую другую клетку по диагонали.
 * Ниже точками выделены возможные ходы слона, а крестиками -- невозможные:
 *
 * .xxx.ххх
 * x.x.xххх
 * xxСxxxxx
 * x.x.xххх
 * .xxx.ххх
 * xxxxx.хх
 * xxxxxх.х
 * xxxxxхх.
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если клетка end недостижима для слона, вернуть -1.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Примеры: bishopMoveNumber(Square(3, 1), Square(6, 3)) = -1; bishopMoveNumber(Square(3, 1), Square(3, 7)) = 2.
 * Слон может пройти через клетку (6, 4) к клетке (3, 7).
 */
fun bishopMoveNumber(start: Square, end: Square): Int {
    require(start.inside() && end.inside())

    return when {
        start == end -> 0
        (start.column - end.column) and 1 != (start.row - end.row) and 1 -> -1
        Math.abs(start.column - end.column) == Math.abs(start.row - end.row) -> 1
        else -> 2
    }
}

/**
 * Сложная
 *
 * Вернуть список из клеток, по которым шахматный слон может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов слона см. предыдущую задачу.
 *
 * Если клетка end недостижима для слона, вернуть пустой список.
 *
 * Если клетка достижима:
 * - список всегда включает в себя клетку start
 * - клетка end включается, если она не совпадает со start.
 * - между ними должны находиться промежуточные клетки, по порядку от start до end.
 *
 * Примеры: bishopTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          bishopTrajectory(Square(3, 1), Square(3, 7)) = listOf(Square(3, 1), Square(6, 4), Square(3, 7))
 *          bishopTrajectory(Square(1, 3), Square(6, 8)) = listOf(Square(1, 3), Square(6, 8))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun bishopTrajectory(start: Square, end: Square): List<Square> = when (bishopMoveNumber(start, end)) {
    -1 -> listOf()
    0 -> listOf(start)
    1 -> listOf(start, end)
    else -> {
        val firstWay = Square(Math.abs((end.column + start.column + end.row - start.row) / 2),
                Math.abs((end.row + start.row + end.column - start.column) / 2))
        if (firstWay.inside())
            listOf(start, firstWay, end)
        else
            listOf(start, Square(Math.abs((end.column - start.column - end.row + start.row) / 2),
                    Math.abs((end.row + start.row - end.column + start.column) / 2)), end)
    }
}

/**
 * Средняя
 *
 * Определить число ходов, за которое шахматный король пройдёт из клетки start в клетку end.
 * Шахматный король одним ходом может переместиться из клетки, в которой стоит,
 * на любую соседнюю по вертикали, горизонтали или диагонали.
 * Ниже точками выделены возможные ходы короля, а крестиками -- невозможные:
 *
 * xxxxx
 * x...x
 * x.K.x
 * x...x
 * xxxxx
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: kingMoveNumber(Square(3, 1), Square(6, 3)) = 3.
 * Король может последовательно пройти через клетки (4, 2) и (5, 2) к клетке (6, 3).
 */
fun kingMoveNumber(start: Square, end: Square): Int {
    require(start.inside() && end.inside())

    return Math.max(Math.abs(start.column - end.column), Math.abs(start.row - end.row))
}

/**
 * Сложная
 *
 * Вернуть список из клеток, по которым шахматный король может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов короля см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры: kingTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          (здесь возможны другие варианты)
 *          kingTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(4, 2), Square(5, 2), Square(6, 3))
 *          (здесь возможен единственный вариант)
 *          kingTrajectory(Square(3, 5), Square(6, 2)) = listOf(Square(3, 5), Square(4, 4), Square(5, 3), Square(6, 2))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun kingTrajectory(start: Square, end: Square): List<Square> {
    require(start.inside() && end.inside())

    val delta_column = end.column - start.column
    val delta_row = Math.abs(end.row - start.row)

    val sign_column = delta_column > 0
    val sign_row = delta_row > 0

    val abs_delta_column = Math.abs(delta_column)
    val abs_delta_row = Math.abs(delta_row)

    val steps = Math.max(abs_delta_column, abs_delta_row)

    var diagonal = Math.min(abs_delta_column, abs_delta_row)
    var forward = steps - diagonal

    var (column, row) = start
    val result = mutableListOf<Square>()

    result.add(start)
    while (diagonal > 0) {
        column += if (sign_column) 1 else -1
        row += if (sign_row) 1 else -1
        result.add(Square(column, row))
        diagonal--
    }
    if (end.column == column)
        while (forward > 0) {
            row += if (sign_row) 1 else -1
            result.add(Square(column, row))
            forward--
        }
    else
        while (forward > 0) {
            column += if (sign_column) 1 else -1
            result.add(Square(column, row))
            forward--
        }
    println(result)
    return result
}

private val knight_ways = arrayOf(
        arrayOf(0,3,2,3,2,3,4,5),
        arrayOf(3,4,1,2,3,4,3,4),
        arrayOf(2,1,4,3,2,3,4,5),
        arrayOf(3,2,3,2,3,4,3,4),
        arrayOf(2,3,2,3,4,3,4,5),
        arrayOf(3,4,3,4,3,4,5,4),
        arrayOf(4,3,4,3,4,5,4,5),
        arrayOf(5,4,5,4,5,4,5,6)
)

fun atBorders(s: Square): Boolean {
    val (column, row) = s
    return column == 1 && row == 1 || column == 1 && row == 8 || column == 8 && row == 1 || column == 8 && row == 8
}

/**
 * Сложная
 *
 * Определить число ходов, за которое шахматный конь пройдёт из клетки start в клетку end.
 * Шахматный конь одним ходом вначале передвигается ровно на 2 клетки по горизонтали или вертикали,
 * а затем ещё на 1 клетку под прямым углом, образуя букву "Г".
 * Ниже точками выделены возможные ходы коня, а крестиками -- невозможные:
 *
 * .xxx.xxx
 * xxKxxxxx
 * .xxx.xxx
 * x.x.xxxx
 * xxxxxxxx
 * xxxxxxxx
 * xxxxxxxx
 * xxxxxxxx
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: knightMoveNumber(Square(3, 1), Square(6, 3)) = 3.
 * Конь может последовательно пройти через клетки (5, 2) и (4, 4) к клетке (6, 3).
 */
fun knightMoveNumber(start: Square, end: Square): Int {
    require(start.inside() && end.inside())

    val delta_column = Math.abs(end.column - start.column)
    val delta_row = Math.abs(end.row - start.row)

    return if (delta_column == 1 && delta_row == 1) {
        if (atBorders(start) || atBorders(end)) 4
        else 2
    }
    else knight_ways[delta_column][delta_row]
}

private class PreviousStep(val previous: PreviousStep?, val square: Square)

private class KnightDoneException : Exception()

/**
 * Очень сложная
 *
 * Вернуть список из клеток, по которым шахматный конь может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов коня см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры:
 *
 * knightTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 * здесь возможны другие варианты)
 * knightTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(5, 2), Square(4, 4), Square(6, 3))
 * (здесь возможен единственный вариант)
 * knightTrajectory(Square(3, 5), Square(5, 6)) = listOf(Square(3, 5), Square(5, 6))
 * (здесь опять возможны другие варианты)
 * knightTrajectory(Square(7, 7), Square(8, 8)) =
 *     listOf(Square(7, 7), Square(5, 8), Square(4, 6), Square(6, 7), Square(8, 8))
 *
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun knightTrajectory(start: Square, end: Square): List<Square> {
    val result = mutableListOf<Square>()

    fun nextStep(left: Int, previous: PreviousStep) {
        if (!previous.square.inside())
            return
        if (left == 0) {
            if (previous.square == end) {
                var iter: PreviousStep? = previous
                while (iter != null) {
                    result.add(iter.square)
                    iter = iter.previous
                }
                throw KnightDoneException()
            }
            else
                return
        }
        val (column, row) = previous.square
        nextStep(left - 1, PreviousStep(previous, Square(column + 2, row + 1)))
        nextStep(left - 1, PreviousStep(previous, Square(column + 2, row - 1)))
        nextStep(left - 1, PreviousStep(previous, Square(column - 2, row + 1)))
        nextStep(left - 1, PreviousStep(previous, Square(column - 2, row - 1)))
        nextStep(left - 1, PreviousStep(previous, Square(column + 1, row + 2)))
        nextStep(left - 1, PreviousStep(previous, Square(column + 1, row - 2)))
        nextStep(left - 1, PreviousStep(previous, Square(column - 1, row + 2)))
        nextStep(left - 1, PreviousStep(previous, Square(column - 1, row - 2)))
    }

    try {
        nextStep(knightMoveNumber(start, end), PreviousStep(null, start))
    }
    catch (e: KnightDoneException) {
        return result.reversed()
    }
    return emptyList()
}

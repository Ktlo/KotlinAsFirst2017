@file:Suppress("UNUSED_PARAMETER")
package lesson7.task2

import lesson7.task1.Matrix
import lesson7.task1.MatrixImpl
import lesson7.task1.createMatrix

// Все задачи в этом файле требуют наличия реализации интерфейса "Матрица" в Matrix.kt

/**
 * Пример
 *
 * Транспонировать заданную матрицу matrix.
 * При транспонировании строки матрицы становятся столбцами и наоборот:
 *
 * 1 2 3      1 4 6 3
 * 4 5 6  ==> 2 5 5 2
 * 6 5 4      3 6 4 1
 * 3 2 1
 */
fun <E> transpose(matrix: Matrix<E>): Matrix<E> {
    if (matrix.width < 1 || matrix.height < 1) return matrix
    val result = createMatrix(height = matrix.width, width = matrix.height, e = matrix[0, 0])
    for (i in 0 until matrix.width) {
        for (j in 0 until matrix.height) {
            result[i, j] = matrix[j, i]
        }
    }
    return result
}

/**
 * Пример
 *
 * Сложить две заданные матрицы друг с другом.
 * Складывать можно только матрицы совпадающего размера -- в противном случае бросить IllegalArgumentException.
 * При сложении попарно складываются соответствующие элементы матриц
 */
operator fun Matrix<Int>.plus(other: Matrix<Int>): Matrix<Int> {
    if (width != other.width || height != other.height) throw IllegalArgumentException()
    if (width < 1 || height < 1) return this
    val result = createMatrix(height, width, this[0, 0])
    for (i in 0 until height) {
        for (j in 0 until width) {
            result[i, j] = this[i, j] + other[i, j]
        }
    }
    return result
}

/**
 * Сложная
 *
 * Заполнить матрицу заданной высоты height и ширины width
 * натуральными числами от 1 до m*n по спирали,
 * начинающейся в левом верхнем углу и закрученной по часовой стрелке.
 *
 * Пример для height = 3, width = 4:
 *  1  2  3  4
 * 10 11 12  5
 *  9  8  7  6
 */
fun generateSpiral(height: Int, width: Int): Matrix<Int> {
    var h = height
    var w = width

    val matrix = createMatrix(height, width, -1)
    var iter = 0
    val last = height * width
    var offset_i = 0
    var offset_j = -1
    matrix[0, 0] = 1
    while (iter < last) {
        for (j in offset_j + 1 until w) {
            matrix[offset_i, j] = ++iter
        }
        if (iter >= last) break
        offset_j++
        val offset_w = width - offset_j - 1
        for (i in offset_i + 1 until h) {
            matrix[i, offset_w] = ++iter
        }
        if (iter >= last) break
        val offset_h = height - offset_i - 1
        for (j in (offset_j until w - 1).reversed()) {
            matrix[offset_h, j] = ++iter
        }
        if (iter >= last) break
        for (i in (offset_i + 1 until h - 1).reversed()) {
            matrix[i, offset_j] = ++iter
        }
        offset_i++
        w--
        h--
    }
    return matrix
}

/**
 * Сложная
 *
 * Заполнить матрицу заданной высоты height и ширины width следующим образом.
 * Элементам, находящимся на периферии (по периметру матрицы), присвоить значение 1;
 * периметру оставшейся подматрицы – значение 2 и так далее до заполнения всей матрицы.
 *
 * Пример для height = 5, width = 6:
 *  1  1  1  1  1  1
 *  1  2  2  2  2  1
 *  1  2  3  3  2  1
 *  1  2  2  2  2  1
 *  1  1  1  1  1  1
 */
fun generateRectangles(height: Int, width: Int): Matrix<Int> {
    val half_h = height / 2
    val half_w = width / 2
    return MatrixImpl(height, width) {
        i, j -> Math.min((if (i >= half_h) height - i - 1 else i), (if (j >= half_w) width - j - 1 else j)) + 1
    }
}

/**
 * Сложная
 *
 * Заполнить матрицу заданной высоты height и ширины width диагональной змейкой:
 * в левый верхний угол 1, во вторую от угла диагональ 2 и 3 сверху вниз, в третью 4-6 сверху вниз и так далее.
 *
 * Пример для height = 5, width = 4:
 *  1  2  4  7
 *  3  5  8 11
 *  6  9 12 15
 * 10 13 16 18
 * 14 17 19 20
 */
fun generateSnake(height: Int, width: Int): Matrix<Int> {
    var iter = 0
    val matrix = createMatrix(height, width, -1)
    for (i in 0 until width) {
        for (j in 0 until Math.min(i + 1, height)) {
            matrix[j, i - j] = ++iter
        }
    }
    for (i in 1 until height) {
        for (j in 0 until Math.min(height - i, width)) {
            matrix[i + j, width - j - 1] = ++iter
        }
    }
    return matrix
}

/**
 * Средняя
 *
 * Содержимое квадратной матрицы matrix (с произвольным содержимым) повернуть на 90 градусов по часовой стрелке.
 * Если height != width, бросить IllegalArgumentException.
 *
 * Пример:    Станет:
 * 1 2 3      7 4 1
 * 4 5 6      8 5 2
 * 7 8 9      9 6 3
 */
fun <E> rotate(matrix: Matrix<E>): Matrix<E> {
    require(matrix.height == matrix.width)
    val size = matrix.height
    return MatrixImpl(matrix.height, matrix.width) {
        i, j ->
        matrix[size - j - 1, i]
    }
}

/**
 * Сложная
 *
 * Проверить, является ли квадратная целочисленная матрица matrix латинским квадратом.
 * Латинским квадратом называется матрица размером n x n,
 * каждая строка и каждый столбец которой содержат все числа от 1 до n.
 * Если height != width, вернуть false.
 *
 * Пример латинского квадрата 3х3:
 * 2 3 1
 * 1 2 3
 * 3 1 2
 */
fun isLatinSquare(matrix: Matrix<Int>): Boolean {
    require(matrix.width == matrix.height)
    try {
        val size = matrix.width
        for (i in 0 until size) {
            val numbers = BooleanArray(size) { false }
            for (j in 0 until size) {
                val ind = matrix[i, j] - 1
                if (numbers[ind])
                    return false
                else
                    numbers[ind] = true
            }
        }
        for (i in 0 until size) {
            val numbers = BooleanArray(size) { false }
            for (j in 0 until size) {
                val ind = matrix[j, i] - 1
                if (numbers[ind])
                    return false
                else
                    numbers[ind] = true
            }
        }
        return true
    }
    catch (e: IndexOutOfBoundsException) {
        return false
    }
}

/**
 * Средняя
 *
 * В матрице matrix каждый элемент заменить суммой непосредственно примыкающих к нему
 * элементов по вертикали, горизонтали и диагоналям.
 *
 * Пример для матрицы 4 x 3: (11=2+4+5, 19=1+3+4+5+6, ...)
 * 1 2 3       11 19 13
 * 4 5 6  ===> 19 31 19
 * 6 5 4       19 31 19
 * 3 2 1       13 19 11
 *
 * Поскольку в матрице 1 х 1 примыкающие элементы отсутствуют,
 * для неё следует вернуть как результат нулевую матрицу:
 *
 * 42 ===> 0
 */
fun sumNeighbours(matrix: Matrix<Int>): Matrix<Int> {
    fun get(i: Int, j: Int) =
            if (i < 0 || j < 0 || i >= matrix.height || j >= matrix.width) 0
            else matrix[i, j]

    return MatrixImpl(matrix.height, matrix.width) {
        i, j ->
        get(i - 1, j - 1) + get(i - 1, j) + get(i - 1, j + 1) +
        get(i, j - 1) + get(i, j + 1) +
        get(i + 1, j - 1) + get(i + 1, j) + get(i + 1, j + 1)
    }
}

/**
 * Средняя
 *
 * Целочисленная матрица matrix состоит из "дырок" (на их месте стоит 0) и "кирпичей" (на их месте стоит 1).
 * Найти в этой матрице все ряды и колонки, целиком состоящие из "дырок".
 * Результат вернуть в виде Holes(rows = список дырчатых рядов, columns = список дырчатых колонок).
 * Ряды и колонки нумеруются с нуля. Любой из спискоов rows / columns может оказаться пустым.
 *
 * Пример для матрицы 5 х 4:
 * 1 0 1 0
 * 0 0 1 0
 * 1 0 0 0 ==> результат: Holes(rows = listOf(4), columns = listOf(1, 3)): 4-й ряд, 1-я и 3-я колонки
 * 0 0 1 0
 * 0 0 0 0
 */
fun findHoles(matrix: Matrix<Int>): Holes {
    val rows = mutableListOf<Int>()
    val columns = mutableListOf<Int>()

    for (i in 0 until matrix.height) {
        var row = true
        for (j in 0 until matrix.width) {
            if (matrix[i, j] == 1) {
                row = false
            }
        }
        if (row) {
            rows.add(i)
        }
    }
    for (j in 0 until matrix.width) {
        var column = true
        for (i in 0 until matrix.height) {
            if (matrix[i, j] == 1) {
                column = false
            }
        }
        if (column) {
            columns.add(j)
        }
    }

    return Holes(rows, columns)
}

/**
 * Класс для описания местонахождения "дырок" в матрице
 */
data class Holes(val rows: List<Int>, val columns: List<Int>)

/**
 * Средняя
 *
 * В целочисленной матрице matrix каждый элемент заменить суммой элементов подматрицы,
 * расположенной в левом верхнем углу матрицы matrix и ограниченной справа-снизу данным элементом.
 *
 * Пример для матрицы 3 х 3:
 *
 * 1  2  3      1  3  6
 * 4  5  6  =>  5 12 21
 * 7  8  9     12 27 45
 *
 * К примеру, центральный элемент 12 = 1 + 2 + 4 + 5, элемент в левом нижнем углу 12 = 1 + 4 + 7 и так далее.
 */
fun sumSubMatrix(matrix: Matrix<Int>): Matrix<Int> {
    val result = createMatrix(matrix.height, matrix.width, -1)
    fun sumBranch(i: Int, j: Int, sum: Int) {
        result[i, j] = sum

        if (j < matrix.width - 1) {
            var next_sum = sum
            for (k in 0..i)
                next_sum += matrix[k, j + 1]
            sumBranch(i, j + 1, next_sum)
        }

        if (i < matrix.height - 1) {
            var next_sum = sum
            for (k in 0..j)
                next_sum += matrix[i + 1, k]
            sumBranch(i + 1, j, next_sum)
        }
    }

    sumBranch(0, 0, matrix[0, 0])
    return result
}

/**
 * Сложная
 *
 * Даны мозаичные изображения замочной скважины и ключа. Пройдет ли ключ в скважину?
 * То есть даны две матрицы key и lock, key.height <= lock.height, key.width <= lock.width, состоящие из нулей и единиц.
 *
 * Проверить, можно ли наложить матрицу key на матрицу lock (без поворота, разрешается только сдвиг) так,
 * чтобы каждой единице в матрице lock (штырь) соответствовал ноль в матрице key (прорезь),
 * а каждому нулю в матрице lock (дырка) соответствовала, наоборот, единица в матрице key (штырь).
 * Ключ при сдвиге не может выходить за пределы замка.
 *
 * Пример: ключ подойдёт, если его сдвинуть на 1 по ширине
 * lock    key
 * 1 0 1   1 0
 * 0 1 0   0 1
 * 1 1 1
 *
 * Вернуть тройку (Triple) -- (да/нет, требуемый сдвиг по высоте, требуемый сдвиг по ширине).
 * Если наложение невозможно, то первый элемент тройки "нет" и сдвиги могут быть любыми.
 */
fun canOpenLock(key: Matrix<Int>, lock: Matrix<Int>): Triple<Boolean, Int, Int> {
    require(key.width <= lock.width && key.height <= lock.height)

    for (i_offset in 0..lock.height - key.height)
        for (j_offset in 0..lock.width - key.width) {
            var passed = true
            for (i in 0 until key.height)
                for (j in 0 until key.width)
                    if (key[i, j] == lock[i + i_offset, j + j_offset]) {
                        passed = false
                    }
            if (passed)
                return Triple(true, i_offset, j_offset)
        }

    return Triple(false, 0, 0)
}

/**
 * Простая
 *
 * Инвертировать заданную матрицу.
 * При инвертировании знак каждого элемента матрицы следует заменить на обратный
 */
operator fun Matrix<Int>.unaryMinus(): Matrix<Int> = MatrixImpl(height, width) {
    i, j -> -this[i, j]
}

/**
 * Средняя
 *
 * Перемножить две заданные матрицы друг с другом.
 * Матрицы можно умножать, только если ширина первой матрицы совпадает с высотой второй матрицы.
 * В противном случае бросить IllegalArgumentException.
 * Подробно про порядок умножения см. статью Википедии "Умножение матриц".
 */
operator fun Matrix<Int>.times(other: Matrix<Int>): Matrix<Int> {
    require(width == other.height)

    return MatrixImpl(height, other.width) {
        i, j ->
        var sum = 0
        for (k in 0 until width) {
            sum += this[i, k] * other[k, j]
        }
        sum
    }
}

/**
 * Сложная
 *
 * В матрице matrix размером 4х4 дана исходная позиция для игры в 15, например
 *  5  7  9  1
 *  2 12 14 15
 *  3  4  6  8
 * 10 11 13  0
 *
 * Здесь 0 обозначает пустую клетку, а 1-15 – фишки с соответствующими номерами.
 * Напомним, что "игра в 15" имеет квадратное поле 4х4, по которому двигается 15 фишек,
 * одна клетка всегда остаётся пустой. Цель игры -- упорядочить фишки на игровом поле.
 *
 * В списке moves задана последовательность ходов, например [8, 6, 13, 11, 10, 3].
 * Ход задаётся номером фишки, которая передвигается на пустое место (то есть, меняется местами с нулём).
 * Фишка должна примыкать к пустому месту по горизонтали или вертикали, иначе ход не будет возможным.
 * Все номера должны быть в пределах от 1 до 15.
 * Определить финальную позицию после выполнения всех ходов и вернуть её.
 * Если какой-либо ход является невозможным или список содержит неверные номера,
 * бросить IllegalStateException.
 *
 * В данном случае должно получиться
 * 5  7  9  1
 * 2 12 14 15
 * 0  4 13  6
 * 3 10 11  8
 */
fun fifteenGameMoves(matrix: Matrix<Int>, moves: List<Int>): Matrix<Int> {
    require(matrix.height == 4 && matrix.width == 4)

    val result = MatrixImpl(4, 4) {
        i, j ->
        require(matrix[i, j] in 0..15)
        matrix[i, j]
    }

    fun swapIfZero(i: Int, j: Int, last_i: Int, last_j: Int): Boolean {
        if (i < 0 || j < 0 || i >= result.height || j >= result.width) return false
        if (result[i, j] == 0) {
            val temp = result[i, j]
            result[i, j] = result[last_i, last_j]
            result[last_i, last_j] = temp
            return true
        }
        return false
    }

    for (move in moves) {
        check(move in 1..15)
        val (i, j) = result find move
        check(swapIfZero(i - 1, j, i, j) || swapIfZero(i + 1, j, i, j)
                || swapIfZero(i, j - 1, i, j) || swapIfZero(i, j + 1, i, j))
    }
    return result
}

/**
 * Очень сложная
 *
 * В матрице matrix размером 4х4 дана исходная позиция для игры в 15, например
 *  5  7  9  2
 *  1 12 14 15
 *  3  4  6  8
 * 10 11 13  0
 *
 * Здесь 0 обозначает пустую клетку, а 1-15 – фишки с соответствующими номерами.
 * Напомним, что "игра в 15" имеет квадратное поле 4х4, по которому двигается 15 фишек,
 * одна клетка всегда остаётся пустой.
 *
 * Цель игры -- упорядочить фишки на игровом поле, приведя позицию к одному из следующих двух состояний:
 *
 *  1  2  3  4          1  2  3  4
 *  5  6  7  8   ИЛИ    5  6  7  8
 *  9 10 11 12          9 10 11 12
 * 13 14 15  0         13 15 14  0
 *
 * Можно математически доказать, что РОВНО ОДНО из этих двух состояний достижимо из любой исходной позиции.
 *
 * Вернуть решение -- список ходов, приводящих исходную позицию к одной из двух упорядоченных.
 * Каждый ход -- это перемена мест фишки с заданным номером с пустой клеткой (0),
 * при этом заданная фишка должна по горизонтали или по вертикали примыкать к пустой клетке (но НЕ по диагонали).
 * К примеру, ход 13 в исходной позиции меняет местами 13 и 0, а ход 11 в той же позиции невозможен.
 *
 * Одно из решений исходной позиции:
 *
 * [8, 6, 14, 12, 4, 11, 13, 14, 12, 4,
 * 7, 5, 1, 3, 11, 7, 3, 11, 7, 12, 6,
 * 15, 4, 9, 2, 4, 9, 3, 5, 2, 3, 9,
 * 15, 8, 14, 13, 12, 7, 11, 5, 7, 6,
 * 9, 15, 8, 14, 13, 9, 15, 7, 6, 12,
 * 9, 13, 14, 15, 12, 11, 10, 9, 13, 14,
 * 15, 12, 11, 10, 9, 13, 14, 15]
 *
 * Перед решением этой задачи НЕОБХОДИМО решить предыдущую
 */
fun fifteenGameSolution(matrix: Matrix<Int>): List<Int> {
    TODO()
    /*
    require(matrix.height == 4 && matrix.width == 4)

    val copy = MatrixImpl(4, 4) {
        i, j -> matrix[i, j]
    }

    val moves = mutableListOf<Int>()

    for (chip in 1..15) {
        var (zero_i, zero_j) = copy find 0
        var (chip_i, chip_j) = copy find chip
        when (chip) {
            1, 2 -> {

            }
        }
    }
    */
}

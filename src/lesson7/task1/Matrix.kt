@file:Suppress("UNUSED_PARAMETER", "unused")
package lesson7.task1

import java.util.*

/**
 * Ячейка матрицы: row = ряд, column = колонка
 */
data class Cell(val row: Int, val column: Int)

/**
 * Интерфейс, описывающий возможности матрицы. E = тип элемента матрицы
 */
interface Matrix<E> {
    /** Высота */
    val height: Int

    /** Ширина */
    val width: Int

    /**
     * Доступ к ячейке.
     * Методы могут бросить исключение, если ячейка не существует или пуста
     */
    operator fun get(row: Int, column: Int): E
    operator fun get(cell: Cell): E

    /**
     * Запись в ячейку.
     * Методы могут бросить исключение, если ячейка не существует
     */
    operator fun set(row: Int, column: Int, value: E)
    operator fun set(cell: Cell, value: E)
}

/**
 * Простая
 *
 * Метод для создания матрицы, должен вернуть РЕАЛИЗАЦИЮ Matrix<E>.
 * height = высота, width = ширина, e = чем заполнить элементы.
 * Бросить исключение IllegalArgumentException, если height или width <= 0.
 */
fun <E> createMatrix(height: Int, width: Int, e: E): Matrix<E> = MatrixImpl(height, width) { _, _ -> e }

@Suppress("UNCHECKED_CAST")
/**
 * Средняя сложность
 *
 * Реализация интерфейса "матрица"
 */
class MatrixImpl<E> constructor(n: Int, m: Int, init: (i: Int, j: Int) -> E) : Matrix<E> {

    private val data: Array<Any>

    private fun checkArgs(row: Int, column: Int) {
        if (row >= height || column >= width || row < 0 || column < 0)
            throw IndexOutOfBoundsException("matrix indexes was [$row, $column]")
    }

    override val height: Int = n

    override val width: Int = m

    override fun get(row: Int, column: Int): E  {
        checkArgs(row, column)
        return data[row * width + column] as E
    }

    override fun get(cell: Cell): E  {
        checkArgs(cell.row, cell.column)
        return data[cell.row * width + cell.column] as E
    }

    override fun set(row: Int, column: Int, value: E) {
        checkArgs(row, column)
        data[row * width + column] = value as Any
    }

    override fun set(cell: Cell, value: E) {
        checkArgs(cell.row, cell.column)
        data[cell.row * width + cell.column] = value as Any
    }

    infix fun find(value: E): Cell {
        for (i in 0 until data.size)
            if (data[i] == value)
                return Cell(i / width, i % width)
        return Cell(-1,-1)
    }

    fun isExactMatrix(other: Matrix<*>): Boolean {
        if (height == other.height && width == other.width) {
            for (i in 0 until height)
                for (j in 0 until width)
                    if (get(i, j) != other[i, j])
                        return false
            return true
        }
        return false
    }

    override fun equals(other: Any?): Boolean =
            other is MatrixImpl<*> && data.contentEquals(other.data) || other is Matrix<*> && isExactMatrix(other)

    override fun toString(): String {
        val begin = "Matrix<${data[0].javaClass}>(height = $height, width = $width)\n"
        val rows = mutableListOf<String>()
        for (i in 0 until height) {
            val columns = mutableListOf<String>()
            for (j in 0 until width) {
                columns.add(this[i, j].toString())
            }
            rows.add(columns.joinToString(prefix = "\t|", separator = "\t", postfix = "|"))
        }
        return rows.joinToString(prefix = begin,separator = "\n")
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(data)
        result = 31 * result + height
        result = 31 * result + width
        return result
    }

    init {
        require(m > 0 && n > 0) {
            when {
                m <= 0 && m <= 0 -> "height (height = $n) and width (width = $m) values are equals or lower than zero"
                n <= 0 -> "height value 'height = $n' is equals or lower than zero"
                m <= 0 -> "width value 'width = $m' is equals or lower than zero"
                else -> ""
            }
        }
        data = Array(m * n) {
            init(it / m, it % m) as Any
        }
    }
}


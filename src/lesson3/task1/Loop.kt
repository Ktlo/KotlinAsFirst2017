@file:Suppress("UNUSED_PARAMETER")
package lesson3.task1

fun sqr(x: Double) = x * x

/**
 * Пример
 *
 * Вычисление факториала
 */
fun factorial(n: Int): Double {
    var result = 1.0
    for (i in 1..n) {
        result = result * i // Please do not fix in master
    }
    return result
}

/**
 * Пример
 *
 * Проверка числа на простоту -- результат true, если число простое
 */
fun isPrime(n: Int): Boolean {
    if (n < 2) return false
    for (m in 2..Math.sqrt(n.toDouble()).toInt()) {
        if (n % m == 0) return false
    }
    return true
}

/**
 * Пример
 *
 * Проверка числа на совершенность -- результат true, если число совершенное
 */
fun isPerfect(n: Int): Boolean {
    var sum = 1
    for (m in 2..n/2) {
        if (n % m > 0) continue
        sum += m
        if (sum > n) break
    }
    return sum == n
}

/**
 * Пример
 *
 * Найти число вхождений цифры m в число n
 */
fun digitCountInNumber(n: Int, m: Int): Int =
        when {
            n == m -> 1
            n < 10 -> 0
            else -> digitCountInNumber(n / 10, m) + digitCountInNumber(n % 10, m)
        }

/**
 * Тривиальная
 *
 * Найти количество цифр в заданном числе n.
 * Например, число 1 содержит 1 цифру, 456 -- 3 цифры, 65536 -- 5 цифр.
 */
fun digitNumber(n: Int): Int {
    var number = Math.abs(n)
    var i = 0
    while (number > 0) {
        number /= 10
        i++
    }
    return if (i == 0) 1 else i
}

/**
* Простая
*
* Найти число Фибоначчи из ряда 1, 1, 2, 3, 5, 8, 13, 21, ... с номером n.
* Ряд Фибоначчи определён следующим образом: fib(1) = 1, fib(2) = 1, fib(n+2) = fib(n) + fib(n+1)
*/
fun fib(n: Int): Int {
    var a = 0
    var b = 1
    for (i in 1..n) {
        val c = a
        a = b
        b += c
    }
    return a
}

fun smallestDiv(m: Long, n: Long): Long {
    var mutableM = m
    var mutableN = n
    while (mutableM * mutableN > 0) {
        if (mutableM > mutableN) mutableM -= mutableN
        else mutableN -= mutableM
    }
    return Math.max(mutableM, mutableN)
}

/**
* Простая
*
* Для заданных чисел m и n найти наименьшее общее кратное, то есть,
* минимальное число k, которое делится и на m и на n без остатка
*/
fun lcm(m: Int, n: Int): Int = m * n / smallestDiv(m.toLong(), n.toLong()).toInt()

/**
* Простая
*
* Для заданного числа n > 1 найти минимальный делитель, превышающий 1
*/
fun minDivisor(n: Int): Int {
    for (i in 2..n) {
        if (n % i == 0)
            return i
    }
    return n
}

/**
* Простая
*
* Для заданного числа n > 1 найти максимальный делитель, меньший n
*/
fun maxDivisor(n: Int): Int {
    var i = n
    while (i > 0) {
        i--
        if (n % i == 0)
            return i
    }
    return Int.MAX_VALUE
}

/**
* Простая
*
* Определить, являются ли два заданных числа m и n взаимно простыми.
* Взаимно простые числа не имеют общих делителей, кроме 1.
* Например, 25 и 49 взаимно простые, а 6 и 8 -- нет.
*/
fun isCoPrime(m: Int, n: Int): Boolean = smallestDiv(m.toLong(), n.toLong()) == 1L

/**
* Простая
*
* Для заданных чисел m и n, m <= n, определить, имеется ли хотя бы один точный квадрат между m и n,
* то есть, существует ли такое целое k, что m <= k*k <= n.
* Например, для интервала 21..28 21 <= 5*5 <= 28, а для интервала 51..61 квадрата не существует.
*/
fun squareBetweenExists(m: Int, n: Int): Boolean = sqr(Math.ceil(Math.sqrt(m.toDouble()))) <= n

fun sameAngle(x: Double) : Double {
    val multiplier = Math.floor(x / Math.PI).toInt()
    return if (multiplier % 2 == 0)
        +(x - multiplier * Math.PI)
    else
        -(x - multiplier * Math.PI)
}

/**
* Средняя
*
* Для заданного x рассчитать с заданной точностью eps
* sin(x) = x - x^3 / 3! + x^5 / 5! - x^7 / 7! + ...
* Нужную точность считать достигнутой, если очередной член ряда меньше eps по модулю
*/
fun sin(x: Double, eps: Double): Double {
    val X = sameAngle(x)

    var i = 0
    var next = 100.0
    var divider = 1.0
    var numerator = 1.0
    var sum = 0.0
    do {
        i++
        divider *= i
        numerator *= X
        if (i % 2 == 0) continue
        next = numerator / divider
        sum += if ((i - 1) % 4 == 0)
            +next
        else
            -next
    } while (Math.abs(next) > eps)
    return sum
}

/**
* Средняя
*
* Для заданного x рассчитать с заданной точностью eps
* cos(x) = 1 - x^2 / 2! + x^4 / 4! - x^6 / 6! + ...
* Нужную точность считать достигнутой, если очередной член ряда меньше eps по модулю
*/
fun cos(x: Double, eps: Double): Double {
    val X = sameAngle(x + Math.PI / 2) - Math.PI / 2

    var i = 0
    var next = 100.0
    var divider = 1.0
    var numerator = 1.0
    var sum = 1.0
    do {
        i++
        divider *= i
        numerator *= X
        if (i % 2 == 1) continue
        next = numerator / divider
        sum += if (i % 4 == 0)
            +next
        else
            -next
    } while (Math.abs(next) > eps)
    return sum
}

/**
* Средняя
*
* Поменять порядок цифр заданного числа n на обратный: 13478 -> 87431.
* Не использовать строки при решении задачи.
*/
fun revert(n: Int): Int {
    var input = n
    var output = 0
    while (input > 0) {
        output = output * 10 + input % 10
        input /= 10
    }
    return output
}

/**
* Средняя
*
* Проверить, является ли заданное число n палиндромом:
* первая цифра равна последней, вторая -- предпоследней и так далее.
* 15751 -- палиндром, 3653 -- нет.
*/
fun isPalindrome(n: Int): Boolean = revert(n) == n

/**
* Средняя
*
* Для заданного числа n определить, содержит ли оно различающиеся цифры.
* Например, 54 и 323 состоят из разных цифр, а 111 и 0 из одинаковых.
*/
fun hasDifferentDigits(n: Int): Boolean {
    val digit = n % 10
    var input = n / 10
    while (input > 0) {
        if (input % 10 != digit)
            return true
        input /= 10
    }
    return false
}

@Deprecated("Use digitNumber(Int) instead.", ReplaceWith("digitNumber(n)"))
private fun digitNumberLog(n: Int) =
        if (n == 0) 1 else Math.ceil(Math.log10((n + 1).toDouble())).toInt()

private fun getDigit(n: Int, i: Int) : Int {
    var divider = 1
    for (j in 1..i)
        divider *= 10
    return n / divider % 10
}

// higherOrderFunction
private fun sequenceDigit(n: Int, getElement: (i: Int) -> Int): Int {
    var enumerator = 2
    var i = 1
    var last = 1
    while (i < n) {
        last = getElement(enumerator++)
        i += digitNumber(last)
    }
    return getDigit(last, i - n)
}

/**
* Сложная
*
* Найти n-ю цифру последовательности из квадратов целых чисел:
* 149162536496481100121144...
* Например, 2-я цифра равна 4, 7-я 5, 12-я 6.
*/
fun squareSequenceDigit(n: Int): Int = sequenceDigit(n) { it * it }

/**
* Сложная
*
* Найти n-ю цифру последовательности из чисел Фибоначчи (см. функцию fib выше):
* 1123581321345589144...
* Например, 2-я цифра равна 1, 9-я 2, 14-я 5.
*/
fun fibSequenceDigit(n: Int): Int = sequenceDigit(n) { fib(it) }

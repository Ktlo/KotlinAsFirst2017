@file:Suppress("UNUSED_PARAMETER")
package lesson5.task1

import lesson4.task1.RomanDigits

/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Пример: консольный ввод
 */
fun main(args: Array<String>) {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        }
        else {
            println("Прошло секунд с начала суток: $seconds")
        }
    }
    else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}

private class Month(val number : String, val maxData : Int)

private val months = mapOf(
        "января" to Month("01", 31), "февраля" to Month("02", 29), "марта" to Month("03", 31),
        "апреля" to Month("04", 31), "мая" to Month("05", 29), "июня" to Month("06", 31),
        "июля" to Month("07", 31), "августа" to Month("08", 29), "сентября" to Month("09", 31),
        "октября" to Month("10", 31), "ноября" to Month("11", 29), "декабря" to Month("12", 31)
)

private val dateStrToDigit_regex = Regex("^\\d{1,2} [а-я]{3,} \\d+$")

/**
 * Средняя
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку
 */
fun dateStrToDigit(str: String): String {
    if (!(str matches dateStrToDigit_regex))
        return ""
    val (data_num, month, year) = str.split(' ')
    return if (months.containsKey(month) && data_num.toInt() in 1..months[month]!!.maxData)
        "${if (data_num.length == 1) "0"+data_num else data_num}.${months[month]!!.number}.$year"
    else
        ""
}

private val month_array = months.values.toTypedArray()
private val month_names = arrayOf("января", "февраля", "марта",
                                "апреля", "мая", "июня",
                                "июля", "августа", "сентября",
                                "октября", "ноября", "декабря")

private val dateDigitToStr_regex = Regex("^\\d{2}\\.\\d{2}\\.\\d+$")

/**
 * Средняя
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 */
fun dateDigitToStr(digital: String): String {
    if (!(digital matches dateDigitToStr_regex))
        return ""
    val (data_num, month, year) = digital.split('.')
    val m = month.toInt() - 1
    val d = data_num.toInt()
    return if (m in 0..11 && d in 1..month_array[m].maxData)
        "$d ${month_names[m]} ${year.toInt()}"
    else ""
}

private val telephone_regex = Regex("^\\s*(\\+\\d+)?[\\s\\-]*(\\(\\d+\\))?([\\s\\-]*\\d)+\\s*$")

private val telephone_regex_replace = Regex("[\\Q-() \\E]")

/**
 * Средняя
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -98 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку
 */
fun flattenPhoneNumber(phone: String): String =
    if (phone matches telephone_regex)
        phone.replace(telephone_regex_replace, "")
        //phone.replace(" ", "").replace("-", "")
    else ""

private val bestLongJump_regex = Regex("^(\\d+|-|%)( +(\\d+|-|%))*$")
private val bestLongJump_regex_nums = Regex("\\d+")

/**
 * Средняя
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int {
    var max_jump = -1
    if (jumps matches bestLongJump_regex)
        for (num in bestLongJump_regex_nums.findAll(jumps)) {
            val curr = num.value.toInt()
            if (curr > max_jump)
                max_jump = curr
        }
    return max_jump
}

private val assertJumps = Regex("(\\d+ [+\\-%]+ )*\\d+ [+\\-%]+")
private val findAllJumps = Regex("\\d+ [\\-%]*\\+")
private val height_regex = Regex("\\d+")
/**
 * Сложная
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки вернуть -1.
 */
fun bestHighJump(jumps: String): Int {
    if (!(jumps matches assertJumps))
        return -1
    var most_high = 0
    for (jump in findAllJumps.findAll(jumps)) {
        val curr_jump = height_regex.find(jump.value)!!.value.toInt()
        if (curr_jump > most_high)
            most_high = curr_jump
    }
    return most_high
}

private val plusMinus_assert_regex = Regex("\\d+( [+\\-] \\d+)*")
private val plusMinus_number = Regex("\\d+")
/**
 * Сложная
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 */
fun plusMinus(expression: String): Int {
    var summ = 0
    var sign = true
    require(expression matches plusMinus_assert_regex)
    for (one in expression.split(' ')) {
        when {
            one matches plusMinus_number -> summ += if (sign) one.toInt() else -one.toInt()
            one == "+" -> sign = true
            else -> sign = false
        }
    }
    return summ
}

private val firstDuplicateIndex_word_regex = Regex("\\S+")

/**
 * Сложная
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 */
fun firstDuplicateIndex(str: String): Int {
    var last_word = ""
    var last_index = -1
    for (word in firstDuplicateIndex_word_regex.findAll(str)) {
        if (word.value.compareTo(last_word, ignoreCase = true) == 0)
            return last_index
        else {
            last_word = word.value
            last_index = word.range.start
        }
    }
    return -1
}

/*
    Я подумал, что название состоит из слов, а не из случаёных символов.

private val mostExpensive_regex = Regex("^(([а-я]|[А-Я]|Ё|ё|\\w)+ \\d+\\.\\d; )*(([а-я]|[А-Я]|Ё|ё|\\w)+ \\d+\\.\\d)$")
private val mostExpensive_each = Regex("([а-я]|[А-Я]|Ё|ё|\\w)+ \\d+\\.\\d")
private val mostExpensive_product = Regex("([а-я]|[А-Я]|Ё|ё|\\w)+")
private val mostExpensive_price = Regex("\\d+\\.\\d")
*/

private val mostExpensive_regex = Regex("^((\\S)+ \\d+\\.\\d; )*((\\S)+ \\d+\\.\\d)$")
private val mostExpensive_each = Regex("(\\S)+ \\d+\\.\\d")
private val mostExpensive_product = Regex("(\\S)+")
private val mostExpensive_price = Regex("\\d+\\.\\d")

/**
 * Сложная
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62.5; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть положительными
 */
fun mostExpensive(description: String): String {
    if (!(description matches mostExpensive_regex))
        return ""
    var max_price = .0
    var product_name = ""
    for (each in mostExpensive_each.findAll(description)) {
        val price = mostExpensive_price.find(each.value)!!.value.toDouble()
        if (price > max_price) {
            max_price = price
            product_name = mostExpensive_product.find(each.value)!!.value
        }
    }
    return product_name
}

private val fromRoman_regex = Regex("^M*(CM)?((DC{0,3})|(CD)|C{1,3})?(XC|LX{0,3}|XL|X{1,3})?(IX|VI{0,3}|IV|I{1,3})?$")

/**
 * Сложная
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */
fun fromRoman(roman: String): Int {
    when {
        roman.isBlank() -> 0
        !(roman matches fromRoman_regex) -> -1
    }
    var result = 0
    var index = 0
    var correct = false
    // Е-ее-еее!!!! Коуде реюзабилити!
    for (symbol in RomanDigits.values()) {
        val name = symbol.name
        val symbol_len = name.length
        while (roman.length - index >= symbol_len && roman.substring(index, index + symbol_len) == name) {
            result += symbol.number
            index += symbol_len
            correct = true
        }
    }
    return if (correct) result else -1
}

private val brainFuck_regex = Regex("^[\\Q+-<>[] \\E]*$")

private fun requireBrainFuck(commands: String) {
    require(commands matches brainFuck_regex)
    var braces = 0
    for (i in 0 until commands.length) {
        when (commands[i]) {
            '[' -> braces++
            ']' -> braces--
        }
    }
    require(braces == 0)
}

private fun braceEnds(commands: String, offset: Int): Int {
    var braces = 1
    var ptr = offset
    while (braces > 0) {
        ptr++
        when (commands[ptr]) {
            '[' -> braces++
            ']' -> braces--
        }
    }
    return ptr
}

private fun braceBegin(commands: String, offset: Int): Int {
    var braces = 1
    var ptr = offset
    while (braces > 0) {
        ptr--
        when (commands[ptr]) {
            '[' -> braces--
            ']' -> braces++
        }
    }
    return ptr
}

/**
 * Очень сложная
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */
fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> {
    requireBrainFuck(commands)
    val list = MutableList(cells) {0}
    var counter = limit
    var list_ptr = cells / 2
    var cmd_ptr = 0
    val end = commands.length
    while (counter > 0) {
        if (cmd_ptr == end)
            break
        when (commands[cmd_ptr]) {
            '+' -> list[list_ptr]++
            '-' -> list[list_ptr]--
            '>' -> {
                list_ptr++
                check(list_ptr < cells)
            }
            '<' -> {
                list_ptr--
                check(list_ptr >= 0)
            }
            '[' -> if (list[list_ptr] == 0) {
                cmd_ptr = braceEnds(commands, cmd_ptr)
            }
            ']' -> if (list[list_ptr] != 0) {
                cmd_ptr = braceBegin(commands, cmd_ptr)
            }
        }
        cmd_ptr++
        counter--
    }
    return list
}
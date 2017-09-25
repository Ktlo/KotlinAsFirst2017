@file:Suppress("UNUSED_PARAMETER")
package lesson8.task1

import java.io.BufferedOutputStream
import java.io.BufferedWriter
import java.io.File
import java.util.*

/**
 * Пример
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Вывести его в выходной файл с именем outputName, выровняв по левому краю,
 * чтобы длина каждой строки не превосходила lineLength.
 * Слова в слишком длинных строках следует переносить на следующую строку.
 * Слишком короткие строки следует дополнять словами из следующей строки.
 * Пустые строки во входном файле обозначают конец абзаца,
 * их следует сохранить и в выходном файле
 */
fun alignFile(inputName: String, lineLength: Int, outputName: String) {
    val outputStream = File(outputName).bufferedWriter()
    var currentLineLength = 0
    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) {
            outputStream.newLine()
            if (currentLineLength > 0) {
                outputStream.newLine()
                currentLineLength = 0
            }
            continue
        }
        for (word in line.split(" ")) {
            if (currentLineLength > 0) {
                if (word.length + currentLineLength >= lineLength) {
                    outputStream.newLine()
                    currentLineLength = 0
                }
                else {
                    outputStream.write(" ")
                    currentLineLength++
                }
            }
            outputStream.write(word)
            currentLineLength += word.length
        }
    }
    outputStream.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * На вход подаётся список строк substrings.
 * Вернуть ассоциативный массив с числом вхождений каждой из строк в текст.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 */
fun countSubstrings(inputName: String, substrings: List<String>): Map<String, Int> {
    val input = File(inputName).readText()
    val result = mutableMapOf<String, Int>()
    for (string in substrings) {
        var counter = 0
        val first_letter = string.first().toLowerCase()
        for (i in 0 .. input.length - string.length) {
            if (input[i].toLowerCase() == first_letter &&
                    input.substring(i, i + string.length).compareTo(string, ignoreCase = true) == 0) {
                counter++
            }
        }
        result[string] = counter
    }
    return result
}

/**
 * Средняя
 *
 * В русском языке, как правило, после букв Ж, Ч, Ш, Щ пишется И, А, У, а не Ы, Я, Ю.
 * Во входном файле с именем inputName содержится некоторый текст на русском языке.
 * Проверить текст во входном файле на соблюдение данного правила и вывести в выходной
 * файл outputName текст с исправленными ошибками.
 *
 * Регистр заменённых букв следует сохранять.
 *
 * Исключения (жюри, брошюра, парашют) в рамках данного задания обрабатывать не нужно
 *
 */
fun sibilants(inputName: String, outputName: String) {
    val result = File(inputName).readText().toCharArray()
    for (i in 0 until result.size - 1) {
        when (result[i]) {
            'Ж', 'ж', 'Ч', 'ч', 'Ш', 'ш', 'Щ', 'щ' -> when (result[i + 1]) {
                'Ы' -> result[i + 1] = 'И'
                'ы' -> result[i + 1] = 'и'
                'Я' -> result[i + 1] = 'А'
                'я' -> result[i + 1] = 'а'
                'Ю' -> result[i + 1] = 'У'
                'ю' -> result[i + 1] = 'у'
            }
        }
    }
    File(outputName).writeText(result.joinToString(separator = ""))
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по центру
 * относительно самой длинной строки.
 *
 * Выравнивание следует производить путём добавления пробелов в начало строки.
 *
 *
 * Следующие правила должны быть выполнены:
 * 1) Пробелы в начале и в конце всех строк не следует сохранять.
 * 2) В случае невозможности выравнивания строго по центру, строка должна быть сдвинута в ЛЕВУЮ сторону
 * 3) Пустые строки не являются особым случаем, их тоже следует выравнивать
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых)
 *
 */
fun centerFile(inputName: String, outputName: String) {
    val lines = File(inputName).readLines().map { it.trim() }
    var max_size = 0
    for (line in lines)
        if (max_size < line.length)
            max_size = line.length
    val output_stream = File(outputName).bufferedWriter()
    for (line in lines) {
        val delta = (max_size - line.length) / 2
        output_stream.write("${" ".repeat(if (delta < 0) 0 else delta)}$line\n")
    }

    output_stream.close()
}

private val word_pattern = Regex("\\S+")

/**
 * Сложная
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по левому и правому краю относительно
 * самой длинной строки.
 * Выравнивание производить, вставляя дополнительные пробелы между словами: равномерно по всей строке
 *
 * Слова внутри строки отделяются друг от друга одним или более пробелом.
 *
 * Следующие правила должны быть выполнены:
 * 1) Каждая строка входного и выходного файла не должна начинаться или заканчиваться пробелом.
 * 2) Пустые строки или строки из пробелов трансформируются в пустые строки без пробелов.
 * 3) Строки из одного слова выводятся без пробелов.
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых).
 *
 * Равномерность определяется следующими формальными правилами:
 * 5) Число пробелов между каждыми двумя парами соседних слов не должно отличаться более, чем на 1.
 * 6) Число пробелов между более левой парой соседних слов должно быть больше или равно числу пробелов
 *    между более правой парой соседних слов.
 *
 * Следует учесть, что входной файл может содержать последовательности из нескольких пробелов  между слвоами. Такие
 * последовательности следует учитывать при выравнивании и при необходимости избавляться от лишних пробелов.
 * Из этого следуют следующие правила:
 * 7) В самой длинной строке каждая пара соседних слов должна быть отделена В ТОЧНОСТИ одним пробелом
 * 8) Если входной файл удовлетворяет требованиям 1-7, то он должен быть в точности идентичен выходному файлу
 */
fun alignFileByWidth(inputName: String, outputName: String) {
    val lines = File(inputName).readLines().map { it.trim() }
    var max_size = 0
    for (line in lines)
        if (max_size < line.length)
            max_size = line.length

    val output_stream = File(outputName).bufferedWriter()
    for (line in lines) {
        val whitespaces = max_size - line.length + line.count { it == ' ' }
        val words =  word_pattern.findAll(line)
        val words_number = words.count()
        when (words_number) {
            0 -> {}
            1 -> output_stream.write(words.first().value)
            else -> {
                val space_number = words_number - 1
                val median = whitespaces / space_number
                val module = whitespaces % space_number + 1
                var i = 0
                var isFirst = true
                for (word in words) {
                    if (isFirst) {
                        output_stream.write(word.value)
                        isFirst = false
                    }
                    else {
                        output_stream.write("${" ".repeat(median + if (i / module == 0) 1 else 0)}${word.value}")
                    }
                    i++
                }
            }
        }
        output_stream.write("\n")
    }

    output_stream.close()
}

private val just_words = Regex("([а-яё]|[А-ЯЁ]|[a-z]|[A-Z])+")

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * Вернуть ассоциативный массив, содержащий 20 наиболее часто встречающихся слов с их количеством.
 * Если в тексте менее 20 различных слов, вернуть все слова.
 *
 * Словом считается непрерывная последовательность из букв (кириллических,
 * либо латинских, без знаков препинания и цифр).
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 * Ключи в ассоциативном массиве должны быть в нижнем регистре.
 *
 */
fun top20Words(inputName: String): Map<String, Int> {
    val input = File(inputName).readText()
    val result = mutableMapOf<String, Int>()

    for (word in just_words.findAll(input)) {
        val value = word.value.toLowerCase()
        if (result.containsKey(value))
            result[value] = result[value]!! + 1
        else
            result[value] = 1
    }

    val sorted = result.toList(). sortedWith(Comparator {
        a, b ->
        b.second.compareTo(a.second)
    })
    result.clear()

    return sorted.subList(0, if (sorted.size > 20) 20 else sorted.size).toMap()
}

/**
 * Средняя
 *
 * Реализовать транслитерацию текста из входного файла в выходной файл посредством динамически задаваемых правил.

 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * В ассоциативном массиве dictionary содержится словарь, в котором некоторым символам
 * ставится в соответствие строчка из символов, например
 * mapOf('з' to "zz", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "yy", '!' to "!!!")
 *
 * Необходимо вывести в итоговый файл с именем outputName
 * содержимое текста с заменой всех символов из словаря на соответствующие им строки.
 *
 * При этом регистр символов в словаре должен игнорироваться,
 * но при выводе символ в верхнем регистре отображается в строку, начинающуюся с символа в верхнем регистре.
 *
 * Пример.
 * Входной текст: Здравствуй, мир!
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun transliterate(inputName: String, dictionary: Map<Char, String>, outputName: String) {
    val input = File(inputName).bufferedReader()
    val output = File(outputName).bufferedWriter()

    while (input.ready()) {
        val symbol = input.read().toChar()
        if (dictionary.containsKey(symbol.toLowerCase()) || dictionary.containsKey(symbol.toUpperCase())) {
            var replaceString = (dictionary[symbol.toLowerCase()] ?: dictionary[symbol.toUpperCase()])!!
            if (symbol.isUpperCase())
                replaceString = replaceString.first().toUpperCase() + replaceString.substring(1)
            output.write(replaceString)
        }
        else
            output.write(symbol.toString())
    }

    input.close()
    output.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName имеется словарь с одним словом в каждой строчке.
 * Выбрать из данного словаря наиболее длинное слово,
 * в котором все буквы разные, например: Неряшливость, Четырёхдюймовка.
 * Вывести его в выходной файл с именем outputName.
 * Если во входном файле имеется несколько слов с одинаковой длиной, в которых все буквы разные,
 * в выходной файл следует вывести их все через запятую.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 * Пример входного файла:
 * Карминовый
 * Боязливый
 * Некрасивый
 * Остроумный
 * БелогЛазый
 * ФиолетОвый

 * Соответствующий выходной файл:
 * Карминовый, Некрасивый
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun chooseLongestChaoticWord(inputName: String, outputName: String) {
    val input = File(inputName).bufferedReader()
    val specialWords = mutableListOf<String>()

    while (input.ready()) {
        val string = input.readLine()!!
        val word = string.toCharArray().map { it.toLowerCase() }
        var areLettersDifferent = true
        val length = word.size
        comparingLetters@for (i in 0 until length - 1) {
            val char = word[i]
            for (letter in i + 1 until length) {
                if (char == word[letter]) {
                    areLettersDifferent = false
                    break@comparingLetters
                }
            }
        }
        if (areLettersDifferent)
            specialWords.add(string)
    }
    specialWords.sortWith(Comparator { a, b -> b.length.compareTo(a.length) })
    val maxLength = specialWords.first().length

    val output = File(outputName).bufferedWriter()

    var isFirst = true
    for (word in specialWords) {
        if (word.length == maxLength) {
            if (isFirst) {
                output.write(word)
                isFirst = false
            }
            else
                output.write(", $word")
        }
    }

    input.close()
    output.close()
}

typealias HTML = BufferedWriter
typealias Paragraph = String

private fun beginHtmlBody(filename: String): HTML {
    val output = File(filename).bufferedWriter()
    output.write("<html>\n\t<body>\n")
    return output
}

private fun HTML.endHtmlBody() {
    write("\n\t</body>\n</html>")
    close()
}

private fun HTML.addParagraph(paragraph: Paragraph) {
    write("\t\t<p>\n")
    for (line in paragraph.lines()) {
        write("\t\t\t$line\n")
    }
    write("\t\t</p>\n")
}

private fun splitParagraphsFromFile(filename: String): MutableList<Paragraph> {
    val result = mutableListOf<String>()
    for (line in File(filename).readText().split("\n\n")) {
        if (line.isNotBlank())
            result.add(line)
    }
    return result
}

private fun MutableList<String>.addCanceled(string: Paragraph) {
    var isCanceled = false
    for (text in string.split("~~")) {
        if (isCanceled)
            add("<s>")
        addBold(text)
        if (isCanceled)
            add("</s>")
        isCanceled = !isCanceled
    }

}

private val boldBorders = Regex("\\*\\*\\S(.+\\S)?\\*?\\*\\*")

private fun MutableList<String>.addBold(string: String) {
    var lastIndex = 0
    for (text in boldBorders.findAll(string)) {
        addCursive(string.substring(lastIndex, text.range.start - 1))
        lastIndex = text.range.last + 1
        add("<b>")
        addCursive(string.substring(text.range.start + 2, text.range.last - 2))
        add("</b>")
    }
    addCursive(string.substring(lastIndex))
}

private val cursiveBorders = Regex("\\*\\S(.+\\S)?\\*")

private fun MutableList<String>.addCursive(string: String) {
    var lastIndex = 0
    for (text in cursiveBorders.findAll(string)) {
        add(string.substring(lastIndex, text.range.start - 1))
        lastIndex = text.range.last + 1
        add("<i>")
        add(string.substring(text.range.start + 1, text.range.last - 1))
        add("</i>")
    }
    add(string.substring(lastIndex))
}

private fun Paragraph.applyFontModifiers(): Paragraph {
    var isCanceled = false
    val list = mutableListOf<String>()
    list.addCanceled(this)
    return list.joinToString(separator = "")
}

/**
 * Сложная
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе элементы текстовой разметки следующих типов:
 * - *текст в курсивном начертании* -- курсив
 * - **текст в полужирном начертании** -- полужирный
 * - ~~зачёркнутый текст~~ -- зачёркивание
 *
 * Следует вывести в выходной файл этот же текст в формате HTML:
 * - <i>текст в курсивном начертании</i>
 * - <b>текст в полужирном начертании</b>
 * - <s>зачёркнутый текст</s>
 *
 * Кроме того, все абзацы исходного текста, отделённые друг от друга пустыми строками, следует обернуть в теги <p>...</p>,
 * а весь текст целиком в теги <html><body>...</body></html>.
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 * Отдельно следует заметить, что открывающая последовательность из трёх звёздочек (***) должна трактоваться как "<b><i>"
 * и никак иначе.
 *
 * Пример входного файла:
Lorem ipsum *dolor sit amet*, consectetur **adipiscing** elit.
Vestibulum lobortis, ~~Est vehicula rutrum *suscipit*~~, ipsum ~~lib~~ero *placerat **tortor***,

Suspendisse ~~et elit in enim tempus iaculis~~.
 *
 * Соответствующий выходной файл:
<html>
    <body>
        <p>
            Lorem ipsum <i>dolor sit amet</i>, consectetur <b>adipiscing</b> elit.
            Vestibulum lobortis. <s>Est vehicula rutrum <i>suscipit</i></s>, ipsum <s>lib</s>ero <i>placerat <b>tortor</b></i>.
        </p>
        <p>
            Suspendisse <s>et elit in enim tempus iaculis</s>.
        </p>
    </body>
</html>
 *
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlSimple(inputName: String, outputName: String) {
    TODO()
    /*
    val htmlDoc = beginHtmlBody(outputName)

    val paragraphs = splitParagraphsFromFile(inputName)
    for (paragraph in paragraphs) {
        htmlDoc.addParagraph(paragraph.applyFontModifiers())
    }

    htmlDoc.endHtmlBody()
    */
}

/**
 * Сложная
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе набор вложенных друг в друга списков.
 * Списки бывают двух типов: нумерованные и ненумерованные.
 *
 * Каждый элемент ненумерованного списка начинается с новой строки и символа '*', каждый элемент нумерованного списка --
 * с новой строки, числа и точки. Каждый элемент вложенного списка начинается с отступа из пробелов, на 4 пробела большего,
 * чем список-родитель. Максимально глубина вложенности списков может достигать 6. "Верхние" списки файла начинются
 * прямо с начала строки.
 *
 * Следует вывести этот же текст в выходной файл в формате HTML:
 * Нумерованный список:
 * <ol>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ol>
 *
 * Ненумерованный список:
 * <ul>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ul>
 *
 * Кроме того, весь текст целиком следует обернуть в теги <html><body>...</body></html>
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 *
 * Пример входного файла:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
* Утка по-пекински
    * Утка
    * Соус
* Салат Оливье
    1. Мясо
        * Или колбаса
    2. Майонез
    3. Картофель
    4. Что-то там ещё
* Помидоры
* Фрукты
    1. Бананы
    23. Яблоки
        1. Красные
        2. Зелёные
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 *
 *
 * Соответствующий выходной файл:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
<html>
  <body>
    <ul>
      <li>
        Утка по-пекински
        <ul>
          <li>Утка</li>
          <li>Соус</li>
        </ul>
      </li>
      <li>
        Салат Оливье
        <ol>
          <li>Мясо
            <ul>
              <li>
                  Или колбаса
              </li>
            </ul>
          </li>
          <li>Майонез</li>
          <li>Картофель</li>
          <li>Что-то там ещё</li>
        </ol>
      </li>
      <li>Помидоры</li>
      <li>
        Яблоки
        <ol>
          <li>Красные</li>
          <li>Зелёные</li>
        </ol>
      </li>
    </ul>
  </body>
</html>
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlLists(inputName: String, outputName: String) {
    TODO()
}

/**
 * Очень сложная
 *
 * Реализовать преобразования из двух предыдущих задач одновременно над одним и тем же файлом.
 * Следует помнить, что:
 * - Списки, отделённые друг от друга пустой строкой, являются разными и должны оказаться в разных параграфах выходного файла.
 *
 */
fun markdownToHtml(inputName: String, outputName: String) {
    TODO()
}

/**
 * Средняя
 *
 * Вывести в выходной файл процесс умножения столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 111):
   19935
*    111
--------
   19935
+ 19935
+19935
--------
 2212785
 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 * Нули в множителе обрабатывать так же, как и остальные цифры:
  235
*  10
-----
    0
+235
-----
 2350
 *
 */
fun printMultiplicationProcess(lhv: Int, rhv: Int, outputName: String) {
    TODO()
}


/**
 * Очень сложная
 *
 * Вывести в выходной файл процесс деления столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 22):
  19935 | 22
 -198     906
 ----
    13
    -0
    --
    135
   -132
   ----
      3

 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 *
 */
fun printDivisionProcess(lhv: Int, rhv: Int, outputName: String) {
    TODO()
}


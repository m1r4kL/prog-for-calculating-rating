import com.mongodb.client.MongoCollection
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.litote.kmongo.eq
import org.litote.kmongo.setValue
import java.awt.Desktop
import java.io.File
import kotlin.math.roundToInt

fun <T> loadDataToDatabaseFromJson(path: String, collection: MongoCollection<T>, serializer: KSerializer<List<T>>) {
    val json = File(path).readText()
    val col = Json.decodeFromString(serializer, json)
    collection.drop()
    collection.insertMany(col)
}

fun loadDataToMongoDB() {
    print("Загрузить документы в базу данных \"MongoDB\"? (Да/Нет): ")
    if (readln().lowercase() == "да") {
        loadDataToDatabaseFromJson(pathToJsonStudents, mStudent, ListSerializer(Student.serializer()))
        loadDataToDatabaseFromJson(pathToJsonTasks, mTask, ListSerializer(Task.serializer()))
        loadDataToDatabaseFromJson(pathToJsonRatings, mRating, ListSerializer(Rating.serializer()))
        println("Данные успешно загружены в MongoDB.")
    }
}

fun printTasksAndWeights() {
    mRating.find().forEach { rating ->
        print("\n")
        println("Дисциплина - \"${rating.subject}\"")
        rating.formula.forEach {
            println("Задание - \"${it.key}\", вес задания - ${it.value}")
        }
    }
}

fun setTaskWeight(subject: String, task: String, weight: Int) {
    val filterSubject = Rating::subject eq subject
    val rating = mRating.find(filterSubject).firstOrNull()
        ?: throw SubjectException("Ошибка: дисциплина: \"$subject\" не найдена.")
    if (rating.formula[task] == null) {
        throw TaskException("Ошибка: задание: \"$task\" не найдено.")
    }
    rating.formula[task] = weight
    mRating.updateOne(
        filterSubject,
        setValue(Rating::formula, rating.formula)
    )
    println("Заданию \"$task\", по дисциплине \"$subject\", задан вес - $weight.")
}

fun printStudentRating() {
    print("\n")
    println("Рейтинг студентов")
    mStudent.find().forEach { student ->
        println("Студент - \"${student.name}\", группа - \"${student.group}\", рейтинг - ${student.rating}")
    }
}

fun calculateAndSetStudentRating(studentName: String) {
    val filterName = Student::name eq studentName
    val student = mStudent.find(filterName).firstOrNull()
        ?: throw StudentException("Ошибка: студент \"$studentName\" не найден.")
    var totalRating = 0
    var studentRating = 0
    mTask.find().forEach { task ->
        val grade = task.students[studentName]!!
        val weight = mRating.find().first { rating -> rating.formula.containsKey(task.task) }.formula[task.task]!!
        studentRating += grade * weight // Рейтинг студента
        totalRating += 5 * weight // Максимальный рейтинг
    }
    val finalRating = ((studentRating.toDouble() / totalRating.toDouble()) * 100).roundToInt()
    mStudent.updateOne(
        filterName, setValue(Student::rating, finalRating)
    )
    println("Студенту \"${student.name}\", группа \"${student.group}\" выставлен рейтинг $finalRating.")
}

fun createRatingHTMLPage() {
    val htmlString = buildString {
        appendHTML().table {
            style = "font-size: 25px; border: 1px solid black; margin: auto"
            caption { +"Таблица рейтингов студентов" }
            tr {
                style = "text-align: center; background-color: black; color: white;"
                td { +"Имя студента" }
                td { +"Группа"}
                td { +"Рейтинг" }
            }
            mStudent.find().forEach { student ->
                tr {
                    style = when (student.rating) {
                        in 0..59 -> {
                            "text-align: center; background-color: red;"
                        }

                        in 60..75 -> {
                            "text-align: center; background-color: yellow;"
                        }

                        in 76..92 -> {
                            "text-align: center; background-color: green;"
                        }

                        else -> {
                            "text-align: center; background-color: lime;"
                        }
                    }
                    td { +student.name }
                    td { +student.group }
                    td { +student.rating.toString() }
                }
            }
        }
    }
    val htmlFile = File(pathToSaveHTMLPage).apply { writeText(htmlString) }
    println("\nHTML страница с результатами рейтингов сформирована по пути: \"$pathToSaveHTMLPage\".")
    print("Открыть HTML страницу? (Да/Нет): ")
    if (readln().lowercase() == "да") {
        println("Открытие...")
        Desktop.getDesktop().browse(htmlFile.toURI())
    }
}
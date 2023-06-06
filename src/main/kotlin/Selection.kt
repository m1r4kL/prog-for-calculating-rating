var exit = false

fun selectionMenu() {

    while (!exit) {
        println("\nВведите действие, которое необходимо выполнить:")
        println("1 - Просмотреть все задачи и их веса.")
        println("2 - Установить вес для определенной задачи.")
        println("3 - Просмотреть студентов и их рейтинг.")
        println("4 - Рассчитать рейтинг для определенного студента (для всех).")
        println("5 - Сформировать html страницу с результатами рейтинга.")
        println("0 - Выход из программы.")
        print("Ввод: ")
        when (readln()) {
            "0" -> {
                println("Выход из программы...")
                exit = true
            }

            "1" -> printTasksAndWeights()

            "2" -> {
                print("Введите название дисциплины: ")
                val subject = readln()
                print("Введите название задания: ")
                val task = readln()
                print("Введите вес задания: ")
                val weight = readln().toIntOrNull() ?: 0
                setTaskWeight(subject, task, weight)
            }

            "3" -> printStudentRating()

            "4" -> {
                print("\nВведите имя студента (введите \"Все\", чтобы посчитать рейтинг для всех студентов): ")
                val studentName = readln()
                when (studentName.lowercase()) {
                    "все" -> {
                        mStudent.find().forEach { student ->
                            calculateAndSetStudentRating(student.name)
                        }
                        println("Всем студентам выставлен рейтинг.")
                    }
                    else -> calculateAndSetStudentRating(studentName)
                }
            }

            "5" -> createRatingHTMLPage()

            else -> throw WrongCommandException
        }
    }
}
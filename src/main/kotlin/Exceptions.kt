open class ProgramExceptions(message: String): Exception(message)

object WrongCommandException: ProgramExceptions("Ошибка: некорректный выбор действия.")

class SubjectException(string: String): ProgramExceptions(string)

class TaskException(string: String): ProgramExceptions(string)

class StudentException(string: String): ProgramExceptions(string)
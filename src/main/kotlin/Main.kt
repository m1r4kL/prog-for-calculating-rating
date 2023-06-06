import com.mongodb.client.MongoCollection
import org.litote.kmongo.getCollection

val mStudent: MongoCollection<Student> = mongoDatabase.getCollection<Student>()
val mTask: MongoCollection<Task> = mongoDatabase.getCollection<Task>()
val mRating: MongoCollection<Rating> = mongoDatabase.getCollection<Rating>()

const val pathToJsonStudents: String = "C:/Users/KhusainovKA/IdeaProjects/Coursework_OOP/" +
        "src/main/kotlin/Database/Students.json"
const val pathToJsonTasks: String = "C:/Users/KhusainovKA/IdeaProjects/Coursework_OOP/" +
        "src/main/kotlin/Database/Tasks.json"
const val pathToJsonRatings: String = "C:/Users/KhusainovKA/IdeaProjects/Coursework_OOP" +
        "/src/main/kotlin/Database/Ratings.json"
const val pathToSaveHTMLPage: String = "C:/Users/KhusainovKA/IdeaProjects/Coursework_OOP" +
        "/src/main/kotlin/Rating.html"

fun main() {
    loadDataToMongoDB()
    do {
        try {
            selectionMenu()
        }
        catch (error: ProgramExceptions) {
            println(error.message)
        }
    } while (!exit)

    client.close()
}
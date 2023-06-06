import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val name: String,
    val age: Int,
    val marks: MutableMap<String, List<Int>>,
    val gpa: Double,
    val rating: Int,
    val group: String
)
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val subject: String,
    val task: String,
    val students: MutableMap<String, Int>
)
import kotlinx.serialization.Serializable

@Serializable
data class Rating(
    val subject: String,
    var formula: MutableMap<String, Int>
)
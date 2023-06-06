import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo

val client = KMongo.createClient("mongodb://localhost:27017")
val mongoDatabase: MongoDatabase = client.getDatabase("Coursework")
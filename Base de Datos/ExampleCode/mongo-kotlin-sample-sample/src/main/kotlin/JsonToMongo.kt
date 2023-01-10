import org.bson.BsonDocument
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object BsonImporter {

    fun resourceToString(resourcePath : String) : String  {
        val jsonPath = BsonImporter::class.java.getResource(resourcePath)
        return fileToString(Paths.get(jsonPath.toURI()));
    }

    fun pathToJson(path : String) : BsonDocument {
        val jsonAsText = fileToString(Paths.get(path))
        return BsonDocument.parse(jsonAsText)
    }

    private fun fileToString(path: Path) : String {
        val lines = Files.readAllLines(path, Charset.forName("UTF-8"));
        val jsonAsText = lines.joinToString(separator = "\n")
        return jsonAsText
    }

    fun resourceToBsonElement(resourcePath: String) : BsonDocument {
        val jsonAsText = resourceToString(resourcePath)
        return BsonDocument.parse(jsonAsText)
    }
}
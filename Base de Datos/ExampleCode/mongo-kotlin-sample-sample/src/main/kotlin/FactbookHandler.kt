import com.mongodb.MongoClient
import com.mongodb.client.AggregateIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Aggregates.addFields
import com.mongodb.client.model.Aggregates.match
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind

import com.mongodb.client.model.Field
import com.mongodb.client.model.Filters.elemMatch
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import org.bson.BsonDocument
import org.bson.BsonInt32
import org.bson.BsonString
import org.bson.Document

object FactbookHandler {

    fun splitIntoSingleJsonObjectsPerCountry(): List<BsonDocument> {
        val factbook = BsonImporter.resourceToBsonElement("/factbook.json")
        val countries = factbook.getDocument("countries")
        val list = countries.map { (_, value) ->
            value.asDocument().getDocument("data")
        }
        return list
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val countriesAsBson = splitIntoSingleJsonObjectsPerCountry()
        val mongoClient = MongoClient()
        writeCountriesToMongo(mongoClient, countriesAsBson)
        val language = "French"
        val allFrenchSpeakingCountries = getAllCountriesSpeakingSpecificLanguage(mongoClient, language)
        allFrenchSpeakingCountries.sortedByDescending { (it.getOrDefault("speakersPercentage", 0) as Number).toDouble() }
                    .forEach {
                        val country = it.getString("country")
                        val percentage = it.getOrDefault("speakersPercentage", "not specified")
                        println("In $country the percentage of people speaking $language is $percentage")
                    }
        getPopulationOfAndorra()
    }

    private fun writeCountriesToMongo(mongoClient: MongoClient, countriesAsBsonDocuments: List<BsonDocument>) {
        val database : MongoDatabase = mongoClient.getDatabase("factbook")
        val collection : MongoCollection<BsonDocument> = database.getCollection("countries", BsonDocument::class.java)
        // clear collection
        collection.drop()
        collection.insertMany(countriesAsBsonDocuments)
    }

    private fun getAllCountriesSpeakingSpecificLanguage(mongoClient: MongoClient, language: String): List<Document> {
        val database = mongoClient.getDatabase("factbook")
        val collection = database.getCollection("countries")
        val aggregate: AggregateIterable<Document> = collection.aggregate(mutableListOf(
                match(elemMatch("people.languages.language", eq("name", language))),
                unwind("\$people.languages.language"),
                project(Document("name", 1).append("people.languages", 1).append("_id", 0)),
                match(eq("people.languages.language.name", language)),
                addFields(mutableListOf(
                        Field("country", "\$name"),
                        Field("speakersPercentage", "\$people.languages.language.percent")
                ) as List<Field<*>>?),
                project(Document("country", 1).append("speakersPercentage", 1))
        ));
        return aggregate.toList()
    }

    fun getPopulationOfAndorra() {
        val mongoClient = MongoClient()
        val database = mongoClient.getDatabase("factbook")
        val collection = database.getCollection("countries", BsonDocument::class.java)
        val populationOfAndorra = collection
                .find(eq("name", "Andorra"))
                .projection(fields(include("people.population.total")))
        println("The population of Andorra is: ${populationOfAndorra.first().getDocument("people").getDocument("population").getInt32("total").value}")
    }
}

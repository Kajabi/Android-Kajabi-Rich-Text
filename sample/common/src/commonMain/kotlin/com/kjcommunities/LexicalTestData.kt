package com.kjcommunities

import com.github.kajabi.common.generated.resources.Res
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlinx.serialization.json.Json

/**
 * Helper object to load lexical test data from resources
 */
object LexicalTestData {
    
    private var cachedMinifiedJson: String? = null
    private var cachedPrettyJson: String? = null
    
    // Separate cache for test data 2
    private var cachedMinifiedJson2: String? = null
    private var cachedPrettyJson2: String? = null
    
    @OptIn(ExperimentalResourceApi::class)
    private suspend fun loadTestData(): Pair<String, String> {
        if (cachedMinifiedJson == null || cachedPrettyJson == null) {
            val jsonString = Res.readBytes("files/lexical_test_data.json").decodeToString()
            
            // Extract the minified JSON (between the first set of quotes)
            val minifiedStart = jsonString.indexOf("\"minified_test_json\": \"") + "\"minified_test_json\": \"".length
            val minifiedEnd = jsonString.indexOf("\",", minifiedStart)
            cachedMinifiedJson = jsonString.substring(minifiedStart, minifiedEnd)
            
            // Extract the pretty JSON (between the second set of quotes)
            val prettyStart = jsonString.indexOf("\"pretty_test_json\": \"") + "\"pretty_test_json\": \"".length
            val prettyEnd = jsonString.lastIndexOf("\"")
            cachedPrettyJson = jsonString.substring(prettyStart, prettyEnd)
                .replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\")
        }
        return Pair(cachedMinifiedJson!!, cachedPrettyJson!!)
    }
    
        
    @OptIn(ExperimentalResourceApi::class)
    private suspend fun loadTestData2(): Pair<String, String> {
        if (cachedMinifiedJson2 == null || cachedPrettyJson2 == null) {
            val jsonString = Res.readBytes("files/lexical_test_data2.json").decodeToString()
            
            // The file contains direct lexical JSON, so use it as minified
            cachedMinifiedJson2 = jsonString
            
            // Create a pretty-printed version using kotlinx.serialization
            try {
                val json = Json { prettyPrint = true }
                val jsonElement = Json.parseToJsonElement(jsonString)
                cachedPrettyJson2 = json.encodeToString(kotlinx.serialization.json.JsonElement.serializer(), jsonElement)
            } catch (e: Exception) {
                // If pretty printing fails, use the original
                cachedPrettyJson2 = jsonString
            }
        }
        return Pair(cachedMinifiedJson2!!, cachedPrettyJson2!!)
    }
    

    /**
     * Get the minified test JSON string
     */
    fun getMinifiedTestJson(): String {
        return runBlocking {
            loadTestData().first
        }
    }
    
    /**
     * Get the pretty-formatted test JSON string
     */
    fun getPrettyTestJson(): String {
        return runBlocking {
            loadTestData().second
        }
    }
    
    /**
     * Get the minified test JSON string from data set 2
     */
    fun getMinifiedTestJson2(): String {
        return runBlocking {
            loadTestData2().first
        }
    }
    
    /**
     * Get the pretty-formatted test JSON string from data set 2
     */
    fun getPrettyTestJson2(): String {
        return runBlocking {
            loadTestData2().second
        }
    }
} 
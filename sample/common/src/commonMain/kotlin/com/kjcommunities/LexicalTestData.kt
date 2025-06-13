package com.kjcommunities

import com.mohamedrejeb.richeditor.common.generated.resources.Res
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi

/**
 * Helper object to load lexical test data from resources
 */
object LexicalTestData {
    
    private var cachedMinifiedJson: String? = null
    private var cachedPrettyJson: String? = null
    
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
} 
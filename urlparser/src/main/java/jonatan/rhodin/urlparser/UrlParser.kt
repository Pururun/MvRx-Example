package jonatan.rhodin.urlparser

import java.net.MalformedURLException

/**
 *
 */
fun parseUrlString(url: String): Map<String, String> {
    var startIndex = 0
    val map = mutableMapOf<String, String>()

    val scheme = url.substringBefore("://", "")
    if (scheme.isEmpty()) {
        throw MalformedURLException("Missing scheme!")
    }
    map["scheme"] = scheme
    //Move to first index after scheme
    startIndex = url.indexOf("://") + 3

    if (startIndex > url.lastIndex) {
        //No more data, just return
        return map
    }

    val userInfo = url.substringBefore("@", "", startIndex).split(":")
    val user = userInfo[0]
    val pass = if (userInfo.size > 1) userInfo[1] else ""
    if (user.isNotEmpty()) {
        map["user"] = user
        //We have some kind of userinfo, move the index forward
        startIndex = url.indexOf("@") + 1
    }
    if (pass.isNotEmpty()) {
        map["pass"] = pass
    }

    val host = url.substringBeforeAny(listOf("/", "?", "#"), url.substring(startIndex), startIndex)

    if (host.isNotEmpty()) {
        map["host"] = host
        startIndex = url.indexOfAny(listOf("/", "?", "#"), startIndex)
    }

    if (startIndex == -1 || startIndex > url.lastIndex) {
        //No more data
        return map
    }

    var lastIndex = url.length
    val fragment = url.substringAfterLast("#", "")
    if (fragment.isNotEmpty()) {
        map["fragment"] = fragment
        lastIndex = url.indexOf("#")
    }

    val query = url.substringAfterLast("?", "", lastIndex)
    if (query.isNotEmpty()) {
        map["query"] = query
        lastIndex = url.indexOf("?")
    }

    //Path is everything between the end of host and before any query or fragment
    val path = url.substring(startIndex, lastIndex)
    if (path.isNotEmpty()) {
        map["path"] = path
    }

    return map
}

fun parseQuery(query: String): Map<String, String> {
    val map = mutableMapOf<String, String>()
    query.split("&").forEach { item ->
        val pair = item.split("=").zipWithNext()
        map.plusAssign(pair)
    }
    return map
}

/**
 *
 * An implementation of Strings.substringBefore that takes a start index as well (Default implementation only has 0 as start index)
 *
 */
private fun String.substringBefore(delimiter: String, missingDelimiterValue: String = this, startIndex: Int): String {
    val index = indexOf(delimiter, startIndex)
    return if (index == -1) missingDelimiterValue else substring(startIndex, index)
}

/**
 *
 * Gets a substring before the first instance of any of the delimiters, starting from startIndex
 *
 */
private fun String.substringBeforeAny(
    delimiters: Collection<String>,
    missingDelimiterValue: String = this,
    startIndex: Int
): String {
    val index = indexOfAny(delimiters, startIndex)
    return if (index == -1) missingDelimiterValue else substring(startIndex, index)
}

/**
 *
 * An implementation of Strings.substringAfterLast that takes an exclusive end index as well
 *
 */
private fun String.substringAfterLast(delimiter: String, missingDelimiterValue: String = this, endIndex: Int): String {
    val index = lastIndexOf(delimiter)
    return if (index == -1) missingDelimiterValue else substring(index + delimiter.length, endIndex)
}
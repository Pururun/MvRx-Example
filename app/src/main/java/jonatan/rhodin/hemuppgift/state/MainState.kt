package jonatan.rhodin.hemuppgift.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized

/**
 *
 * Should probably be broken down into substates
 *
 */
data class MainState(
    val request : Async<Map<String, String>> = Uninitialized,
    val urlError : Boolean = false,
    val urlResult: Map<String, String> = emptyMap(),
    val queryError : Boolean = false,
    val queryResult: Map<String, String> = emptyMap(),
    val query: String = "")
    : MvRxState
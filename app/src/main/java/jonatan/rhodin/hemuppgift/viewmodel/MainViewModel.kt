package jonatan.rhodin.hemuppgift.viewmodel

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.Loading
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import jonatan.rhodin.hemuppgift.state.MainState
import jonatan.rhodin.urlparser.parseQuery
import jonatan.rhodin.urlparser.parseUrlString

class MainViewModel(initialState: MainState) :
    BaseMvRxViewModel<MainState>(initialState, false) {

    fun parseUrl(url: String) = withState { state ->
        if (state.request is Loading) {
            return@withState
        }

        Observable.fromCallable { parseUrlString(url) }.subscribeOn(Schedulers.computation()).execute {
            val result = it() ?: emptyMap()
            copy(
                request = it,
                urlError = result.isEmpty(),
                urlResult = result,
                query = ""
            )
        }
    }

    fun parseUrlQuery(query: String) = withState { state ->
        if (state.request is Loading) {
            return@withState
        }

        Observable.fromCallable { parseQuery(query) }.subscribeOn(Schedulers.computation()).execute {
            val result = it() ?: emptyMap()
            copy(
                request = it,
                queryError = result.isEmpty(),
                queryResult = result,
                query = ""
            )
        }
    }

    fun copyQuery(url: String) = withState { state ->
        if (state.request is Loading) {
            return@withState
        }

        Observable.fromCallable { parseUrlString(url) }.subscribeOn(Schedulers.computation()).execute {
            val query = it()?.get("query") ?: ""
            copy(
                request = it,
                query = query
            )
        }
    }
}
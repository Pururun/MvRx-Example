package jonatan.rhodin.hemuppgift.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import jonatan.rhodin.hemuppgift.R
import jonatan.rhodin.hemuppgift.viewmodel.MainViewModel

class MainFragment : BaseMvRxFragment() {

    private val urlInputView: EditText by lazy {
        find<EditText>(R.id.url_input)
    }
    private val queryInputView: EditText by lazy {
        find<EditText>(R.id.query_input)
    }

    private val parseUrlButton: Button by lazy {
        find<Button>(R.id.parse_url)
    }
    private val parseQueryButton: Button by lazy {
        find<Button>(R.id.parse_query)
    }
    private val copyFromUrlButton: Button by lazy {
        find<Button>(R.id.copy_from_url)
    }

    private val parseUrlResultLayout: ViewGroup by lazy {
        find<ViewGroup>(R.id.parse_url_result)
    }

    private val parseQueryResult: ViewGroup by lazy {
        find<ViewGroup>(R.id.parse_query_result)
    }

    private val viewModel: MainViewModel by fragmentViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parseUrlButton.setOnClickListener {
            viewModel.parseUrl(urlInputView.text?.toString() ?: "")
        }

        parseQueryButton.setOnClickListener {
            viewModel.parseUrlQuery(queryInputView.text?.toString() ?: "")
        }

        copyFromUrlButton.setOnClickListener {
            viewModel.copyQuery(urlInputView.text?.toString() ?: "")
        }
    }

    override fun invalidate() {
        withState(viewModel) { state ->
            parseUrlResultLayout.removeAllViews()
            if (!state.urlError) {
                state.urlResult.forEach { entry ->
                    parseUrlResultLayout.addView(
                        TextView(context).apply {
                            text = getString(R.string.key_to_value, entry.key, entry.value)
                        }
                    )
                }
            } else {
                parseUrlResultLayout.addView(
                    TextView(context).apply {
                        setText(R.string.url_error)
                    }
                )
            }

            parseQueryResult.removeAllViews()
            if (!state.queryError) {
                state.queryResult.entries.forEach { entry ->
                    parseQueryResult.addView(
                        TextView(context).apply {
                            text = getString(R.string.key_to_value, entry.key, entry.value)
                        }
                    )
                }
            } else {
                parseQueryResult.addView(
                    TextView(context).apply {
                        setText(R.string.query_error)
                    }
                )
            }

            if (state.query.isNotEmpty()) {
                queryInputView.setText(state.query)
            }

        }
    }

    inline fun <reified T : View> Fragment.find(id: Int): T = view?.findViewById(id) as T
}
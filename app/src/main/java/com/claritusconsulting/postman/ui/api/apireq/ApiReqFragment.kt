package com.claritusconsulting.postman.ui.api.apireq

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*

import com.claritusconsulting.postman.R
import com.claritusconsulting.postman.data.ApiRequest
import com.claritusconsulting.postman.di.Injectable
import com.claritusconsulting.postman.ui.api.ApiFragment
import com.claritusconsulting.postman.ui.api.ApiViewModel
import kotlinx.android.synthetic.main.fragment_api_req.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import javax.inject.Inject


class ApiReqFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var okHttpClient: OkHttpClient
    lateinit var apiViewModel: ApiViewModel
    val currentReq: ApiRequest = ApiRequest()
    private var headerBool = false
    private var bodyBool = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_api_req, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initSpinners()
        val apiFragment = parentFragment as ApiFragment
        apiViewModel = ViewModelProviders.of(apiFragment, viewModelFactory)
                .get(ApiViewModel::class.java)
        apiViewModel.getReq().observe(this, Observer {
            if (currentReq.url == "") {
                val occ = it?.url?.indexOf('/') ?: -1
                if (occ != -1) {
                    val editedUrl = it?.url?.subSequence(occ + 2, it.url.length)
                    urlEdit.setText(editedUrl)
                }
                var c = 0
                for ((key, value) in it?.apiHeader.orEmpty()) {
                    if (c > 0) {
                        headerTable.addView(generateTableRow(key, value))
                    } else {
                        key1.setText(key)
                        val1.setText(value)
                    }
                    c++
                }
                c = 0
                for ((key, value) in it?.apiBody.orEmpty()) {
                    if (c > 0) {
                        bodyTable.addView(generateTableRow(key, value))
                    } else {
                        bkey1.setText(key)
                        bval1.setText(value)
                    }
                    c++
                }
                when (it?.method) {
                    "GET" -> methodSpinner.setSelection(0)
                    "POST" -> methodSpinner.setSelection(1)
                }
            }
//            rawText.setText()
        })
        clickListeners()
    }

    private fun initSpinners() {
        val aa = ArrayAdapter(activity, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.method))
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        methodSpinner.adapter = aa
        methodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view1: View?, position: Int, id: Long) {
                when(position) {
                    0->{
                        bodyCardView.visibility= View.INVISIBLE
                        bodyContent.visibility = View.GONE
                        bodyUp.visibility = View.GONE
                        bodydown.visibility = View.VISIBLE
                        bodyBool = true
                    }
                    1->{
                        bodyCardView.visibility = View.VISIBLE
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        val httpAdapter = ArrayAdapter(activity,android.R.layout.simple_spinner_item,resources.getStringArray(R.array.http))
        httpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        httpSpinner.adapter = httpAdapter

        val bodySpinnerAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.body_type))
        bodySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bodySpinner.adapter = bodySpinnerAdapter
        bodySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view1: View?, position: Int, id: Long) {
                when(position) {
                    0 -> {
                        bodyForm.visibility = View.GONE
                        bodyRaw.visibility = View.VISIBLE
                    }
                    1 -> {
                        bodyForm.visibility = View.VISIBLE
                        bodyRaw.visibility = View.GONE
                    }
                }
            }
        }

        val raw = resources.getStringArray(R.array.raw_type)
        val rawSpinnerAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, raw)
        rawSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rawTypeSpinner.adapter = rawSpinnerAdapter
        methodSpinner.setSelection(0)
        bodySpinner.setSelection(0)
        rawTypeSpinner.setSelection(0)
    }

    private fun clickListeners() {
        headerTop.setOnClickListener{
            if (!headerBool) {
                headerContent.visibility = View.GONE
                headerUp.visibility = View.GONE
                headerDown.visibility = View.VISIBLE
                headerBool = true
            } else {
                headerContent.visibility = View.VISIBLE
                headerUp.visibility = View.VISIBLE
                headerDown.visibility = View.GONE
                headerBool = false
            }
        }

        bodyTop.setOnClickListener{
            if (!bodyBool) {
                bodyContent.visibility = View.GONE
                bodyUp.visibility = View.GONE
                bodydown.visibility = View.VISIBLE
                bodyBool = true
            } else {
                bodyContent.visibility = View.VISIBLE
                bodyUp.visibility = View.VISIBLE
                bodydown.visibility = View.GONE
                bodyBool = false
            }
        }

        addHeader.setOnClickListener {
            val lastRow = headerTable.getChildAt(headerTable.childCount-1) as TableRow
            val e1 = lastRow.getChildAt(0) as EditText
            val e2 = lastRow.getChildAt(1) as EditText
            if (e1.text.toString().isBlank() || e2.text.toString().isBlank()) {
                headerError.visibility = View.VISIBLE
            } else {
                headerError.visibility = View.GONE
                val t1 = generateTableRow("","")
                headerTable.addView(t1)
            }
        }

        addBody.setOnClickListener {
            val lastRow = bodyTable.getChildAt(bodyTable.childCount-1) as TableRow
            val e1 = lastRow.getChildAt(0) as EditText
            val e2 = lastRow.getChildAt(1) as EditText
            if (e1.text.toString().isBlank() || e2.text.toString().isBlank()) {
                bodyFormError.visibility = View.VISIBLE
            } else {
                bodyFormError.visibility = View.GONE
                val t1 = generateTableRow("","")
                bodyTable.addView(t1)
            }
        }

        sendReq.setOnClickListener{
            val v = activity?.currentFocus
            if (v != null) {
                val im = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                im.hideSoftInputFromWindow(v.windowToken,0)
            }
            val url = httpSpinner.selectedItem.toString() + urlEdit.text.toString()
            val headerBuilder = Headers.Builder()
            val headerMap = mutableMapOf<String,String>()
            val request:Request
            for (i in 0 until headerTable.childCount) {
                val row = headerTable.getChildAt(i) as TableRow
                val k = row.getChildAt(0) as EditText
                val v = row.getChildAt(1) as EditText
                val kText = k.text.toString()
                val vText = v.text.toString()
                if (!kText.isBlank() && !vText.isBlank()) {
                    headerBuilder.add(k.text.toString(),v.text.toString())
                    headerMap[kText] = vText
                }
            }
            if (validInputs()) {
                when(methodSpinner.selectedItemPosition) {
                    0 -> {//GET request
                        currentReq.apiHeader = headerMap
                        currentReq.url = url
                        currentReq.method = "GET"
                        request = Request.Builder().headers(headerBuilder.build()).url(url).build()
                        callReq(request)
                        apiViewModel.setChange(true)
                    }
                    1 -> {
                        if (bodySpinner.selectedItemPosition == 0) {
                            val formBuilder = FormBody.Builder()
                            for (i in 0 until bodyTable.childCount) {
                                val tr = bodyTable.getChildAt(i) as TableRow
                                val key = tr.getChildAt(0) as EditText
                                val value = tr.getChildAt(1) as EditText
                                if (!key.text.toString().isEmpty() && !value.text.toString().isEmpty())
                                    formBuilder.add(key.text.toString(), value.text.toString())
                            }
                            request = Request.Builder()
                                    .headers(headerBuilder.build())
                                    .url(url)
                                    .post(formBuilder.build())
                                    .build()
                            callReq(request)
                        } else {
                            val media:MediaType = if (bodySpinner.selectedItemPosition == 0) {
                                MediaType.parse("application/json; charset=utf-8")!!
                            } else {
                                MediaType.parse("application/xml; charset=utf-8")!!
                            }
                            request = Request.Builder()
                                    .headers(headerBuilder.build())
                                    .url(url)
                                    .post(RequestBody.create(media, rawText.text.toString()))
                                    .build()
                            callReq(request)
                        }
                    }
                }
            }
        }
    }

    private fun validInputs(): Boolean {
        if (urlEdit.text.isBlank()) {
            urlEdit.error = "Please fill this field"
            return false
        }
        try {
            URL(httpSpinner.selectedItem.toString()+urlEdit.text.toString())
        } catch (e:Exception) {
            urlEdit.error = "Incorrect URL"
            return false
        }
        if (methodSpinner.selectedItemPosition==2
            && bodySpinner.selectedItemPosition == 0
            && rawTypeSpinner.selectedItemPosition == 0) {
            try{
                JSONObject(rawText.text.toString())
            } catch (e:JSONException) {
                try {
                    JSONArray(rawText.text.toString())
                } catch (e:JSONException) {
                    rawText.error = "Invalid Json"
                    return false
                }
            }
        }
        urlEdit.error = null
        rawText.error = null
        return true
    }

    private fun callReq(request: Request) {
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                apiViewModel.failedRequest(currentReq.copy(requestId = null))
            }
            override fun onResponse(call: Call?, response: Response?) {
                if (response != null) {
                    if (response.isSuccessful) {
                        apiViewModel.successResponse(response,currentReq)
                    }
                }
            }
        })
    }

    private fun generateTableRow(key:String, value:String): TableRow {
        val t1 = TableRow(activity)
        val e1 = EditText(activity)
        val e2 = EditText(activity)
        val b1 = ImageButton(activity)
        t1.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT)
        e1.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT)
        e1.hint = "Key"
        if (key!="") e1.setText(key)
        e2.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT)
        e2.hint = "Value"
        if (value!="") e2.setText(value)
        b1.setImageResource(R.drawable.ic_action_remove)
        b1.setBackgroundResource(android.R.drawable.screen_background_light_transparent)
        b1.setPadding(36,4,36,0)
        t1.addView(e1)
        t1.addView(e2)
        t1.addView(b1)
        b1.setOnClickListener {
            val row = it.parent as View
            val container = row.parent as ViewGroup
            container.removeView(row)
            container.invalidate()
        }
        return t1
    }
}

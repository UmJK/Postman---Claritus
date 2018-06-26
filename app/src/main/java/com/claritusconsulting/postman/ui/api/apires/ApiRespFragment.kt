package com.claritusconsulting.postman.ui.api.apires


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView

import com.claritusconsulting.postman.R
import com.claritusconsulting.postman.data.ApiResponse
import com.claritusconsulting.postman.di.Injectable
import com.claritusconsulting.postman.ui.api.ApiFragment
import com.claritusconsulting.postman.ui.api.ApiViewModel
import kotlinx.android.synthetic.main.fragment_api_resp.*
import timber.log.Timber
import javax.inject.Inject

class ApiRespFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var apiViewModel: ApiViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_api_resp, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val apiFragment = parentFragment as ApiFragment
        apiViewModel = ViewModelProviders.of(apiFragment, viewModelFactory)
                .get(ApiViewModel::class.java)
        apiViewModel.getResponse().observe(this, Observer {
            Timber.d(it.toString())
            responseProgress.visibility = View.GONE
            if (it?.code == -1) {
                noResponseText.visibility = View.VISIBLE
                noResponseText.text = getString(R.string.cant_connect_host)
            } else {
                updateUi(it)
                responseContainer.visibility = View.VISIBLE
            }
        })
        apiViewModel.getChange().observe(this, Observer {
            Timber.d(it.toString())
            if (it == true) {
                responseProgress.visibility = View.VISIBLE
                responseContainer.visibility = View.GONE
                noResponseText.visibility = View.GONE
                apiViewModel.setChange(false)
            }
        })
    }

    private fun updateUi(it: ApiResponse?) {
        url.text = it?.url
        code.text = it?.code.toString()
        response.text = it?.responseTxt
        for (header in it?.responseHeader.orEmpty()) {
            val t1 = TableRow(activity)
            val e1 = TextView(activity)
            val e2 = TextView(activity)
            t1.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
            e1.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
            e1.text = header.key
            e2.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
            e2.text= header.value
            t1.addView(e1)
            t1.addView(e2)
            respHeaderTable.addView(t1)
        }
    }
}
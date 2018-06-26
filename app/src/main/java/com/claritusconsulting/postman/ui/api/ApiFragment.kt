package com.claritusconsulting.postman.ui.api


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.claritusconsulting.postman.R
import com.claritusconsulting.postman.di.Injectable
import com.claritusconsulting.postman.ui.adapters.ApiPagerAdapter
import com.claritusconsulting.postman.ui.api.apireq.ApiReqFragment
import com.claritusconsulting.postman.ui.api.apires.ApiRespFragment
import kotlinx.android.synthetic.main.fragment_api.*
import javax.inject.Inject

class ApiFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var apiViewModel: ApiViewModel
    var requestId: Long? = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        requestId = arguments?.getString("id")?.toLong()
        return inflater.inflate(R.layout.fragment_api, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        apiViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ApiViewModel::class.java)
        if (requestId != null) apiViewModel.setReqId(requestId!!)
        apiViewModel.getChange().observe(this, Observer {
            if (it==true) {
                api_pager.currentItem = 1
            }
        })
        setupViewPager(api_pager)
        api_tabs.setupWithViewPager(api_pager)
    }

    private fun setupViewPager(main_pager: ViewPager?) {
        val pagerAdapter = ApiPagerAdapter(childFragmentManager)
        val f1 = ApiReqFragment()
        val f2 = ApiRespFragment()
        val bundle = Bundle()
        bundle.putString("id",requestId.toString())
        f1.arguments = bundle
        f2.arguments = bundle
        pagerAdapter.addFragment(f1,"Request")
        pagerAdapter.addFragment(f2,"Response")
        main_pager?.adapter = pagerAdapter
    }
}

package com.claritusconsulting.postman.ui.main


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
import com.claritusconsulting.postman.ui.adapters.PostPagerAdapter
import com.claritusconsulting.postman.ui.api.ApiFragment
import com.claritusconsulting.postman.ui.main.history.HistoryFragment
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject


class MainFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var mainViewModel:MainViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_main, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager(main_pager)
        main_tabs.setupWithViewPager(main_pager)
        addReqFab.setOnClickListener{
            val apiReqFragment = ApiFragment()
            activity!!.supportFragmentManager.beginTransaction().add(R.id.main_frame,apiReqFragment).addToBackStack("main").commitAllowingStateLoss()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MainViewModel::class.java)
    }

    private fun setupViewPager(main_pager: ViewPager?) {
        val postPagerAdapter = PostPagerAdapter(activity?.supportFragmentManager)
        val h1 = HistoryFragment()
        postPagerAdapter.addFragment(h1,"History")
//        postPagerAdapter.addFragment(h1,"Collection")
        main_pager?.adapter = postPagerAdapter
    }
}

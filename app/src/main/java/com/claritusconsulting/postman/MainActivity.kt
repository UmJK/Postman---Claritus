package com.claritusconsulting.postman

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.claritusconsulting.postman.ui.main.MainFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(),HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            val mainFragment = MainFragment()
            supportFragmentManager.beginTransaction().add(R.id.main_frame, mainFragment).commitAllowingStateLoss()
        }
    }



    override fun supportFragmentInjector() = dispatchingAndroidInjector
}

/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.claritusconsulting.postman.di

import com.claritusconsulting.postman.ui.api.ApiFragment
import com.claritusconsulting.postman.ui.api.apireq.ApiReqFragment
import com.claritusconsulting.postman.ui.api.apires.ApiRespFragment
import com.claritusconsulting.postman.ui.main.history.HistoryFragment
import com.claritusconsulting.postman.ui.main.MainFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeHistoryFragment(): HistoryFragment
    @ContributesAndroidInjector
    abstract fun contributeApiReqFragment(): ApiReqFragment
    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment
    @ContributesAndroidInjector
    abstract fun contributeRespFragment(): ApiRespFragment
    @ContributesAndroidInjector
    abstract fun contributeApiFragment(): ApiFragment
}

package com.claritusconsulting.postman.ui.main.history

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.claritusconsulting.postman.data.ApiRequest
import com.claritusconsulting.postman.repo.ApiRepo
import javax.inject.Inject

class HistoryViewModel @Inject constructor(private val apiRepo: ApiRepo) : ViewModel() {
    fun getHistory(): LiveData<List<ApiRequest>> {
        return apiRepo.loadRequests()
    }

    fun removeEntry(apiRequest: ApiRequest) {
        apiRepo.deleteRequest(apiRequest)
    }
}
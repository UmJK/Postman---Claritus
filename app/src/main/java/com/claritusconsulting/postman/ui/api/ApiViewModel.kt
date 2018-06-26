package com.claritusconsulting.postman.ui.api

import android.arch.lifecycle.*
import com.claritusconsulting.postman.data.ApiRequest
import com.claritusconsulting.postman.data.ApiResponse
import com.claritusconsulting.postman.repo.ApiRepo
import okhttp3.Response
import javax.inject.Inject

class ApiViewModel @Inject constructor(private val apiRepo: ApiRepo) : ViewModel() {
    private val changeLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val requestId = MediatorLiveData<Long>()
    private val responseLiveData: LiveData<ApiResponse> = Transformations.switchMap(requestId) {
        apiRepo.getResponse(it)
    }
    private val requestLiveData: LiveData<ApiRequest> = Transformations.switchMap(requestId){
        apiRepo.getRequest(it)
    }

    fun setReqId(id: Long) {
        requestId.postValue(id)
    }

    fun getReq(): LiveData<ApiRequest> {
        return requestLiveData
    }
    fun setChange(bool: Boolean) {
        changeLiveData.value = bool
    }

    fun getChange(): LiveData<Boolean> {
        return changeLiveData
    }

    fun getResponse(): LiveData<ApiResponse> {
        return responseLiveData
    }

    fun failedRequest(apiRequest: ApiRequest) {
        insertReqRes(apiRequest,ApiResponse().copy(code = -1))
    }

    fun successResponse(response: Response, currentReq: ApiRequest) {
        val body = response.body()?.string()
        val apiResponse = ApiResponse()
        apiResponse.code = response.code()
        apiResponse.method = currentReq.method
        val respHeaders = mutableMapOf<String,String>()
        val headers = response.headers()
        for (i in 0 until headers.size()) {
            respHeaders[headers.name(i)]= headers.value(i)
        }
        apiResponse.responseHeader = respHeaders
        apiResponse.responseTxt = body.orEmpty()
        apiResponse.url = currentReq.url
        insertReqRes(currentReq,apiResponse)
    }

    private fun insertReqRes(apiRequest: ApiRequest, apiResponse: ApiResponse) {
        val apiReqId = apiRepo.addReq(apiRequest, apiResponse)
        requestId.addSource(apiReqId, {
            requestId.removeSource(apiReqId)
            requestId.value = it
        })
    }
}
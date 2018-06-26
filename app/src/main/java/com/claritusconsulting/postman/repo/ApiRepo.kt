package com.claritusconsulting.postman.repo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.claritusconsulting.postman.AppExecutors
import com.claritusconsulting.postman.data.ApiRequest
import com.claritusconsulting.postman.data.ApiResponse
import com.claritusconsulting.postman.db.PostmanDb
import com.claritusconsulting.postman.db.RequestDao
import com.claritusconsulting.postman.db.ResponseDao
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class ApiRepo @Inject constructor(
        private val appExecutors: AppExecutors,
        private val reqDao: RequestDao,
        private val resDao: ResponseDao,
        private val postmanDb: PostmanDb
) {
    fun loadRequests(): LiveData<List<ApiRequest>> {
        return reqDao.findAll()
    }
    private fun insertRequest(apiRequest: ApiRequest): Long {
        return reqDao.insert(apiRequest)
    }

    fun deleteRequest(apiRequest: ApiRequest) {
        appExecutors.diskIO().execute {
            //Run in transaction
            postmanDb.runInTransaction({
                reqDao.deleteRequest(apiRequest)
                resDao.deleteResponse(resDao.findByIdSync(apiRequest.requestId!!))
            })
        }
    }

    fun getResponse(it: Long): LiveData<ApiResponse>? {
        return resDao.findById(it)
    }

    fun getRequest(it: Long): LiveData<ApiRequest>? {
        return reqDao.findById(it)
    }

    private fun insertResponse(response: ApiResponse) {
        resDao.insert(response)
    }

    fun addReq(apiRequest: ApiRequest,apiResponse: ApiResponse): LiveData<Long> {
        val mutableLiveData = MutableLiveData<Long>()
        appExecutors.diskIO().execute {
            val id = insertRequest(apiRequest)
            apiResponse.requestId = id
            insertResponse(apiResponse)
            mutableLiveData.postValue(id)
        }
        return mutableLiveData
    }
}
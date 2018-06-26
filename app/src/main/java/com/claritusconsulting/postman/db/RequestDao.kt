package com.claritusconsulting.postman.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.claritusconsulting.postman.data.ApiRequest

@Dao
interface RequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(apiRequest: ApiRequest) : Long

    @Query("SELECT * FROM apirequest")
    fun findAll(): LiveData<List<ApiRequest>>

    @Query("SELECT * FROM apirequest WHERE requestId = :id")
    fun findById(id: Long): LiveData<ApiRequest>

    @Delete
    fun deleteRequest(request: ApiRequest)
}

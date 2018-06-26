package com.claritusconsulting.postman.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.claritusconsulting.postman.data.ApiResponse

@Dao
interface ResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(apiResponse: ApiResponse) : Long

    @Query("SELECT * FROM apiresponse WHERE requestId = :id")
    fun findById(id:Long): LiveData<ApiResponse>

    @Query("SELECT * FROM apiresponse WHERE requestId = :id")
    fun findByIdSync(id:Long): ApiResponse

    @Delete
    fun deleteResponse(response: ApiResponse)
}

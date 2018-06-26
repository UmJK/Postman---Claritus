package com.claritusconsulting.postman.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class  ApiRequest(
        @PrimaryKey(autoGenerate = true)
        var requestId: Long?,
        var url: String,
        var method: String,
        var apiHeader: Map<String,String>,
        var apiBody: Map<String,String>
){
    constructor():this(null,"","", emptyMap(), emptyMap())
}
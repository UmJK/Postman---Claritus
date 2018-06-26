package com.claritusconsulting.postman.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class  ApiResponse(
        @PrimaryKey(autoGenerate = true)
        var responseId: Long?,
        var requestId: Long?,
        var url: String,
        var method: String,
        var responseHeader: Map<String,String>,
        var code: Int?,
        var responseTxt: String
){
    constructor():this(null,null,"","", emptyMap(),null,"")
}
package com.leomarkpaway.kotlin_coroutine.data.source.remote.dto


import com.google.gson.annotations.SerializedName

data class Todo(
    val completed: Boolean,
    val id: Int,
    val title: String,
    val userId: Int
)
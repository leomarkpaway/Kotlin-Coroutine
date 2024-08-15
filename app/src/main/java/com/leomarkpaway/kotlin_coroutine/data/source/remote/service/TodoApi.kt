package com.leomarkpaway.kotlin_coroutine.data.source.remote.service

import com.leomarkpaway.kotlin_coroutine.data.source.remote.dto.Todo
import retrofit2.Call
import retrofit2.http.GET

interface TodoApi {

    @GET("/todos")
    fun getTodos(): Call<List<Todo>>

}
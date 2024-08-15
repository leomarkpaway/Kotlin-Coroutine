package com.leomarkpaway.kotlin_coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ViewModel
    private val myScope = CoroutineScope(CoroutineName("my_scope"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = MyViewModel()

        GlobalScope.launch {
            Log.d("GlobalScope", this.coroutineContext.toString())
        }

        lifecycleScope.launch {
            // lifecycleScope it aware on view life cycle
            Log.d("lifecycleScope", this.coroutineContext.toString())
        }

        myScope.launch {
            // custom scope use only if no lifecycleScope or viewModelScope
            // note: it should cancelable when it note use
            Log.d("myScope", this.coroutineContext.toString())
        }

        runBlocking {
            // only use for testing
            Log.d("runBlocking", this.coroutineContext.toString())
        }

    }

}
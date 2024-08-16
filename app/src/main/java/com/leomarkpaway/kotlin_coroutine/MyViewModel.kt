package com.leomarkpaway.kotlin_coroutine

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    init {
        viewModelScope.launch {
            // viewModelScope is aware on view model life cycle
            Log.d("viewModelScope", this.coroutineContext.toString())
        }

        // Demo: return value from coroutine scope
        val result = viewModelScope.async {
            delay(3000)
            "result value"
        }
        result.invokeOnCompletion {
            if(it == null) {
                Log.d( "returnValue", result.getCompleted())
            }
        }
    }

}
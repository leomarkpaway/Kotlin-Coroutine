package com.leomarkpaway.kotlin_coroutine

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    init {
        viewModelScope.launch {
            // viewModelScope is aware on view model life cycle
            Log.d("viewModelScope", this.coroutineContext.toString())
        }
    }

}
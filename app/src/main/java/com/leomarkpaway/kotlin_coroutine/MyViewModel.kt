package com.leomarkpaway.kotlin_coroutine

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
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

        // Demo #1: coroutine channel
        val channel = Channel<String>()
        // sender
        viewModelScope.launch {
            channel.send("value1")
            channel.send("value2")
            channel.cancel()
        }
        // receiver
        viewModelScope.launch {
            Log.d( "channel_demo1", "isCloseForReceive ${channel.isClosedForReceive}")
            val receive1 = channel.receive()
            Log.d( "channel_demo1", "receive1 $receive1")
            val receive2 = channel.receive()
            Log.d( "channel_demo1", "receive2 $receive2")
            Log.d( "channel_demo1", "isCloseForReceive ${channel.isClosedForReceive}")
        }

        // Demo #2: coroutine channel
        var channel1: ReceiveChannel<String> = Channel()
        // sender
        viewModelScope.launch {
            channel1 = produce(capacity = UNLIMITED) {
                send("value1")
                send("value2")
                send("value3")
                send("value4")
                send("value5")
            }
        }
        // receiver
        viewModelScope.launch {
            channel1.consumeEach { value ->
                Log.d( "channel_demo2", "receive $value")
                Log.d( "channel_demo2", "isCloseForReceive ${channel1.isClosedForReceive}")
            }
        }
    }

}
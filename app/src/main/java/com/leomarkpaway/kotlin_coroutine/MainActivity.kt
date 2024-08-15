package com.leomarkpaway.kotlin_coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.leomarkpaway.kotlin_coroutine.data.source.remote.service.RetrofitInstance
import com.leomarkpaway.kotlin_coroutine.adapter.TodoAdapter
import com.leomarkpaway.kotlin_coroutine.databinding.ActivityMainBinding
import com.leomarkpaway.kotlin_coroutine.data.source.remote.dto.Todo
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: ViewModel
    private val myScope = CoroutineScope(CoroutineName("my_scope"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = MyViewModel()

        val api = RetrofitInstance.api
        lifecycleScope.launch(Dispatchers.IO) {
            val response = api.getTodos().awaitResponse()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    setupRecyclerView(response.body()!!)
                }
            }
        }

        demoCoroutineScopes()
        demoWithContext()
        demoCoroutineJob()
    }

    private fun setupRecyclerView(todos: List<Todo>) = binding.rvTodos.apply {
        adapter = TodoAdapter(todos)
        layoutManager = LinearLayoutManager(this@MainActivity)
    }

    private fun demoCoroutineScopes() {
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

    private fun demoWithContext() {
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("demoWithContext", this.coroutineContext.toString())
            withContext(Dispatchers.Main) {
                Log.d("demoWithContext", this.coroutineContext.toString())
            }
        }
    }

    private fun demoCoroutineJob() {
        myScope.launch {
            val job1 = launch {
                while (isActive) {
                    Log.d("job1", "Job 1 Running ... ")

                }
            }
            Log.d("job1", "Canceling ... ")
            job1.cancel()
            job1.join()
            Log.d("job1", "Job 1 CANCELED!")

            val job2 = launch {
                while (true) {
                    ensureActive()
                    Log.d("job2", "Job 2 Running ... ")

                }
            }
            delay(2000L)
            Log.d("job2", "Canceling ... ")
            job2.cancel()
            job2.join()
            Log.d("job2", "Job 2 CANCELED!")

            val job3 = launch {
                while (true) {
                    Log.d("job3", "Job 3 Running ... ")
                    delay(1000L)
                }
            }
            delay(5000L)
            Log.d("job3", "Canceling ... ")
            job3.cancelAndJoin()
            Log.d("job3", "Job 3 CANCELED!")
        }
    }

}
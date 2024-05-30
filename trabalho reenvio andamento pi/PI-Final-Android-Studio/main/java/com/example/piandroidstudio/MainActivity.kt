package com.exemple.echoviagens

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echoviagens.ApiService
import com.example.echoviagens.Produtos
import com.example.piandroidstudio.R
import com.exemple.echoviagens.CustomAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewProdutos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ConfiguraÃ§Ã£o do Logging Interceptor
        val logging = HttpLoggingInterceptor { message ->
            Log.d("OkHttp", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // ConfiguraÃ§Ã£o do OkHttpClient com o interceptor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        // ConfiguraÃ§Ã£o do Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://3c1b993a-9a53-4f3f-b9c7-e08be33b2ffd-00-2uvqxwroiqpl1.kirk.repl.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getProdutos().enqueue(object : Callback<List<Produtos>> {
            override fun onResponse(call: Call<List<Produtos>>, response: Response<List<Produtos>>) {
                if (response.isSuccessful) {
                    val produtos = response.body() ?: emptyList()
                    adapter = CustomAdapter(produtos)
                    recyclerView.adapter = adapter
                } else {
                    Log.e("API Error", "Response not successful. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Produtos>>, t: Throwable) {
                Log.e("API Failure", "Error fetching products", t)
            }
        })
    }
}


package com.example.echoviagens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.echoviagens.Produtos
import com.example.echoviagens.ResponseCompra
import com.example.piandroidstudio.R
import com.exemple.echoviagens.MainActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.paymentactivity)

        var totalValue = intent.getStringExtra("TOTAL")?.toDoubleOrNull()
        val userId = intent.getIntExtra("USER", 0)
        val productList = intent.getParcelableArrayListExtra<Produtos>("PRODUCT_LIST")

        findViewById<TextView>(R.id.totalValueText).text = totalValue.toString()


        val cardNumberInput: EditText = findViewById(R.id.cardNumberInput)
        val cardExpirationInput: EditText = findViewById(R.id.cardExpirationInput)
        val cardCVCInput: EditText = findViewById(R.id.cardCVCInput)
        val finishPaymentButton: Button = findViewById(R.id.finishPaymentButton)

        finishPaymentButton.setOnClickListener {
            //if (validateCardDetails(cardNumberInput.text.toString(), cardExpirationInput.text.toString(), cardCVCInput.text.toString())) {

            enviaOrdem(userId, totalValue.toString().toDouble(), productList)
            //} else {
            //    Toast.makeText(this, "Detalhes do cartÃ£o invÃ¡lidos", Toast.LENGTH_LONG).show()
            //}
        }
    }

    private fun enviaOrdem(userId: Int, total: Double, products: ArrayList<Produtos>?) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://20a5686b-0ea6-436a-8019-9a5e55c9dafb-00-bl1jzh23zffh.spock.replit.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(OrderApiService::class.java)
        val orderRequest = OrderRequest(userId, total, products!!)
        val call = api.createOrder(orderRequest)
        call.enqueue(object : Callback<ResponseCompra> {
            override fun onResponse(call: Call<ResponseCompra>, response: Response<ResponseCompra>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PaymentActivity, "Pedido realizado com sucesso", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@PaymentActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@PaymentActivity, "Erro ao realizar pedido", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseCompra>, t: Throwable) {
                Toast.makeText(this@PaymentActivity, "Falha na conexÃ£o", Toast.LENGTH_LONG).show()
            }
        })
    }

    interface OrderApiService {
        @POST("/")
        fun createOrder(@Body orderRequest: OrderRequest): Call<ResponseCompra>
    }
}
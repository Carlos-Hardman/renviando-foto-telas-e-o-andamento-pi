package com.exemple.echoviagens

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.echoviagens.CartApiService
import com.example.echoviagens.Produtos
import com.example.piandroidstudio.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CartAdapter(
    private val items: MutableList<Produtos>,
    private val context: Context,
    private val updateTotal: () -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView = view.findViewById(R.id.productNameTextView)
        val productPrice: TextView = view.findViewById(R.id.productPriceTextView)
        val productQuantity: TextView = view.findViewById(R.id.productQuantityTextView)
        val productImage: ImageView = view.findViewById(R.id.productImageView)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detalhe_carrinho, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.productName.text = item.produtoNome
        // CorreÃƒÂ§ÃƒÂ£o: Assegurando que o preÃƒÂ§o ÃƒÂ© tratado como um nÃƒÂºmero para formataÃƒÂ§ÃƒÂ£o
        holder.productPrice.text = item.produtoPreco?.let { String.format("R$%.2f", it.toDouble()) }

        holder.productQuantity.text = "Qtd: ${item.quantidadeDisponivel}"
        Glide.with(context).load(item.imagemUrl).into(holder.productImage)

        holder.deleteButton.setOnClickListener {
            removeItemFromCart(item, position)
        }
    }

    private fun removeItemFromCart(item: Produtos, position: Int) {
        val retrofit = getRetrofit()
        val api = retrofit.create(CartApiService::class.java)

        val sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", 0)

        api.deleteCartItem(item.produtoId, userId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    items.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, items.size)
                    updateTotal()  // Chamada da funÃƒÂ§ÃƒÂ£o para atualizar o total
                    Toast.makeText(context, "Item deletado com sucesso", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Falha ao deletar o item", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Erro ao conectar-se ao servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://27831889-32b8-483e-9302-c31e43aacf2e-00-243sb8io6q5v0.worf.repl.co/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun getItemCount() = items.size
}

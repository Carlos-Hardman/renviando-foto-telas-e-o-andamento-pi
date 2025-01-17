package com.exemple.echoviagens



import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.echoviagens.Produtos
import com.example.piandroidstudio.R


class CustomAdapter(private val dataSet: List<Produtos>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.nomeProduto)
        val descricao: TextView = view.findViewById(R.id.descricaoProduto)
        val valor: TextView = view.findViewById(R.id.valorProduto)
        val imagem: ImageView = view.findViewById(R.id.imagem_produto)
        val btnComprar: Button = view.findViewById(R.id.btnComprar)


    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.itemdetalhe, viewGroup, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val produto = dataSet[position]

        viewHolder.nome.text = produto.produtoNome
        viewHolder.descricao.text = produto.produtoDesc
        viewHolder.valor.text = produto.produtoPreco.toString()

        Glide.with(viewHolder.itemView.context)
            .load(produto.imagemUrl)
            .placeholder(R.drawable.ic_launcher_background) // placeholder
            .error(com.google.android.material.R.drawable.mtrl_ic_error) // indica erro
            .into(viewHolder.imagem)

        viewHolder.btnComprar.setOnClickListener {
            val intent = Intent(viewHolder.itemView.context, ProdutoDetalhesActivity::class.java)
            intent.putExtra("ID_PRODUTO", produto.produtoId)
            intent.putExtra("NOME_PRODUTO", produto.produtoNome)
            intent.putExtra("DESCRICAO_PRODUTO", produto.produtoDesc)
            intent.putExtra("QUANTIDADE_DISPONIVEL", produto.quantidadeDisponivel)
            viewHolder.itemView.context.startActivity(intent)

        }

    }


    override fun getItemCount() = dataSet.size



}
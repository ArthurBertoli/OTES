package com.example.projetootes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// O Adapter recebe uma lista de transações no construtor
class TransacaoAdapter(private val transacoes: List<Transacao>) :
    RecyclerView.Adapter<TransacaoAdapter.TransacaoViewHolder>() {

    // O ViewHolder é quem "segura" as referências dos TextViews do nosso XML (item_transacao.xml)
    class TransacaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDescricao: TextView = itemView.findViewById(R.id.textDescricao)
        val textMeta: TextView = itemView.findViewById(R.id.textMeta)
        val textValor: TextView = itemView.findViewById(R.id.textValor)
        val buttonExcluir: Button = itemView.findViewById(R.id.buttonExcluir)
    }

    // Cria as "linhas" visuais inflando o layout XML
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransacaoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transacao, parent, false)
        return TransacaoViewHolder(view)
    }

    // Preenche a "linha" com os dados da transação correspondente àquela posição
    override fun onBindViewHolder(holder: TransacaoViewHolder, position: Int) {
        val transacao = transacoes[position]
        holder.textDescricao.text = transacao.descricao
        holder.textValor.text = "R$ ${transacao.valor}"
        holder.textMeta.text = transacao.tipo
        holder.buttonExcluir.visibility = View.GONE
    }

    // Diz ao RecyclerView quantos itens existem na lista
    override fun getItemCount(): Int {
        return transacoes.size
    }
}
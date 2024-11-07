package com.gereso.terramasterhub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KnowledgeAdapter(
    private val knowledgeList: List<Knowledge>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<KnowledgeAdapter.KnowledgeViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(knowledge: Knowledge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnowledgeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.knowledge_item, parent, false)
        return KnowledgeViewHolder(view)
    }

    override fun onBindViewHolder(holder: KnowledgeViewHolder, position: Int) {
        val knowledge = knowledgeList[position]
        holder.bind(knowledge, itemClickListener)
    }

    override fun getItemCount(): Int {
        return knowledgeList.size
    }

    class KnowledgeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(knowledge: Knowledge, clickListener: OnItemClickListener) {
            itemView.findViewById<TextView>(R.id.titleTextView).text = knowledge.title
            itemView.findViewById<ImageView>(R.id.imageView).setImageResource(knowledge.imageResId)
            itemView.setOnClickListener {
                clickListener.onItemClick(knowledge)
            }
        }
    }
}
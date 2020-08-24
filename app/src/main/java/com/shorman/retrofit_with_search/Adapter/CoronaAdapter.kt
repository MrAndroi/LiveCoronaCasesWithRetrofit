package com.shorman.retrofit_with_search.Adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shorman.retrofit_with_search.models.CoronaModelItem
import com.shorman.retrofit_with_search.R
import kotlinx.android.synthetic.main.cases_item.view.*

class CoronaAdapter :RecyclerView.Adapter<CoronaAdapter.CoronaViewHolder>() {


    class CoronaViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {}


    val diffUtil = object :DiffUtil.ItemCallback<CoronaModelItem>(){
        override fun areItemsTheSame(oldItem: CoronaModelItem, newItem: CoronaModelItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: CoronaModelItem,
            newItem: CoronaModelItem
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoronaViewHolder {
        return CoronaViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cases_item,parent,false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CoronaViewHolder, position: Int) {
        val coronaCase = differ.currentList[position]

        holder.itemView.apply {
            tvCountryName.text = coronaCase.Country
            tvNumberOfCases.text = coronaCase.Cases.toString()
            val date = coronaCase.Date.substring(0,10)
            tvDate.text = date
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}
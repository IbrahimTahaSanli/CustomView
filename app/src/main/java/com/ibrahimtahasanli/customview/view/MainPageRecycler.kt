package com.ibrahimtahasanli.customview.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibrahimtahasanli.customview.databinding.MainPageRowBinding
import org.json.JSONArray

class MainPageRecycler : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public var views : JSONArray;
    public var onElementClick: (( pos :Int)->Unit)? = null;

    constructor(views: JSONArray): super(){
        this.views = views;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return main_page_row(MainPageRowBinding.inflate(LayoutInflater.from(parent.context),parent, false)) as RecyclerView.ViewHolder;
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as main_page_row
        holder.binding.mainPageRowID.text = views.getJSONObject(position).getString("ID")
        holder.binding.mainPageRowName.text = views.getJSONObject(position).getString("Name")
        holder.binding.mainPageRowSelf.setOnClickListener(
            View.OnClickListener {
                if(this@MainPageRecycler.onElementClick != null)
                    this@MainPageRecycler.onElementClick?.invoke(position);
            }
        )
    }

    override fun getItemCount(): Int {
        return views.length();
    }
}
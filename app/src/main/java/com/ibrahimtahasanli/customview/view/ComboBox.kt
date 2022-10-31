package com.ibrahimtahasanli.customview.view

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ibrahimtahasanli.customview.R
import com.ibrahimtahasanli.customview.databinding.ComboBoxRowBinding
import com.ibrahimtahasanli.customview.databinding.ComboboxBinding

class ComboBox(activity: Activity, rows : List<String>) : Dialog(activity, R.style.full_screen_dialog) {
    private var activity: Activity = activity;
    private var binding: ComboboxBinding? = null;
    private var rows : List<String> = rows;

    public var onSelection: ((num: Number) -> Unit)? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ComboboxBinding.inflate(activity.layoutInflater);

        setTitle(R.string.ComboBox);

        setContentView(binding?.root as View);

        binding?.ComboBoxRecycler?.layoutManager = LinearLayoutManager(context);
        binding?.ComboBoxRecycler?.adapter = ComboBoxRecycler(rows);
    }

    public inner class ComboBoxRecycler(rows: List<String>) : RecyclerView.Adapter<ComboBoxRecycler.ComboBoxViewHolder>() {
        public var rows : List<String> = rows;


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComboBoxViewHolder {
            return ComboBoxViewHolder(ComboBoxRowBinding.inflate(LayoutInflater.from(parent.context),parent, false));
        }

        override fun onBindViewHolder(holder: ComboBoxViewHolder, position: Int) {
            holder.binding.ComboBoxRowText.text = rows[position];
            holder.binding.ComboBoxRowSelf.setOnClickListener {
                this@ComboBox.onSelection?.invoke(position);
            }
        }

        override fun getItemCount(): Int {
            return rows.size;
        }

        public inner class ComboBoxViewHolder(binding: ComboBoxRowBinding) : RecyclerView.ViewHolder(binding.root){
            public var binding : ComboBoxRowBinding = binding;
        }

    }

}
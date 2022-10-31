package com.ibrahimtahasanli.customview.view

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ibrahimtahasanli.customview.R
import com.ibrahimtahasanli.customview.controller.DataObject
import com.ibrahimtahasanli.customview.databinding.ComboboxButtonBinding
import com.ibrahimtahasanli.customview.databinding.DatePickerViewBinding
import com.ibrahimtahasanli.customview.databinding.EditTextViewBinding
import com.ibrahimtahasanli.customview.databinding.FragmentRecursiveShowPageBinding
import com.ibrahimtahasanli.customview.databinding.NewScreenButtonBinding
import com.ibrahimtahasanli.customview.model.Types
import org.json.JSONArray


class RecursiveShowPageFragment : Fragment() {
    private var binding : FragmentRecursiveShowPageBinding? = null;
    val args: RecursiveShowPageFragmentArgs by navArgs()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRecursiveShowPageBinding.inflate(inflater, container, false);
        return binding?.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.NewScreenNoHeader);

        for (ind in 0..DataObject.GetObjects()!!.length()-1){
            if(DataObject.GetObjects()!!.getJSONObject(ind).getString("CustomViewType") == Types.Header.Name)
                requireActivity().title = DataObject.GetObjects()!!.getJSONObject(ind).getJSONObject("Properties").getString("Text");
        }

        var recycler : RecursiveShowPageAdapter = RecursiveShowPageAdapter(JSONArray(args.jsonstring), activity as Activity, this);

        binding?.ShowPageRecycler?.layoutManager = LinearLayoutManager(context);
        binding?.ShowPageRecycler?.adapter = recycler;
    }

}

class RecursiveShowPageAdapter(objs : JSONArray, activity: Activity, fragment: Fragment): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    public var objs : JSONArray = objs;
    private var activity: Activity = activity;
    private var fragment: Fragment = fragment;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            Types.ComboBox.Value->return ComboBoxButtonHolder(ComboboxButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false));
            Types.TextInput.Value->return TextInputHolder(EditTextViewBinding.inflate(LayoutInflater.from(parent.context), parent, false));
            Types.DatePicker.Value->return DatePickerHolder(DatePickerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false));
            Types.NewScreen.Value->return NewScreenHolder(NewScreenButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false));

            else-> throw Exception("Haven't Implemented");
        }
    }

    override fun getItemViewType(position: Int): Int {
        when(objs.getJSONObject(GetRealIndex(position)).getString("CustomViewType")){
            Types.ComboBox.Name->return Types.ComboBox.Value;
            Types.TextInput.Name->return Types.TextInput.Value;
            Types.DatePicker.Name->return Types.DatePicker.Value;
            Types.NewScreen.name->return Types.NewScreen.Value;
            else->return -1;
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(objs.getJSONObject( GetRealIndex(position) ).getString("CustomViewType")){
            Types.ComboBox.Name->{
                holder as ComboBoxButtonHolder;

                var comboBoxObject = objs.getJSONObject( GetRealIndex(position) );

                var texts = comboBoxObject.getJSONObject("Properties").getJSONArray("Text");

                var text = mutableListOf<String>();

                for (ind : Int in 0..texts.length()-1 )
                    text.add(texts.getString(ind));

                holder.binding.root.setOnClickListener {

                    var tmp: ComboBox =
                        ComboBox(
                            this@RecursiveShowPageAdapter.activity,
                            text
                        );
                    tmp.onSelection = {
                        println("Clicked On :" + it.toString())
                        holder.binding.ComboBoxButtonSelection.text = text[it as Int];
                        tmp.dismiss()
                    }
                    tmp.show()
                }

                if(comboBoxObject.getJSONObject("Properties").getString("Header") != "")
                    holder.binding.ComboBoxHeader.text =comboBoxObject.getJSONObject("Properties").getString("Header");
            }
            Types.TextInput.Name->{
                holder as TextInputHolder;
                holder.binding.EditTextView.hint = objs.getJSONObject( GetRealIndex(position) ).getJSONObject("Properties").getString("Header")
            }
            Types.DatePicker.Name->{
                holder as DatePickerHolder;
            };
            Types.NewScreen.Name->{
                holder as NewScreenHolder;

                var newScreenObject = objs.getJSONObject( GetRealIndex(position) ).getJSONObject("Properties");
                holder.binding.NewScreenName.text = newScreenObject.getString("ScreenName")

                holder.binding.NewScreenButtonSelf.setOnClickListener {
                    this@RecursiveShowPageAdapter.activity?.runOnUiThread {
                        findNavController(fragment).navigate(
                            RecursiveShowPageFragmentDirections.actionRecursiveShowPageFragmentSelf(
                                newScreenObject.getString("ScreenView")
                            )
                        );
                    }
                }
            }
            else-> throw Exception("Haven't Implemented");
        }
    }

    override fun getItemCount(): Int {
        var count : Int = 0;
        for (ind in 0..objs.length()-1){
            if(objs.getJSONObject(ind).getString("CustomViewType") != Types.Header.Name)
                count++;
        }
        return count;
    }

    private fun GetRealIndex(position: Int): Int{
        var count : Int = -1;
        for (ind in 0..objs.length()){
            if (objs.getJSONObject(ind).getString("CustomViewType") != Types.Header.Name)
                count++;

            if(count == position)
                return ind;
        }

        return -1;
    }

    private inner class TextInputHolder(itemView: EditTextViewBinding) : RecyclerView.ViewHolder(itemView.root){
        public var binding: EditTextViewBinding = itemView;
    }

    private inner class ComboBoxButtonHolder(itemView: ComboboxButtonBinding) : RecyclerView.ViewHolder(itemView.root){
        public var binding: ComboboxButtonBinding = itemView;
    }

    private inner class DatePickerHolder(itemView: DatePickerViewBinding) : RecyclerView.ViewHolder(itemView.root){
        public var binding: DatePickerViewBinding = itemView;
    }

    private inner class NewScreenHolder(itemView: NewScreenButtonBinding) : RecyclerView.ViewHolder(itemView.root){
        public var binding: NewScreenButtonBinding = itemView;
    }

}
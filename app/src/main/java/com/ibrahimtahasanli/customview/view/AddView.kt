package com.ibrahimtahasanli.customview.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ibrahimtahasanli.customview.R
import com.ibrahimtahasanli.customview.databinding.FragmentAddViewBinding
import org.json.JSONArray
import org.json.JSONObject


class AddView : Fragment() {
    private var binding: FragmentAddViewBinding? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddViewBinding.inflate(inflater, container, false);
        return binding!!.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.AddScreenButton?.setOnClickListener(View.OnClickListener {
            var obj: JSONObject = JSONObject();
            obj.put("Name", binding!!.AddScreenName.text);
            try{
                obj.put("View", JSONArray(binding!!.AddScreenJSON.text.toString()))
                com.ibrahimtahasanli.customview.controller.View.PostView(obj,
                    object: com.ibrahimtahasanli.customview.controller.View.Events{
                        override fun OnStart() {
                        }

                        override fun OnEnd(obj: JSONArray) {
                            this@AddView.activity?.runOnUiThread({
                                findNavController().popBackStack()
                            })
                        }

                        override fun OnError(e: Exception) {
                        }

                    })
            }catch (e : Exception){
                e.printStackTrace()
            }



        })


    }

}
package com.ibrahimtahasanli.customview.view

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ibrahimtahasanli.customview.R
import com.ibrahimtahasanli.customview.controller.DataObject
import com.ibrahimtahasanli.customview.databinding.FragmentMainPageBinding
import org.json.JSONArray


class MainPage : Fragment() {
    public lateinit var binding: FragmentMainPageBinding;
    public lateinit var adapter: MainPageRecycler;

    public lateinit var views: JSONArray;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        views = JSONArray();

        binding.mainPageRecycler.layoutManager = LinearLayoutManager(context)
        adapter = MainPageRecycler(views)
        binding.mainPageRecycler.adapter = adapter

        adapter.onElementClick = {
            com.ibrahimtahasanli.customview.controller.View.GetViewByID(
            views.getJSONObject(it).getString("ID"),
                object:com.ibrahimtahasanli.customview.controller.View.Events{
                    override fun OnStart() {
                        println("Start")
                    }

                    override fun OnEnd(obj: JSONArray) {
                        DataObject.SetObjects(obj)
                        this@MainPage.activity?.runOnUiThread {
                            findNavController().navigate(MainPageDirections.actionMainPage2ToRecursiveShowPageFragment(obj.toString()));
                        }
                    }

                    override fun OnError(e: Exception) {
                        e.printStackTrace();
                    }
                }
            )
        }

        if((savedInstanceState == null) || !savedInstanceState.getBoolean("IsShown", false)!!) {
        }
        activity?.title = "Hello"

        com.ibrahimtahasanli.customview.controller.View.GetAllViewID( object:
            com.ibrahimtahasanli.customview.controller.View.Events{
            override fun OnStart() {
                println("start");
            }

            override fun OnEnd(obj: JSONArray) {
                this@MainPage.views = obj;
                this@MainPage.adapter.views = obj;

                this@MainPage.activity?.runOnUiThread {
                    adapter.notifyDataSetChanged()
                }
                println("end");
            }

            override fun OnError(e: Exception) {
                e.printStackTrace();
            }
        })

        binding.mainPageButton.setOnClickListener{
            findNavController().navigate( MainPageDirections.actionMainPage2ToAddView())
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(! outState.getBoolean("IsShown"))
            outState.putBoolean("IsShown", true);
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainPageBinding.inflate(layoutInflater, container, false);
        return binding.root;
    }
}
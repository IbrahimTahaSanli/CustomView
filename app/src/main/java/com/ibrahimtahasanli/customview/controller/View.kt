package com.ibrahimtahasanli.customview.controller

import android.os.AsyncTask
import com.ibrahimtahasanli.customview.model.Paths
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class View {
    companion object{
        var instance :View = View();
        public fun GetAllViewID(events: Events){
            if (instance == null)
                instance = View();

            GlobalScope.launch {
                events.OnStart();
                var obj : JSONArray = instance._GetAllViewID(events);
                events.OnEnd(obj)
            }
        }

        public fun GetViewByID(ID: String, events: Events){
            if (instance == null)
                instance = View();

            GlobalScope.launch {
                events.OnStart();
                var obj : JSONArray = instance._GetViewByID(ID, events);
                events.OnEnd(obj)
            }
        }

        public fun PostView(json: JSONObject, events:Events){
            if(instance == null)
                instance = View();

            GlobalScope.launch {
                events.OnStart();
                instance._PostView(json, events)
            }

        }
    }

    private fun _GetViewByID(ID: String, events: Events): JSONArray{
        var conn : HttpURLConnection = URL(Paths.ViewPath.URL + ID).openConnection() as HttpURLConnection;
        conn.doInput = true;

        try {
            conn.connect();
        }
        catch ( e: Exception){
            events.OnError(e);
        }
        var input : InputStream = conn.inputStream;

        var str : String = input.reader().readText();
        input.close();

        return JSONArray(str);
    }

    private fun _GetAllViewID(events: Events) :JSONArray{
        var conn : HttpURLConnection = URL(Paths.ViewsPath.URL).openConnection() as HttpURLConnection;
        conn.doInput = true;

        try {
            conn.connect();
        }
        catch ( e: Exception){
            events.OnError(e);
        }
        var input : InputStream = conn.inputStream;

        var str : String = input.reader().readText();
        input.close();

        return JSONArray(str);
    }

    private fun _PostView(json: JSONObject, events: Events) : Boolean{
        var conn : HttpURLConnection = URL(Paths.ViewsPath.URL).openConnection() as HttpURLConnection;
        conn.doInput = true;
        conn.doOutput = true;
        conn.requestMethod = "POST";

        var outputSream : OutputStreamWriter = OutputStreamWriter(conn.outputStream);

        outputSream.write(json.toString());
        outputSream.flush();
        if(conn.responseCode == HttpURLConnection.HTTP_OK){
            events.OnEnd(JSONArray())
            return true;
        }
        else{
            events.OnError(java.lang.Exception());
            return false;
        }
    }

    public interface Events{
        public fun OnStart();
        public fun OnEnd(obj: JSONArray) ;
        public fun OnError(e: Exception);
    }

}
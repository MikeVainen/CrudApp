package com.mviana.crudapp.api

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.util.JsonReader
import android.util.JsonToken
import android.util.Log
import com.mviana.crudapp.users.User
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.io.StringReader
import java.net.URL
import java.time.LocalDateTime

class ServiceRequest(var listener: RequestPerformer): AsyncTask<Int, Void, Void>(){

    val SRQ_TYPE_GET_USER = 1
    val SRQ_TYPE_POST_USER = 2
    val SRQ_TYPE_PUT_USER = 3
    val SRQ_TYPE_DEL_USER = 4

    val SRQ_TAG = "API Service Request"

    val SRQ_RESULT_ERROR = -1
    val SRQ_RESULT_SUCCESS = 1
    val SRQ_RESULT_NO_BODY = 0

    val requestUrl = Uri.Builder()
            .scheme("https")
            .authority("hello-world.innocv.com")
            .appendPath("api")
            .appendPath("User")
            .toString()
    val JSON:MediaType = MediaType.parse("application/json")!!

    var rBody: String = ""
    set(value){
        field = if (value!=" ") value
            else ""
    }
    var client: OkHttpClient = OkHttpClient()
    var formBody: String = ""

    //If request type corresponds to PUT or POST situations, params are checked for the body

    override fun doInBackground(vararg params: Int?): Void? {
        // ...
        var request = params[0]
        var callback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                listener.onRequestFailed(SRQ_RESULT_ERROR)
            }
            override fun onResponse(call: Call, response: Response) {
                var rcvbody = response.body()?.string()
                Log.println(Log.INFO,"onResponse", "Recibida")
                when(response.code()){
                    200 ->
                            if (!rcvbody.isNullOrEmpty() && params[0]==SRQ_TYPE_GET_USER) {
                                rBody = rcvbody.toString()
                                listener.onRequestSuccess(request!!)
                            }
                            else{

                                if(response.code()==200) listener.onRequestSuccess(request!!)
                            }
                    300 -> listener.onRequestFailed(300)
                    400 -> listener.onRequestFailed(400)
                    500 -> listener.onRequestFailed(500)
                    else -> listener.onRequestFailed(response.code())
                }

            }
        }
        when(params[0]){
            SRQ_TYPE_GET_USER ->{
                //Chequear si es get o get + Id
                if(params.size>1){
                    var getId = params[1]
                    if (getId != null) getUserId(client,callback, getId)
                }
                else getUser(client, callback)
            }
            SRQ_TYPE_POST_USER ->{
                Log.println(Log.ERROR,"POST USER","llamado")
                postUser(client,callback)
            }
            SRQ_TYPE_PUT_USER ->{
                Log.println(Log.ERROR,"PUT USER","llamado")
                putUser(client, callback)
            }
            SRQ_TYPE_DEL_USER ->{
                Log.println(Log.ERROR,"DELETE USER","llamado")
                deleteUser(client,callback, params[1])

            }

        }
        return null

    }

    override fun onPreExecute() {
        super.onPreExecute()

    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)


    }

    private fun postUser(client: OkHttpClient, callback:Callback) {
        var jsonBody = RequestBody.create(JSON, formBody)
        var request: Request = Request.Builder()
                .url(requestUrl)
                .post(jsonBody)
                .addHeader("Content-Type","application/json")
                .build()

        client.newCall(request).enqueue(callback)

    }

    private fun putUser(client: OkHttpClient, callback:Callback) {
        var jsonBody = RequestBody.create(JSON, formBody)
        var request: Request = Request.Builder()
                .url(requestUrl)
                .put(jsonBody)
                .addHeader("Content-Type","application/json")
                .build()
        client.newCall(request).enqueue(callback)

    }

    private fun getUser(client: OkHttpClient, callback:Callback) {
        var request: Request = Request.Builder()
                .url(requestUrl)
                .addHeader("content-type","application/json")
                .build()
        client.newCall(request).enqueue(callback)
    }

    private fun getUserId(client: OkHttpClient, callback:Callback, id: Int?) {
        val requestUrl = Uri.Builder()
                .scheme("https")
                .authority("hello-world.innocv.com")
                .appendPath("api")
                .appendPath("User")
                .appendPath(id.toString())
                .toString()
        var request: Request = Request.Builder()
                .url(requestUrl)
                .get()
                .addHeader("content-type","application/json")
                .build()
        client.newCall(request).enqueue(callback)
    }
    private fun deleteUser(client: OkHttpClient, callback:Callback, id: Int?) {
        val requestUrl = Uri.Builder()
                .scheme("https")
                .authority("hello-world.innocv.com")
                .appendPath("api")
                .appendPath("User")
                .appendPath(id.toString())
                .toString()
        var request: Request = Request.Builder()
                .url(requestUrl)
                .delete()
                .addHeader("content-type","application/json")
                .build()
        client.newCall(request).enqueue(callback)
    }

    fun rcvdStringToJSON(jsonString: String): ArrayList<User> {
        var userList = ArrayList<User>()
        var reader = JsonReader(StringReader(jsonString))
        reader.beginArray()
        while(reader.hasNext()){
            var id: Int = -1
            var name: String = ""
            var bday: String = ""

            reader.beginObject()
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "name" -> if(reader.peek() != JsonToken.NULL) name = reader.nextString()
                        else reader.skipValue()
                    "birthdate" -> if(reader.peek() != JsonToken.NULL) bday = reader.nextString()
                        else reader.skipValue()
                    "id" -> if(reader.peek() != JsonToken.NULL) id = reader.nextInt()
                        else reader.skipValue()
                    else -> reader.skipValue()
                }
            }
            reader.endObject()
            val user = User()
            user.name = name
            user.id = id
            user.birthDate = LocalDateTime.parse(bday)
            userList.add(user)
        }
        reader.endArray()
        return userList

    }



}
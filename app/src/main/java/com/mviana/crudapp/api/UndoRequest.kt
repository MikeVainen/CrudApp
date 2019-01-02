package com.mviana.crudapp.api

import android.util.JsonReader
import android.util.JsonToken
import com.mviana.crudapp.users.User
import java.io.StringReader
import java.time.LocalDateTime

class UndoRequest(var requestListener:RequestPerformer){

    fun undoRequest(request: String){
        var reader = JsonReader(StringReader(request))
        var action = -1
        var userData = ""
        reader.beginObject()
        while(reader.hasNext()){
            when (reader.nextName()) {
                "action" -> if(reader.peek() != JsonToken.NULL) action = reader.nextInt()
                else reader.skipValue()
                "User" ->if(reader.peek() != JsonToken.NULL) userData = reader.nextString()
                else reader.skipValue()
            }
        }
        reader.endObject()
        var undoRequest = ServiceRequest(requestListener)
        reader = JsonReader(StringReader(userData))
        var user = User()
        reader.beginObject()
        while(reader.hasNext()){
            when (reader.nextName()) {
                "name" -> if(reader.peek() != JsonToken.NULL) user.name = reader.nextString()
                else reader.skipValue()
                "birthdate" -> if(reader.peek() != JsonToken.NULL) user.birthDate = LocalDateTime.parse(reader.nextString())
                else reader.skipValue()
                "id" -> if(reader.peek() != JsonToken.NULL){
                    if(action==undoRequest.SRQ_TYPE_DEL_USER){
                        user.id = 0
                        reader.skipValue()
                    }
                        else user.id = reader.nextInt()
                }
                else reader.skipValue()
            }
        }
        reader.endObject()
        when(action){
            undoRequest.SRQ_TYPE_POST_USER ->{
                undoRequest.execute(undoRequest.SRQ_TYPE_DEL_USER, user.id)

            }
            undoRequest.SRQ_TYPE_PUT_USER  ->{
                undoRequest.formBody = userData
                undoRequest.execute(undoRequest.SRQ_TYPE_PUT_USER)
            }

            undoRequest.SRQ_TYPE_DEL_USER  ->{
                undoRequest.formBody = userData
                undoRequest.execute(undoRequest.SRQ_TYPE_POST_USER)
            }


        }
    }


}
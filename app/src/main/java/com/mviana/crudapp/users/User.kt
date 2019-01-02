package com.mviana.crudapp.users

import android.util.JsonReader
import android.util.JsonToken
import android.util.JsonWriter
import org.json.JSONObject
import java.io.Serializable
import java.io.StringReader
import java.io.StringWriter
import java.io.Writer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter




class User: Serializable{

    var keys: Array<String> = arrayOf("name","birthdate","id")

    var name: String = "John Doe"

    var birthDate: LocalDateTime = LocalDateTime.MIN
        set(value){
            field = if (LocalDateTime.MIN.isBefore(value))
                value
            else LocalDateTime.MIN
        }

    var id: Int = Int.MIN_VALUE
        set(value) {
            field = if (value <= 0)
                0
            else value
        }

    fun toJSONString(): String{
        var stringwriter = StringWriter()
        var jsonwriter = JsonWriter(stringwriter)
        jsonwriter.setIndent(" ")
        jsonwriter.beginObject()
        jsonwriter.name("name").value(name)
        jsonwriter.name("birthdate").value(birthDate.toString())
        jsonwriter.name("id").value(id)
        jsonwriter.endObject()
        jsonwriter.flush()
        return stringwriter.toString()

    }

    fun setWithJSON(userJSON: String): User {
        var reader = JsonReader(StringReader(userJSON))

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "name" -> if (reader.peek() != JsonToken.NULL) name = reader.nextString()
                else reader.skipValue()
                "birthdate" -> if (reader.peek() != JsonToken.NULL) birthDate =
                        LocalDateTime.parse(reader.nextString())
                else reader.skipValue()
                "id" -> if (reader.peek() != JsonToken.NULL) id = reader.nextInt()
                else reader.skipValue()
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return this
    }



    /**

    fun toJSON(): JSONObject{

    val jsonData = JSONObject().put("name",name)
    .put("id",id)
    .put("birthdate",birthDate.toString())
    return jsonData
    }

    fun toData( userJson: JSONObject){
    for (key in keys){
    var keyValue: String = userJson.getString(key)
    if (userJson.get(key)== null && key=="id"){
    id = userJson.getInt(key)
    }
    else{
    if (key=="name") name = keyValue
    else birthDate = stringToLocalDateTime(keyValue)
    }

    }

    }
     */

    fun stringToLocalDateTime(dateTime: String): LocalDateTime{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDateTime.parse(dateTime, formatter)

    }

}
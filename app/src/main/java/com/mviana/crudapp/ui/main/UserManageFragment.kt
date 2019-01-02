package com.mviana.crudapp.ui.main

import android.content.Context
import android.os.Bundle
import android.util.JsonReader
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.Fragment
import android.util.JsonWriter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.mviana.crudapp.R
import com.mviana.crudapp.api.RequestPerformer
import com.mviana.crudapp.api.ServiceRequest
import com.mviana.crudapp.db.DBManager
import com.mviana.crudapp.users.User
import com.mviana.crudapp.users.UserManager
import java.io.StringReader
import java.io.StringWriter
import java.time.LocalDateTime

class UserManageFragment: Fragment(), RequestPerformer {

    private lateinit var listener: UserManager
    private lateinit var userEdit: User
    private var onRequest = false
    private lateinit var editRequest: ServiceRequest
    private var requestAction = -1

    companion object {

        private const val USER = "user"


        fun newInstance(user: User): UserManageFragment {
            val fragment = UserManageFragment()
            val args = Bundle()
            args.putSerializable(USER, user)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is UserManager) {
            listener = context
            Log.println(Log.ERROR,"AddEditFragment","Listener detectado")
        } else {
            throw ClassCastException(context.toString() + " no es del tipo UserManager.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater!!.inflate(
            R.layout.user_manage_fragment, container,
            false)
        var closeBtn = view.findViewById(R.id.edit_close_btn) as com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton
        var nameText = view.findViewById(R.id.etEditName) as EditText
        var bdayText = view.findViewById(R.id.etEditBday) as EditText
        var editBtn = view.findViewById(R.id.submitEditUser) as Button
        var deleteBtn = view.findViewById(R.id.deleteEditUser) as Button
        var updateBtn = view.findViewById(R.id.updateEditUser) as Button
        var requestBar = view.findViewById(R.id.editPBar) as ProgressBar
        editRequest = ServiceRequest(this)
        requestBar.visibility = View.INVISIBLE
        closeBtn.setOnClickListener{
                listener.onCloseAddEdit()
        }
        editBtn.setOnClickListener{
            userEdit.name = nameText.text.toString()
            userEdit.birthDate = LocalDateTime.parse(bdayText.text.toString())

            editRequest.formBody = userEdit.toJSONString()
            requestAction = editRequest.SRQ_TYPE_PUT_USER
            editRequest.execute(editRequest.SRQ_TYPE_PUT_USER)

            requestBar.visibility = View.VISIBLE
        }
        deleteBtn.setOnClickListener{
            requestAction = editRequest.SRQ_TYPE_DEL_USER
            editRequest.execute(editRequest.SRQ_TYPE_DEL_USER, userEdit.id)

            requestBar.visibility = View.VISIBLE
        }
        updateBtn.setOnClickListener{
            requestAction = editRequest.SRQ_TYPE_GET_USER
            editRequest.execute(editRequest.SRQ_TYPE_GET_USER, userEdit.id)
            requestBar.visibility = View.VISIBLE

        }
        if(arguments!= null && arguments!![USER] is User){
            userEdit = arguments!![USER] as User
            nameText.setText(userEdit.name)
            bdayText.setText(userEdit.birthDate.toString())

        }

        return view
    }
    override fun onRequestSuccess(requestType: Int) {

        when(requestAction){
            editRequest.SRQ_TYPE_PUT_USER, editRequest.SRQ_TYPE_DEL_USER->{
                var stringwriter = StringWriter()
                JsonWriter(stringwriter)
                    .beginObject()
                    .name(resources.getString(R.string.action)).value(requestAction)
                    .name(resources.getString(R.string.user)).value(userEdit.toJSONString())
                    .endObject()
                    .flush()
                val prefs = context!!.getSharedPreferences(resources.getString(R.string.PREFS_NAME), 0)
                prefs.edit().putString(resources.getString(R.string.LAST_ACTION), stringwriter.toString()).apply()
                listener.onUserEditedDeleted("edit")
            }
            editRequest.SRQ_TYPE_GET_USER ->{
                //Update User's entry in the DB
                var editedUsers = ArrayList<User>()
                editedUsers.add(User().setWithJSON(editRequest.rBody))
                Log.println(Log.ERROR,"on Update User","Updated " + editedUsers[0].name)
                var dbman = DBManager(context!!)
                dbman.editUsers(editedUsers)
                listener.onUserEditedDeleted("update")


            }
        }
        onRequest = false
        listener.onUserEditedDeleted(resources.getString(R.string.edit))

    }

    override fun onRequestFailed(rsp: Int) {
        Log.println(Log.ERROR,"RequestError"," error")
        onRequest = false
        var failText: String
        when(rsp){
            300 -> failText = resources.getString(R.string.request_300)
            400 -> failText = resources.getString(R.string.request_400)
            500 -> failText = resources.getString(R.string.request_500)
            else -> failText = resources.getString(R.string.failed_request)
        }
        onRequest = false
        listener.onRequestFailed(failText)

    }
}
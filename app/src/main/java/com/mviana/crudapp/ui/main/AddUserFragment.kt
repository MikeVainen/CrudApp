package com.mviana.crudapp.ui.main

import android.content.Context
import android.os.Bundle
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
import com.mviana.crudapp.users.User
import com.mviana.crudapp.users.UserManager
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton
import java.io.StringWriter
import java.time.LocalDateTime

class AddUserFragment: Fragment(), RequestPerformer {

    private lateinit var listener: UserManager
    private lateinit var addRequest: ServiceRequest
    private lateinit var postUser: User
    private var onRequest = false

    companion object {

        private const val MODE = "mode"

        fun newInstance(mode: Int): AddUserFragment {
            val args: Bundle = Bundle()
            args.putSerializable(MODE, mode)
            val fragment = AddUserFragment()
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
            R.layout.adduser_fragment, container,
            false)
        var closeBtn = view.findViewById(R.id.add_close_btn) as FloatingActionButton
        var nameText = view.findViewById(R.id.etAddName) as EditText
        var bdayText = view.findViewById(R.id.etAddBday) as EditText
        var idText = view.findViewById(R.id.etAddId) as EditText
        var submitBtn = view.findViewById(R.id.submitAddUser) as Button
        var requestBar = view.findViewById(R.id.addPBar) as ProgressBar
        addRequest = ServiceRequest(this)
        requestBar.visibility = View.INVISIBLE
        closeBtn.setOnClickListener{
                listener.onCloseAddEdit()
            }
        submitBtn.setOnClickListener{
                Log.println(Log.ERROR,"Submit","Pulsado")
                postUser = User()
                postUser.name = nameText.text.toString()
                postUser.birthDate = LocalDateTime.parse(bdayText.text.toString())
                postUser.id = Integer.parseInt(idText.text.toString())
                addRequest.formBody = postUser.toJSONString()
                addRequest.execute(addRequest.SRQ_TYPE_POST_USER)
                requestBar.visibility = View.VISIBLE
            }
        return view
    }

    override fun onRequestSuccess(requestType: Int) {
        listener.onUserSubmitted()
        var stringwriter = StringWriter()
        JsonWriter(stringwriter)
            .beginObject()
            .name(resources.getString(R.string.action)).value(addRequest.SRQ_TYPE_POST_USER)
            .name(resources.getString(R.string.user)).value(postUser.toJSONString())
            .endObject()
            .flush()
        val prefs = context!!.getSharedPreferences(resources.getString(R.string.PREFS_NAME), 0)
        prefs.edit().putString(resources.getString(R.string.LAST_ACTION), stringwriter.toString()).apply()
        onRequest = false
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
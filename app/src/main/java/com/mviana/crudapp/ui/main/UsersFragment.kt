package com.mviana.crudapp.ui.main

import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import com.mviana.crudapp.R
import com.mviana.crudapp.api.RequestPerformer
import com.mviana.crudapp.api.ServiceRequest
import com.mviana.crudapp.db.DBManager
import com.mviana.crudapp.users.UserFilter
import com.mviana.crudapp.users.UserManager

class UsersFragment : Fragment(), RequestPerformer {

    private lateinit var viewModel: MainViewModel
    private lateinit var ctx: Context
    private lateinit var listener: UserManager
    private var onRequest = false
    private lateinit var getRequest: ServiceRequest
    lateinit var dbman: DBManager



    companion object {

        private const val FILTER = "filter"

        fun newInstance(): UsersFragment{
            val fragment = UsersFragment()
            return fragment
        }

        fun newInstance(filter: String): UsersFragment {
            val args = Bundle()
            args.putSerializable(FILTER, filter)
            val fragment = UsersFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context!!
        if (context is UserManager) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + " no es del tipo UserFilterListener.")
        }
        dbman = DBManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater!!.inflate(R.layout.users_fragment, container,
            false)
        var rvUsers = view.findViewById(R.id.rv_users) as RecyclerView
        rvUsers.layoutManager = GridLayoutManager(activity, 1)
        if(arguments!= null){
            rvUsers.adapter = UserAdapter(UserFilter().getFilteredUsers(dbman.getCurrentUsers(),arguments!![FILTER].toString()),ctx)
        }
        else rvUsers.adapter = UserAdapter(dbman.getCurrentUsers(),ctx)

        var addUserButton = view.findViewById(R.id.add) as FloatingActionButton
        var updateUserButton = view.findViewById(R.id.updateUsersButton) as FloatingActionButton
        var updatePBar = view.findViewById(R.id.updatePBar) as ProgressBar
        updatePBar.visibility = View.INVISIBLE

        addUserButton.setOnClickListener {
            listener.onAddUser()
        }
        updateUserButton.setOnClickListener{
            getRequest.execute(getRequest.SRQ_TYPE_GET_USER)
            updatePBar.visibility = View.VISIBLE
        }
        getRequest = ServiceRequest(this)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel


    }

    override fun onRequestSuccess(requestType: Int) {
        var updatedUserList = getRequest.rcvdStringToJSON(getRequest.rBody)
        dbman.deleteCurrentUsers()
        dbman.addUsers(updatedUserList)
        onRequest = false
        listener.onUserListUpdated()
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



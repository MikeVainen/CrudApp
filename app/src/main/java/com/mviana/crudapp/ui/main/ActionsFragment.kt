package com.mviana.crudapp.ui.main

import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.Switch
import com.mviana.crudapp.R
import com.mviana.crudapp.api.RequestPerformer
import com.mviana.crudapp.api.UndoRequest
import com.mviana.crudapp.users.UserManager


class ActionsFragment: Fragment(), RequestPerformer {

    companion object {
        fun newInstance(mode: Int) = ActionsFragment()
    }

    private lateinit var ctx: Context
    private lateinit var viewModel: MainViewModel
    private lateinit var listener: UserManager
    private lateinit var appMode: String
    private var onRequest = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context!!
        val prefs = ctx.getSharedPreferences(resources.getString(R.string.PREFS_NAME), 0)
        if (context is UserManager) {
            listener = context
            Log.println(Log.ERROR,"ActionsFragment","Listener detectado")
            appMode = prefs.getString(listener.THEME_MODE, listener.THEME_NO_THEME)
        } else {
            throw ClassCastException(context.toString() + " no es del tipo UserFilterListener.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater!!.inflate(R.layout.actions_fragment, container,
            false)
        var userFilter = view.findViewById(R.id.user_filter) as SearchView
        userFilter.setQueryHint("Filtrar usuarios")
        userFilter.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                listener.onFiltering(newText)
                return false
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                listener.onFiltering(query)
                return false
            }
        })
        var undoBtn = view.findViewById(R.id.undoBtn) as Button
        undoBtn.setOnClickListener{
            val prefs = context!!.getSharedPreferences(resources.getString(R.string.PREFS_NAME), 0)
            var requestUndoer = UndoRequest(this)
            requestUndoer.undoRequest(prefs.getString(resources.getString(R.string.LAST_ACTION),"No Action"))

        }
        var switchTheme = view.findViewById(R.id.NightMode) as Switch
        if(appMode == listener.THEME_NIGHT) switchTheme.isChecked = true
        switchTheme.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked) {
                Log.println(Log.ERROR,"switch Mode","CHECK")
                if(appMode==listener.THEME_DAY) listener.onChangeAppTheme(1)
            }
            else{
                Log.println(Log.ERROR,"switch Mode","UNCHECK")
                if(appMode==listener.THEME_NIGHT) listener.onChangeAppTheme(0)
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

    }

    override fun onRequestFailed(rsp: Int) {
        Log.println(Log.ERROR,"RequestError"," error")
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

    override fun onRequestSuccess(requestType: Int) {
        onRequest = false
        listener.onUndoSuccess()

    }




}
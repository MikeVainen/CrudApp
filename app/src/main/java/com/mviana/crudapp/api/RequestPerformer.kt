package com.mviana.crudapp.api

/**
 * Created by mviana on 20/12/18.
 */
interface RequestPerformer {

    fun onRequestFailed(rsp: Int)

    fun onRequestSuccess(requestType: Int)


}
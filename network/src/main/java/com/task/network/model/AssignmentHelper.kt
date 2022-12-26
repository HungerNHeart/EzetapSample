package com.task.network.model

import android.content.Context
import com.task.network.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AssignmentHelper(private val context: Context,private val assignmentService: AssignmentService) {
    private val networkHelper by lazy {
        NetworkHelper(context)
    }
    fun getAssignmentData(): Flow<ResponseData<AssignmentResponse>> = flow {
        emit(ResponseData.loading())
        if(networkHelper.isOnline()) {
            val result = assignmentService.fetchUIData()
            emit(result.getResponseData())
        }else{
            emit(ResponseData.error(StatusCode.NOT_NETWORK_FOUND, context.getString(R.string.no_network_message)))
        }
    }
}
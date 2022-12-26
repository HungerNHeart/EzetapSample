package com.task.ezetapapp

import androidx.compose.runtime.snapshots.SnapshotMutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.ezetapapp.repository.AssignmentRepo
import com.task.network.model.AssignmentResponse
import com.task.network.model.ResponseData
import com.task.network.model.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssignmentViewModel @Inject constructor(
    private val assignmentRepo: AssignmentRepo
) : ViewModel() {

    private val _assignmentData = MutableStateFlow<ResponseData<AssignmentResponse>?>(null)
    val assignmentData = _assignmentData.map {
        it?.data
    }
    val errorCode = _assignmentData.map {
        Pair(it?.code, it?.message)
    }
    val assignmentNetworkState = _assignmentData.map {
        it?.status
    }
    val title = _assignmentData.map {
        if(it?.status == Status.SUCCESS) it.data?.headingText.orEmpty() else if(it?.status == Status.ERROR) it.message.orEmpty() else "Loading.."
    }
    val uiData = _assignmentData.map {
        it?.data?.uiData.orEmpty()
    }

    val inputStore = MutableStateFlow(SnapshotStateMap<String, String>())

    init {
        doRetry()
    }


    fun onValueChanged(key: String?, value: String) {
        val temp = inputStore.value
        temp[key.orEmpty()] = value
        inputStore.tryEmit(temp)
    }

    fun doRetry(){
        viewModelScope.launch(Dispatchers.IO) {
            assignmentRepo.getAssignmentData().collect {
                _assignmentData.value = it
            }
        }

    }

}
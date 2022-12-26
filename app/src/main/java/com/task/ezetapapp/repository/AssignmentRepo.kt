package com.task.ezetapapp.repository

import com.task.network.model.AssignmentHelper
import javax.inject.Inject


class AssignmentRepo @Inject constructor(
    private val assignmentHelper: AssignmentHelper
) {
    fun getAssignmentData() = assignmentHelper.getAssignmentData()

}
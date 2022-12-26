package com.task.ezetapapp

import android.content.Context
import com.task.network.model.AssignmentHelper
import com.task.network.model.AssignmentService
import com.task.network.model.RestClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun assignmentServiceProvider(): AssignmentService =
        RestClient.getAssignmentService()


    @Singleton
    @Provides
    fun assignmentHelperProvider(@ApplicationContext context: Context, assignmentService: AssignmentService) =
        AssignmentHelper(context, assignmentService)
}
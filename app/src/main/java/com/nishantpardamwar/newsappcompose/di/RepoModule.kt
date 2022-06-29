package com.nishantpardamwar.newsappcompose.di

import com.nishantpardamwar.newsappcompose.repo.RemoteDS
import com.nishantpardamwar.newsappcompose.repo.RemoteDSImpl
import com.nishantpardamwar.newsappcompose.repo.Repo
import com.nishantpardamwar.newsappcompose.repo.RepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepoModule {
    @Binds
    abstract fun bindRemoteDS(remoteDsImpl: RemoteDSImpl): RemoteDS

    @Binds
    abstract fun bindRepo(repoImpl: RepoImpl): Repo
}
package com.nishantpardamwar.newsappcompose.repo

import com.nishantpardamwar.newsappcompose.models.IpInfoResponse
import com.nishantpardamwar.newsappcompose.models.NewsResponse
import javax.inject.Inject

interface Repo {
    suspend fun getIpInfo(): IpInfoResponse
    suspend fun getTopHeadlines(countryCode: String): NewsResponse
    suspend fun getNews(query: String): NewsResponse
}

class RepoImpl @Inject constructor(private val remoteDS: RemoteDS) : Repo {
    override suspend fun getIpInfo(): IpInfoResponse {
        return remoteDS.getIpInfo()
    }

    override suspend fun getTopHeadlines(countryCode: String): NewsResponse {
        return remoteDS.getTopHeadlines(countryCode)
    }

    override suspend fun getNews(query: String): NewsResponse {
        return remoteDS.getNews(query)
    }
}



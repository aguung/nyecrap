package com.devfutech.nyecrap.kotlin_flow

import com.devfutech.common.JsoupNetworkCall
import com.devfutech.common.LinkPreviewResult
import com.devfutech.common.isNullResult
import com.devfutech.common.validateUrl
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class NyecrapFlow private constructor() {

    companion object {
        val instance: NyecrapFlow
            get() = NyecrapFlow()
    }

    private var url: String = ""

    fun getLinkPreview(link: String): Flow<LinkPreviewResult?> = flow {
        val request = coroutineScope {
            async { fetchContent(link) }
        }
        emit(request.await())
    }.flowOn(Dispatchers.IO)

    private fun fetchContent(link: String): LinkPreviewResult {
        url = link.validateUrl()
        for (agent in JsoupNetworkCall.instance.agents) {
            val result = JsoupNetworkCall.instance.callUrl(url, agent, link)
            val isResultNull = result?.isNullResult()
            if (isResultNull == false) {
                return result
            }
        }
        throw RuntimeException("This $link --> Null or empty response from the server")
    }
}
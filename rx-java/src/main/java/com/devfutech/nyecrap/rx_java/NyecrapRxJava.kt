package com.devfutech.nyecrap.rx_java

import com.devfutech.common.JsoupNetworkCall
import com.devfutech.common.LinkPreviewResult
import com.devfutech.common.isNullResult
import com.devfutech.common.validateUrl
import rx.Observable

class NyecrapRxJava private constructor() {

    companion object {
        val instance: NyecrapRxJava
            get() = NyecrapRxJava()
    }

    private var url: String = ""

    fun getLinkPreview(link: String): Observable<LinkPreviewResult?> {
        this.url = link.validateUrl()
        return Observable.from( JsoupNetworkCall.instance.agents)
            .flatMap {
                Observable.fromCallable {  JsoupNetworkCall.instance.callUrl(this.url, it, link) }
            }.takeUntil { !it.isNullResult() }.map {
                it ?: throw RuntimeException("This $link --> Null or empty response from the server")
            }
    }
}
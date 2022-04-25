package com.devfutech.common

import android.webkit.URLUtil

fun LinkPreviewResult?.isNullResult(): Boolean {
    return (this?.title.isNullOrEmpty() ||
            this?.title.equals("null")) &&
            (this?.description.isNullOrEmpty() ||
                    this?.description.equals(
                        "null"
                    ))
}

fun String.validateUrl(): String {
    val url = if (!this.contains("http")) {
        "http://$this"
    }else{
        this
    }
    //Force url to https
    val httpsUrl = if (URLUtil.isHttpsUrl(url)) {
        url
    } else url.replace(
        "http://",
        "https://"
    )
    //Make sure url have www
    return if (httpsUrl.isUrlHaveWWW()) {
        httpsUrl
    } else {
        StringBuilder(httpsUrl).insert(8, "www.").toString()
    }
}

fun String?.isUrlHaveWWW(): Boolean {
    return null != this &&
            this.length > 11 &&
            this.substring(8, 12).equals("www.", ignoreCase = true)
}
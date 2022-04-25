package com.devfutech.common

import android.webkit.URLUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.net.URI
import java.net.URISyntaxException
import java.net.URL

class JsoupNetworkCall private constructor() {

    companion object {
        val instance: JsoupNetworkCall
            get() = JsoupNetworkCall()
    }

    private val refer = "http://www.google.com"
    private val timeout = 5 * 1000 // 5 Seconds
    private val docSelectQuery = "meta[property^=og:]"
    private val openGraphKey = "content"
    private val property = "property"
    private val ogImage = "og:image"
    private val ogDescription = "og:description"
    private val ogUrl = "og:url"
    private val ogTitle = "og:title"
    private val ogSiteName = "og:site_name"
    private val ogType = "og:type"
    private var title: String? = null
    private var description: String? = null
    private var siteUrl: String? = null
    private var realUrl: String? = null
    private var image: String? = null
    private var siteName: String? = null
    private var type: String? = null

    val agents = mutableListOf(
        "facebookexternalhit/1.1 (+http://www.facebook.com/externalhit_uatext.php)",
        "Mozilla",
        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36",
        "WhatsApp/2.19.81 A",
        "facebookexternalhit/1.1",
        "facebookcatalog/1.0"
    )

    fun callUrl(url: String, agent: String, originalUrl: String): LinkPreviewResult? {
         LinkPreviewResult()
        try {
            val response = Jsoup.connect(url)
                .ignoreContentType(true)
                .userAgent(agent)
                .referrer(refer)
                .timeout(timeout) // default timeout when not set 30 seconds
                .followRedirects(true)
                .execute()

            val doc = response.parse()
            val ogTags = doc.select(docSelectQuery)
            when {
                ogTags.size > 0 ->
                    ogTags.forEachIndexed { index, _ ->
                        val tag = ogTags[index]
                        when (tag.attr(property)) {
                            ogImage -> {
                                image = (tag.attr(openGraphKey))
                            }
                            ogDescription -> {
                                description = (tag.attr(openGraphKey))
                            }
                            ogUrl -> {
                                siteUrl = (tag.attr(openGraphKey))
                            }
                            ogTitle -> {
                                title = (tag.attr(openGraphKey))
                            }
                            ogSiteName -> {
                                siteName = (tag.attr(openGraphKey))
                            }
                            ogType -> {
                                type = (tag.attr(openGraphKey))
                            }
                        }
                    }
            }

            realUrl = originalUrl

            if (title.isNullOrEmpty()) {
                title = doc.title()
            }

            if (description.isNullOrEmpty()) {
                description =
                    if (doc.select("meta[name=description]").size != 0) doc.select("meta[name=description]")
                        .first().attr("content") else ""
            }

            image = checkValidImageMeta(doc, url)

            if (siteUrl?.isEmpty() == true) {
                siteUrl = getBaseUrl(url)
            }

            return LinkPreviewResult(
                title = title,
                description = description,
                url = siteUrl,
                originalUrl = realUrl,
                image = image,
                siteName = siteName,
                type = type
            )
        } catch (e: Exception) {
            return null
        }
    }

    private fun checkValidImageMeta(doc: Document, url: String): String {
        return if (image.isNullOrEmpty() || !URLUtil.isNetworkUrl(image)) {
            val imageExtension = arrayOf("png", "jpg", "jpeg", "gif", "bmp")
            var src: String = doc.select("link[rel=image_src]").attr("href")
            if (src.isNotEmpty()) resolveURL(url, src) else {
                src = doc.select("link[rel=apple-touch-icon]").attr("href")
                if (src.isNotEmpty()) resolveURL(url, src) else {
                    src = doc.select("link[rel=icon]").attr("href")
                    resolveURL(url, src)
                }
            }
            val inValidImage =
                imageExtension.find { it == File(src).extension } != null && !URLUtil.isNetworkUrl(
                    src
                )
            when {
                inValidImage -> "${getBaseUrl(url)}$src"
                URLUtil.isNetworkUrl(src) -> src
                else -> ""
            }
        } else {
            image!!
        }
    }

    private fun resolveURL(url: String, part: String): String {
        return if (URLUtil.isValidUrl(part)) part else {
            try {
                var baseUri = URI(url)
                baseUri = baseUri.resolve(part)
                baseUri.toString()
            } catch (e: URISyntaxException) {
                ""
            }
        }
    }

    private fun getBaseUrl(urlString: String): String {
        val url = if (!urlString.contains("http")) {
            "http://$this"
        } else {
            urlString
        }
        val base: URL = URI.create(url).toURL()
        return base.protocol.toString() + "://" + base.authority
    }
}
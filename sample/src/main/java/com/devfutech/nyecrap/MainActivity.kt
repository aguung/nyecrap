package com.devfutech.nyecrap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.devfutech.common.LinkPreviewResult
import com.devfutech.nyecrap.kotlin_flow.NyecrapFlow
import com.devfutech.nyecrap.rx_java.NyecrapRxJava
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    private val linkForTest = mutableListOf(
        "https://www.linkedin.com/posts/madhusmita-padhy_machinelearning-datascience-activity-6886390508722163712-yhQ0",
        "https://www.youtube.com/watch?v=n3zsoX7bRlc",
        "https://twitter.com",
        "https://stackoverflow.com/questions/44515769/conda-is-not-recognized-as-internal-or-external-command",
        "https://github.com/Priyansh-Kedia/OpenGraphParser",
        "https://chat.whatsapp.com/DdWAKRkt2VfAmd4OS47y7P",
        "https://facebook.com",
        "https://instagram.com/fcbarcelona?utm_medium=copy_link",
        "https://www.facebook.com/groups/777946865955982/permalink/1385110621906267/",
        "https://www.youtube.com/",
        "https://www.instagram.com/",
        "https://klusterapp.io/kluster/message?message_id=7244&channel_id=2247&Channel_name=new-channel-with-spa&kluster_id=64",
        "https://duniagames.co.id/discover/article/kru-big-mom-yang-kuat-menghadapi-akainu-one-piece",
        "https://developer.twitter.com/en",
        "https://stage2.duniagames.co.id/discover/article/mpl-season-4-day-2-week-2-recap-late-coming-aura-esports-declared-defeated-2"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linkForTest.forEach {
//            scrapRxJava(it)
            scrapCorountine(it)
        }
    }

    private fun scrapCorountine(url: String) {
        lifecycleScope.launchWhenStarted {
            NyecrapFlow
                .instance
                .getLinkPreview(url)
                .catch { error ->
                    Log.d(localClassName, "$error")
                }.collectLatest { result ->
                    Log.d(localClassName, "$url ---> $result")
                }
        }
    }

    private fun scrapRxJava(url: String) {
        NyecrapRxJava.instance.getLinkPreview(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: LinkPreviewResult? ->
                Log.d(localClassName, "$url ---> $result")
            }) {
                Log.d(localClassName, "$it")
            }
    }
}
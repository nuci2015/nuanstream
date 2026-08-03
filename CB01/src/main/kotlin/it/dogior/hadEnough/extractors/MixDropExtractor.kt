Package it.dogior.hadEnough.extractors

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

class MixDropExtractor : ExtractorApi() {
    Override val name = "MixDrop"
    Override val mainUrl = "https://cb01uno.bond"
    Override val requiresReferer = false

    Companion object {
        Private const val TAG = "MixDropExtractor"
        Private const val TIMEOUT_SECONDS = 30L
        
        Private fun getApplicationContext(): Context? {
            Return try {
                Val activityThreadClass = Class.forName("android.app.ActivityThread")
                Val currentActivityThreadMethod = activityThreadClass.getMethod("currentActivityThread")
                Val activityThread = currentActivityThreadMethod.invoke(null)
                Val getApplicationMethod = activityThreadClass.getMethod("getApplication")
                GetApplicationMethod.invoke(activityThread) as? Application
            } catch (e: Exception) {
                Log.e(TAG, "Failed to get Application context: ${e.message}")
                Null
            }
        }
        
        Private fun isVideoUrl(url: String): Boolean {
            Val lowerUrl = url.lowercase()
            Return lowerUrl.contains(".mp4") || 
                   LowerUrl.contains(".m3u8") || 
                   LowerUrl.contains("delivery") || 
                   LowerUrl.contains("v2/hls") ||
                   LowerUrl.contains(".ts")
        }
    }

    Override suspend fun getUrl(
        Url: String,
        Referer: String?,
        SubtitleCallback: (SubtitleFile) -> Unit,
        Callback: (ExtractorLink) -> Unit,
    ) {
        Val baseDomain = url.substringBefore("/e/")
        Val embedUrl = if (url.contains("/e/")) url else {
            Val id = url.substringAfterLast("/").trim()
            Url.replaceAfterLast("/", "e/$id")
        }
        
        Log.d(TAG, "Sniffing su dominio: $embedUrl")
        Val videoUrl = extractWithWebView(embedUrl)

        If (videoUrl != null) {
            Callback.invoke(
                NewExtractorLink(
                    Source = name,
                    Name = "MixDrop",
                    Url = videoUrl,
                    Type = ExtractorLinkType.VIDEO
                ) {
                    This.headers = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36",
                        "Accept" to "*/*"
                    )
                    This.referer = if (baseDomain.startsWith("http")) "$baseDomain/" else "https://cb01uno.bond/"
                }
            )
        }
    }
    
    Private suspend fun extractWithWebView(embedUrl: String): String? {
        Return suspendCancellableCoroutine { continuation ->
            Val latch = CountDownLatch(1)
            Var extractedUrl: String? = null
            Var found = false
            
            Handler(Looper.getMainLooper()).post {
                Try {
                    Val context = getApplicationContext() ?: run {
                        Continuation.resume(null)
                        Return@post
                    }
                    
                    @SuppressLint("SetJavaScriptEnabled")
                    Val webView = WebView(context)
                    
                    WebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                    
                    WebView.settings.apply {
                        JavaScriptEnabled = true
                        DomStorageEnabled = true
                        // databaseEnabled rimossa per deprecazione (Gestito implicitamente o obsoleto nelle API recenti)
                        JavaScriptCanOpenWindowsAutomatically = true
                        MediaPlaybackRequiresUserGesture = false
                        UserAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36"
                    }
                    
                    WebView.webViewClient = object : WebViewClient() {
                        Override fun shouldInterceptRequest(
                            View: WebView?,
                            Request: WebResourceRequest?
                        ): WebResourceResponse? {
                            Val requestUrl = request?.url.toString()
                            
                            If (!found && isVideoUrl(requestUrl)) {
                                Found = true
                                ExtractedUrl = requestUrl
                                Log.i(TAG, "!!! TARGET ACQUIRED !!! -> $requestUrl")
                                
                                Handler(Looper.getMainLooper()).post {
                                    WebView.stopLoading()
                                    WebView.destroy()
                                    Latch.countDown()
                                    Continuation.resume(requestUrl)
                                }
                                Return WebResourceResponse("text/plain", "utf-8", "".byteInputStream())
                            }
                            
                            Return super.shouldInterceptRequest(view, request)
                        }
                    }
                    
                    WebView.loadUrl(embedUrl)
                    
                    Val handler = Handler(Looper.getMainLooper())
                    Val clicker = object : Runnable {
                        Var count = 0
                        Override fun run() {
                            If (!found && count < 8) {
                                WebView.evaluateJavascript("""
                                    (function() {
                                        Var v = document.querySelector('video');
                                        If(v) { v.muted = true; v.play(); }
                                        Document.querySelector('.vjs-big-play-button, #vplayer, .play-button, div[id*="player"]')?.click();
                                    })();
                                """.trimIndent(), null)
                                Count++
                                Handler.postDelayed(this, 2000)
                            }
                        }
                    }
                    Handler.postDelayed(clicker, 2500)
                    
                    Handler(Looper.getMainLooper()).postDelayed({
                        If (!found) {
                            Log.w(TAG, "Timeout: Nessun link rilevato su questo dominio")
                            WebView.stopLoading()
                            WebView.destroy()
                            Latch.countDown()
                            Continuation.resume(null)
                        }
                    }, TimeUnit.SECONDS.toMillis(TIMEOUT_SECONDS))
                    
                } catch (e: Exception) {
                    Continuation.resume(null)
                }
            }
            
            Latch.await(TIMEOUT_SECONDS + 5, TimeUnit.SECONDS)
        }
    }
}

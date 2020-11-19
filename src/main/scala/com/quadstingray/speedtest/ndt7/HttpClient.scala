package com.quadstingray.speedtest.ndt7

import java.net.URI
import java.util.Date
import java.util.concurrent.TimeUnit

import com.quadstingray.speedtest.BuildInfo
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import javax.net.ssl.SSLSession
import okhttp3.{ OkHttpClient, Request }

trait HttpClient extends LazyLogging {
  lazy val useragent: String = {
    val defaultUseragent = "%s.%s/%s %s %s".format(BuildInfo.organization, BuildInfo.name, BuildInfo.version, BuildInfo.gitLastCommitHash, new Date().getTime)
    try {
      val useragent = System.getProperty("com.quadstingray.speedtest.useragent")
      if (useragent != null && useragent.nonEmpty) {
        useragent
      }
      else {
        val useragent = ConfigFactory.defaultApplication().getString("com.quadstingray.speedtest.useragent")
        if (useragent.nonEmpty) {
          useragent
        }
        else {
          defaultUseragent
        }
      }
    } catch {
      case _: Exception =>
        defaultUseragent
    }
  }

  private[ndt7] def buildRequest(uri: URI) = {
    val request = new Request.Builder()
      .url(uri.toString)
      .addHeader("Sec-WebSocket-Protocol", "net.measurementlab.ndt.v7")
      .addHeader("User-Agent", useragent)
      .build()
    request
  }

  private[ndt7] def httpClient() = {
    val builder = new OkHttpClient.Builder()

    builder.hostnameVerifier((hostname: String, session: SSLSession) => true)

    val client = builder
      .connectTimeout(5, TimeUnit.SECONDS)
      .readTimeout(5, TimeUnit.SECONDS)
      .writeTimeout(5, TimeUnit.SECONDS)
      .build()

    client
  }

}

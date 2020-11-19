package com.quadstingray.speedtest.ndt7

import java.net.URI

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonValueCodec, _}
import com.github.plokhotnyuk.jsoniter_scala.macros._
import com.quadstingray.speedtest.ndt7.lib.{Server, ServerResult}

case class ServerClient() extends HttpClient {
  implicit private val serverV2codec: JsonValueCodec[Server]                 = JsonCodecMaker.make[Server]
  implicit private val serverV2Listcodec: JsonValueCodec[ServerResult]       = JsonCodecMaker.make[ServerResult]

  def nextServer: Server = {
    val uri = new URI("https://locate.measurementlab.net/v2/nearest/ndt/ndt7")
    val response = httpClient().newCall(buildRequest(uri)).execute()
    readFromString[ServerResult](response.body().string()).results.head
  }

}

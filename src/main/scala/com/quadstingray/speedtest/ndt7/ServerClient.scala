package com.quadstingray.speedtest.ndt7

import java.net.URI

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.quadstingray.speedtest.ndt7.lib.Server
import com.github.plokhotnyuk.jsoniter_scala.macros._
import com.github.plokhotnyuk.jsoniter_scala.core._

case class ServerClient() extends HttpClient {
  implicit private val codec: JsonValueCodec[Server] = JsonCodecMaker.make[Server]
  implicit private val codec2: JsonValueCodec[List[Server]] = JsonCodecMaker.make[List[Server]]

  def serverBySite(site: String): Option[Server] = {
    allServer.find(_.site == site)
  }

  def allServer: List[Server] = {
    val uri = new URI("http://locate.measurementlab.net/ndt?format=json&policy=all")
    val responseString = httpClient().newCall(buildRequest(uri)).execute().body().string()
    readFromString[List[Server]](responseString)
  }

  def nextServer: Server = {
    val uri = new URI("https://locate.measurementlab.net/ndt_ssl?format=json")
    val responseString = httpClient().newCall(buildRequest(uri)).execute().body().string()
    readFromString[Server](responseString)
  }
}

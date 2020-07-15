package com.quadstingray.speedtest.ndt7

import java.net.URI

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonValueCodec, _}
import com.github.plokhotnyuk.jsoniter_scala.macros._
import com.quadstingray.speedtest.ndt7.lib.{Server, ServerDetails}

case class ServerClient() extends HttpClient {
  implicit private val servercodec: JsonValueCodec[Server]        = JsonCodecMaker.make[Server]
  implicit private val serverListCodec: JsonValueCodec[List[Server]] = JsonCodecMaker.make[List[Server]]
  implicit private val serverDetailsCodec: JsonValueCodec[List[ServerDetails]] = JsonCodecMaker.make[List[ServerDetails]]

  def serverBySite(site: String): Option[Server] =
    allServer.find(_.site == site)

  def allServer: List[Server] = {
    val uri            = new URI("http://locate.measurementlab.net/ndt?format=json&policy=all")
    val responseString = httpClient().newCall(buildRequest(uri)).execute().body().string()
    readFromString[List[Server]](responseString)
  }

  def serverDetailsBySite(site: String): Option[ServerDetails] =
    allServerDetails.find(_.site == site)

  def allServerDetails: List[ServerDetails] = {
    val uri            = new URI("https://siteinfo.mlab-oti.measurementlab.net/v1/sites/locations.json")
    val response = httpClient().newCall(buildRequest(uri)).execute()
    readFromString[List[ServerDetails]](response.body().string())
  }

  def nextServer: Server = {
    val uri            = new URI("https://locate.measurementlab.net/ndt_ssl?format=json")
    val response = httpClient().newCall(buildRequest(uri)).execute()
    readFromString[Server](response.body().string())
  }

}

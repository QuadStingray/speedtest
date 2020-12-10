package com.quadstingray.speedtest.ndt7

import java.net.URI

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonValueCodec, _}
import com.github.plokhotnyuk.jsoniter_scala.macros._
import com.quadstingray.speedtest.ndt7.lib.{Server, ServerDetails, ServerResult}

case class ServerClient() extends HttpClient {
  implicit private val serverCodec: JsonValueCodec[Server]                 = JsonCodecMaker.make[Server]
  implicit private val serverListCodec: JsonValueCodec[ServerResult]       = JsonCodecMaker.make[ServerResult]
  implicit private val serverDetailsCodec: JsonValueCodec[List[ServerDetails]] = JsonCodecMaker.make[List[ServerDetails]]

  def nextServer: Server = {
    val uri = new URI("https://locate.measurementlab.net/v2/nearest/ndt/ndt7")
    val response = httpClient().newCall(buildRequest(uri)).execute()
    readFromString[ServerResult](response.body().string()).results.head
  }

  def serverDetailsByServer(server : Server) = {
    serverDetailsBySite(server.siteId)
  }

  def serverDetailsBySite(siteId: String): Option[ServerDetails] =
    allServerDetails.find(_.site == siteId)

  def allServerDetails: List[ServerDetails] = {
    val uri            = new URI("https://siteinfo.mlab-oti.measurementlab.net/v1/sites/locations.json")
    val response = httpClient().newCall(buildRequest(uri)).execute()
    readFromString[List[ServerDetails]](response.body().string())
  }
}

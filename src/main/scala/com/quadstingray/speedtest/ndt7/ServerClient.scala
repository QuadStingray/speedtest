package com.quadstingray.speedtest.ndt7

import java.net.URI

import com.quadstingray.speedtest.ndt7.lib.Server
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.read

case class ServerClient() extends HttpClient {
  implicit private val formats: DefaultFormats.type = DefaultFormats

  def serverBySite(site: String): Option[Server] = {
    allServer.find(_.site == site)
  }

  def allServer: List[Server] = {
    val uri = new URI("http://locate.measurementlab.net/ndt?format=json&policy=all")
    val responseString = httpClient().newCall(buildRequest(uri)).execute().body().string()
    read[List[Server]](responseString)
  }

  def nextServer: Server = {
    val uri = new URI("https://locate.measurementlab.net/ndt_ssl?format=json")
    val responseString = httpClient().newCall(buildRequest(uri)).execute().body().string()
    read[Server](responseString)
  }
}

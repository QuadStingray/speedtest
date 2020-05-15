package com.quadstingray.speedtest.ndt7.lib

case class ConnectionInfo(client: String, server: String)

object ConnectionInfo {
  def apply(): ConnectionInfo = new ConnectionInfo("UNKNOWN", "UNKNOWN")
}
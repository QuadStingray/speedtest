package com.quadstingray.speedtest.ndt7.lib.api

private[ndt7] case class Measurement(Origin: Option[String],
                                     Test: Option[String],
                                     ConnectionInfo: Option[ConnectionInfo],
                                     BBRInfo: Option[BbrInfo]
//                                     ,TCPInfo: Option[TcpInfo]
                                    )

private[ndt7] object Measurement {
  def apply(): Measurement = new Measurement(None, None, None, None)
}
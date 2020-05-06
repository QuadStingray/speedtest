package com.quadstingray.speedtest.ndt7.lib.api

private[ndt7] case class Measurement(Origin: Option[String],
                                     Test: Option[String],
                                     ConnectionInfo: Option[ConnectionInfo],
                                     TcpInfo: Option[TcpInfo],
                                     BBRInfo: Option[BBRInfo])

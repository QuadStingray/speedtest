package com.quadstingray.speedtest.ndt7.lib.api

private[ndt7] case class Measurement(Origin: Option[String] = None,
                                     Test: Option[String] = None,
                                     ConnectionInfo: Option[ConnectionInfo] = None,
//                                     TCPInfo: Option[TcpInfo] = None,
                                     BBRInfo: Option[BBRInfo]= None)

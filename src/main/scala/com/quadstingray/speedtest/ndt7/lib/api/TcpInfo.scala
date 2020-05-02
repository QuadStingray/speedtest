package com.quadstingray.speedtest.ndt7.lib.api

case class TcpInfo(
                    BusyTime: Option[Long],
                    BytesAcked: Option[Long],
                    BytesReceived: Option[Long],
                    BytesSent: Option[Long],
                    BytesRetrans: Option[Long],
                    ElapsedTime: Option[Long],
                    RTT: Option[Long],
                    RTTVar: Option[Long],
                    RWndLimited: Option[Long],
                    SndBufLimited: Option[Long]
                  )

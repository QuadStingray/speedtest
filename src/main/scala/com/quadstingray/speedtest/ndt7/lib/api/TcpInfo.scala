package com.quadstingray.speedtest.ndt7.lib.api

private[ndt7] case class TcpInfo(
    BytesSent: Option[Long],
    BytesRetrans: Option[Long],
    BusyTime: Option[Long],
    BytesAcked: Option[Long],
    BytesReceived: Option[Long],
    ElapsedTime: Option[Long],
    Retransmits: Option[Long],
    RTT: Option[Long],
    RTTVar: Option[Long],
    RWndLimited: Option[Long],
    SndBufLimited: Option[Long]
)

private[ndt7] object TcpInfo {
  def apply(): TcpInfo = new TcpInfo(None, None, None, None, None, None, None, None, None, None, None)
}

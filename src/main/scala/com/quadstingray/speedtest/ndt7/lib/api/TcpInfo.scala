package com.quadstingray.speedtest.ndt7.lib.api

private[ndt7] case class TcpInfo(
    BytesSent: Option[Long] = None,
    BytesRetrans: Option[Long] = None,
    BusyTime: Option[Long] = None,
    BytesAcked: Option[Long] = None,
    BytesReceived: Option[Long] = None,
    ElapsedTime: Option[Long] = None,
    Retransmits: Option[Long] = None,
    RTT: Option[Long] = None,
    RTTVar: Option[Long] = None,
    RWndLimited: Option[Long] = None,
    SndBufLimited: Option[Long] = None
)

package com.quadstingray.speedtest.ndt7.lib.api

private[ndt7] case class BBRInfo(
    BW: Option[Long] = None,
    MinRTT: Option[Long] = None, // latency
    PacingGain: Option[Long] = None,
    CwndGain: Option[Long] = None,
    ElapsedTime: Option[Long] = None
)

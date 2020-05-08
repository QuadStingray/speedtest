package com.quadstingray.speedtest.ndt7.lib.api

private[ndt7] case class BbrInfo(
    BW: Option[Long],
    MinRTT: Option[Long],
//    PacingGain: Option[Long],
//    CwndGain: Option[Long],
    ElapsedTime: Option[Long]
)

private[ndt7] object BbrInfo {
  def apply(): BbrInfo = new BbrInfo(None, None, None)
}
package com.quadstingray.speedtest.ndt7.lib

import com.github.plokhotnyuk.jsoniter_scala.core._
import com.github.plokhotnyuk.jsoniter_scala.macros._

case class MeasurementResult(testKind: String, bandwidth: Bandwidth, connectionInfo: ConnectionInfo, usedBytes: Long, latency: Option[Long] = None) {
  implicit private val codec: JsonValueCodec[MeasurementResult] = JsonCodecMaker.make[MeasurementResult]

  def toJson: String =
    writeToString(this)
}

object MeasurementResult {
  val TestKindUpload: String   = "UPLOAD"
  val TestKindDownload: String = "DOWNLOAD"
}

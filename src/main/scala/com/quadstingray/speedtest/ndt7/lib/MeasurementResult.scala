package com.quadstingray.speedtest.ndt7.lib

import com.github.plokhotnyuk.jsoniter_scala.macros._
import com.github.plokhotnyuk.jsoniter_scala.core._

case class MeasurementResult(bandwidth: Bandwidth, connectionInfo: ConnectionInfo, usedBytes: Long) {
  implicit val codec: JsonValueCodec[MeasurementResult] = JsonCodecMaker.make[MeasurementResult]
  def toJson: String = {
    writeToString(this)
  }
}


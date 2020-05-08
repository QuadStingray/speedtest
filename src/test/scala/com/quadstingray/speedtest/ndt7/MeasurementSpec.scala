package com.quadstingray.speedtest.ndt7

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonValueCodec, readFromString}
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import com.quadstingray.speedtest.ndt7.lib.Server
import com.quadstingray.speedtest.ndt7.lib.api.Measurement
import com.typesafe.scalalogging.LazyLogging
import org.specs2.mutable.Specification

import scala.io.Source

class MeasurementSpec extends Specification with LazyLogging {
  sequential

  val serverForTest: Server = ServerClient().nextServer

  "Measurement Spec" >> {

    "read measurement" >> {
      implicit val codec: JsonValueCodec[Measurement] = JsonCodecMaker.make[Measurement]
      val lines = Source.fromResource("measurement.json").getLines()
      val content = lines.mkString(" ")
      val measurement = readFromString[Measurement](content)
      measurement.BBRInfo must beSome
      measurement.ConnectionInfo must beSome
    }

  }

}

package com.quadstingray.speedtest.ndt7

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonValueCodec, readFromString}
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import com.quadstingray.speedtest.ndt7.lib.api.Measurement
import com.quadstingray.speedtest.ndt7.lib.{MeasurementResult, Server}
import com.typesafe.scalalogging.LazyLogging
import org.specs2.mutable.Specification

import scala.io.Source

class MesurementSpec extends Specification with LazyLogging {
  sequential

  val serverForTest: Server = ServerClient().nextServer

  "Mesurement Spec" >> {

    "read mesurement" >> {
      implicit val codec: JsonValueCodec[Measurement] = JsonCodecMaker.make[Measurement]
      val lines = Source.fromResource("mesurement.json").getLines()
      val content = lines.mkString(" ")
      val mesurement = readFromString[Measurement](content)
      mesurement.BBRInfo must beSome
      mesurement.ConnectionInfo must beSome
    }

  }

}

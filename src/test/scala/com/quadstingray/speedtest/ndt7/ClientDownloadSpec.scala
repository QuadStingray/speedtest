package com.quadstingray.speedtest.ndt7

import com.quadstingray.speedtest.ndt7.lib.{MeasurementResult, Server}
import com.typesafe.scalalogging.LazyLogging
import org.specs2.mutable.Specification

class ClientDownloadSpec extends Specification with LazyLogging {
  sequential

  val serverForTest: Server = ServerClient().nextServer

  "ClientDownload" >> {

    "run Download" >> {
      val client = TestClient(serverForTest)
      val test   = client.runDownload()
      test.bandwidth.megaBitPerSecond must beGreaterThan(50.0)
    }

    "run Download with Callback" >> {

      var count        = 0
      var bandwidthSum = 0.0
      def callBack(result: MeasurementResult): Unit = {
        count += 1
        bandwidthSum += result.bandwidth.bytePerSeconds
        logger.trace(result.bandwidth.toString)
      }

      val client = TestClient(serverForTest)
      val test   = client.runDownload(callBack)
      test.bandwidth.megaBitPerSecond must beGreaterThan(50.0)
      count must beGreaterThan(5)
      bandwidthSum must beGreaterThan(test.bandwidth.bytePerSeconds)
      bandwidthSum / count must beLessThanOrEqualTo(test.bandwidth.bytePerSeconds)
    }

  }

}

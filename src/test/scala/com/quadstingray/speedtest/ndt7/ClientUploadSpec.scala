package com.quadstingray.speedtest.ndt7

import com.quadstingray.speedtest.ndt7.lib.{MeasurementResult, Server}
import org.specs2.mutable.Specification

class ClientUploadSpec extends Specification {
  sequential

  val serverForTest: Server = ServerClient().nextServer

  "ClientUpload" >> {

    "run Upload" >> {
      val client = TestClient(serverForTest)
      val test   = client.runUpload()
      test.bandwidth.megaBitPerSecond must beGreaterThan(5.0)
    }

    "run Upload with Callback" >> {

      var count        = 0
      var bandwidthSum = 0.0
      def callBack(result: MeasurementResult): Unit = {
        count += 1
        bandwidthSum += result.bandwidth.bytePerSeconds
      }

      val client = TestClient(serverForTest)
      val test   = client.runDownload(callBack)
      test.bandwidth.megaBitPerSecond must beGreaterThan(5.0)
      count must beGreaterThan(5)
      bandwidthSum must beGreaterThan(test.bandwidth.bytePerSeconds)
    }

  }

}

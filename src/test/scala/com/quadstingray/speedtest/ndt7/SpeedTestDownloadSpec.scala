package com.quadstingray.speedtest.ndt7

import com.quadstingray.speedtest.ndt7.lib.{MeasurementResult, Server}
import org.specs2.mutable.Specification

class SpeedTestDownloadSpec extends Specification {
  sequential

  "SpeedTest" >> {

    "run upload without any settings" >> {
      //#auto-download-test-without
      val speedTestResult = SpeedTest.runDownload()
      //#auto-download-test-without
      speedTestResult.bandwidth.megaBitPerSecond must beGreaterThan(10.0)
      speedTestResult.latency.get must beGreaterThanOrEqualTo(100L)
    }

    "run Test with callbacks" >> {
      //#auto-download-test-callbacks
      var dlCount        = 0
      var dlBandwidthSum = 0.0
      def dlCallBack(result: MeasurementResult): Unit = {
        dlCount += 1
        dlBandwidthSum += result.bandwidth.bytePerSeconds
      }

      val speedTestResult = SpeedTest.runDownload(downloadMeasurementCallBack = dlCallBack)
      //#auto-download-test-callbacks

      speedTestResult.bandwidth.megaBitPerSecond must beGreaterThan(10.0)
      speedTestResult.latency.get must beGreaterThan(100L)

    }

  }

}

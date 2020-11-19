package com.quadstingray.speedtest.ndt7

import com.quadstingray.speedtest.ndt7.lib.{MeasurementResult, Server}
import org.specs2.mutable.Specification

class SpeedTestSpec extends Specification {
  sequential

  "SpeedTest" >> {

    "run Test without any settings" >> {
      //#auto-full-test-without
      val speedTestResult = SpeedTest.runTest()
      //#auto-full-test-without
      speedTestResult.download.megaBitPerSecond must beGreaterThan(10.0)
      speedTestResult.upload.megaBitPerSecond must beGreaterThan(5.0)
      speedTestResult.testResults.size must beEqualTo(2)
      speedTestResult.latency must beGreaterThanOrEqualTo(100L)
    }

    "run Test with callbacks" >> {
      //#auto-full-test-callbacks
      var dlCount        = 0
      var dlBandwidthSum = 0.0
      def dlCallBack(result: MeasurementResult): Unit = {
        dlCount += 1
        dlBandwidthSum += result.bandwidth.bytePerSeconds
      }

      var upCount        = 0
      var upBandwidthSum = 0.0
      def upCallBack(result: MeasurementResult): Unit = {
        upCount += 1
        upBandwidthSum += result.bandwidth.bytePerSeconds
      }

      val speedTestResult = SpeedTest.runTest(downloadMeasurementCallBack = dlCallBack, uploadMeasurementCallBack = upCallBack)
      //#auto-full-test-callbacks

      speedTestResult.download.megaBitPerSecond must beGreaterThan(10.0)
      speedTestResult.upload.megaBitPerSecond must beGreaterThan(5.0)
      speedTestResult.testResults.size must beEqualTo(2)
      speedTestResult.latency must beGreaterThan(100L)

    }

  }

}

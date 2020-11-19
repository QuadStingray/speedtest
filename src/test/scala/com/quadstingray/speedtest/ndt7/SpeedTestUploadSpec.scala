package com.quadstingray.speedtest.ndt7

import com.quadstingray.speedtest.ndt7.lib.{MeasurementResult, Server}
import org.specs2.mutable.Specification

class SpeedTestUploadSpec extends Specification {
  sequential

  "SpeedTest" >> {

    "run upload without any settings" >> {
      //#auto-upload-test-without
      val speedTestResult = SpeedTest.runUpload()
      //#auto-upload-test-without
      speedTestResult.bandwidth.megaBitPerSecond must beGreaterThan(10.0)
      speedTestResult.latency.get must beGreaterThanOrEqualTo(100L)
    }

    "run Test with callbacks" >> {
      //#auto-upload-test-callbacks
      var upCount        = 0
      var upBandwidthSum = 0.0
      def upCallBack(result: MeasurementResult): Unit = {
        upCount += 1
        upBandwidthSum += result.bandwidth.bytePerSeconds
      }

      val speedTestResult = SpeedTest.runUpload(uploadMeasurementCallBack = upCallBack)
      //#auto-upload-test-callbacks

      speedTestResult.bandwidth.megaBitPerSecond must beGreaterThan(10.0)
      speedTestResult.latency.get must beGreaterThan(100L)

    }

  }

}

package com.quadstingray.speedtest.ndt7

import com.quadstingray.speedtest.ndt7.lib.{MeasurementResult, Server}
import org.specs2.mutable.Specification

class SpeedTestSpec extends Specification {
  sequential

  "SpeedTest" >> {

    "run Test without any settings" >> {
      val speedTestResult = SpeedTest.runTest()
      speedTestResult.download.megaBitPerSecond must beGreaterThan(50.0)
      speedTestResult.upload.megaBitPerSecond must beGreaterThan(5.0)
      speedTestResult.testResults.size must beEqualTo(2)
      speedTestResult.latency.toLong must beGreaterThanOrEqualTo(1000L)
    }

    "run Test with given server" >> {
      val speedTestResult = SpeedTest.runTest(
        Some(Server("fra05", "ndt-iupui-mlab1-fra05.measurement-lab.org", "Frankfurt", "DE", List("193.142.125.24", "2a01:3e0:ff20:401::24")))
      )
      speedTestResult.download.megaBitPerSecond must beGreaterThan(50.0)
      speedTestResult.upload.megaBitPerSecond must beGreaterThan(5.0)
      speedTestResult.testResults.size must beEqualTo(2)
      speedTestResult.latency.toLong must beGreaterThanOrEqualTo(1000L)
    }

    "run Test with not existing server" >> {
      val speedTestResult = SpeedTest.runTest(Some(Server("ber01", "ndt-iupui-mlab1-ber01.measurement-lab.org", "Berlin", "DE", List())))
      speedTestResult.download.megaBitPerSecond must beGreaterThan(50.0)
      speedTestResult.upload.megaBitPerSecond must beGreaterThan(5.0)
      speedTestResult.testResults.size must beEqualTo(2)
      speedTestResult.latency.toLong must beGreaterThanOrEqualTo(1000L)
    }

    "run Test with callbacks" >> {

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
      speedTestResult.download.megaBitPerSecond must beGreaterThan(50.0)
      speedTestResult.upload.megaBitPerSecond must beGreaterThan(5.0)
      speedTestResult.testResults.size must beEqualTo(2)
      speedTestResult.latency must beGreaterThan(1000l)

    }

  }

}

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

    "run Test with given server" >> {
      //#auto-download-test-server
      val speedTestResult = SpeedTest.runDownload(
        Some(Server("fra05", "ndt-iupui-mlab1-fra05.measurement-lab.org", "Frankfurt", "DE", List("193.142.125.24", "2a01:3e0:ff20:401::24")))
      )
      //#auto-download-test-server
      speedTestResult.bandwidth.megaBitPerSecond must beGreaterThan(5.0)
      speedTestResult.latency.get must beGreaterThanOrEqualTo(100L)
    }

    "run Test with not existing server" >> {
      val speedTestResult = SpeedTest.runDownload(Some(Server("ber01", "ndt-iupui-mlab1-ber01.measurement-lab.org", "Berlin", "DE", List())))
      speedTestResult.bandwidth.megaBitPerSecond must beGreaterThan(5.0)
      speedTestResult.latency.get must beGreaterThanOrEqualTo(100L)
    }

    "run Test with callbacks" >> {
      //#auto-download-test-callbacks
      var upCount = 0
      var upBandwidthSum = 0.0

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

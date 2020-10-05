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

    "run Test with given server" >> {
      //#auto-upload-test-server
      val speedTestResult = SpeedTest.runUpload(
        Some(Server("fra05", "ndt-iupui-mlab1-fra05.measurement-lab.org", "Frankfurt", "DE", List("193.142.125.24", "2a01:3e0:ff20:401::24")))
      )
      //#auto-upload-test-server
      speedTestResult.bandwidth.megaBitPerSecond must beGreaterThan(5.0)
      speedTestResult.latency.get must beGreaterThanOrEqualTo(100L)
    }

    "run Test with not existing server" >> {
      val speedTestResult = SpeedTest.runUpload(Some(Server("ber01", "ndt-iupui-mlab1-ber01.measurement-lab.org", "Berlin", "DE", List())))
      speedTestResult.bandwidth.megaBitPerSecond must beGreaterThan(5.0)
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

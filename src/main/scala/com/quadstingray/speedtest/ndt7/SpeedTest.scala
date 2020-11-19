package com.quadstingray.speedtest.ndt7

import com.quadstingray.speedtest.ndt7.lib.{ MeasurementResult, Server, SpeedTestResult }
import com.typesafe.scalalogging.LazyLogging

object SpeedTest extends LazyLogging {

  def runTest(downloadMeasurementCallBack: MeasurementResult => Unit = defaultMeasurementCallback,
              uploadMeasurementCallBack: MeasurementResult => Unit = defaultMeasurementCallback): SpeedTestResult = {

    val serverClient = ServerClient()
    val server = serverClient.nextServer

    val testClient     = TestClient(server)
    val downloadResult = testClient.runDownload(downloadMeasurementCallBack)
    val uploadResult   = testClient.runUpload(uploadMeasurementCallBack)
    SpeedTestResult(
      downloadResult.bandwidth,
      uploadResult.bandwidth,
      downloadResult.latency.getOrElse(-1),
      uploadResult.connectionInfo,
      server,
      List(downloadResult, uploadResult)
    )
  }

  def runDownload(downloadMeasurementCallBack: MeasurementResult => Unit = defaultMeasurementCallback): MeasurementResult = {

    val serverClient = ServerClient()
    val server = serverClient.nextServer

    val testClient     = TestClient(server)
    val downloadResult = testClient.runDownload(downloadMeasurementCallBack)
    downloadResult
  }

  def runUpload(uploadMeasurementCallBack: MeasurementResult => Unit = defaultMeasurementCallback): MeasurementResult = {
    val serverClient = ServerClient()

    val server = serverClient.nextServer

    val testClient     = TestClient(server)
    val uploadResult   = testClient.runUpload(uploadMeasurementCallBack)
    uploadResult
  }

  def defaultMeasurementCallback(measurement: MeasurementResult): Unit =
    logger.info("intermediate measurement: %s".format(measurement))

}

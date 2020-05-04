package com.quadstingray.speedtest.ndt7

import com.quadstingray.speedtest.ndt7.lib.{MeasurementResult, Server, SpeedTestResult}
import com.typesafe.scalalogging.LazyLogging

object SpeedTest extends LazyLogging {

  def runTest(specificServer: Option[Server] = None, downloadMeasurementCallBack: (MeasurementResult) => Unit = defaultMeasurementCallback, uploadMeasurementCallBack: (MeasurementResult) => Unit = defaultMeasurementCallback): SpeedTestResult = {

    val server = {
      val serverClient = ServerClient()
      if (specificServer.isDefined) {
        serverClient.serverBySite(specificServer.get.site).getOrElse(serverClient.nextServer)
      } else {
        serverClient.nextServer
      }
    }

    val testClient = TestClient(server)
    val downloadResult = testClient.runDownload(downloadMeasurementCallBack)
    val uploadResult = testClient.runUpload(uploadMeasurementCallBack)
    SpeedTestResult(downloadResult.bandwidth, uploadResult.bandwidth, uploadResult.connectionInfo, server, List(downloadResult, uploadResult))
  }

  def defaultMeasurementCallback(measurement: MeasurementResult): Unit = {
    logger.info("intermediate measurement: %s".format(measurement))
  }

}

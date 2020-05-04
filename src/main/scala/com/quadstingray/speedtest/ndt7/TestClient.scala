package com.quadstingray.speedtest.ndt7

import java.net.URI
import java.util.concurrent.TimeUnit

import com.quadstingray.speedtest.ndt7.lib.api.Measurement
import com.quadstingray.speedtest.ndt7.lib.{Bandwidth, ConnectionInfo, MeasurementResult, Server}
import com.quadstingray.speedtest.ndt7.listener.{DownloadSocketListener, UploadSocketListener}
import okhttp3._
import okio.ByteString

import scala.concurrent.duration._
import scala.util.Random

case class TestClient(server: Server) extends HttpClient {
  private var testRunning: Boolean = false
  private var firstRequestTime: Long = 0
  private var lastRequestTime: Long = 0
  private var lastMeasurementResult: MeasurementResult = _
  protected var measurementCallBack: (MeasurementResult) => Unit = defaultMeasurementCallback

  def runUpload(intermediateMeasurementCallBack: (MeasurementResult) => Unit = defaultMeasurementCallback): MeasurementResult = {
    measurementCallBack = intermediateMeasurementCallBack
    if (testRunning) {
      throw new Exception("Test is already running")
    }

    val uri: URI = new URI("wss://" + server.fqdn + "/ndt/v7/upload")
    val client: OkHttpClient = httpClient()
    val request: Request = buildRequest(uri)

    var lastSocketMessage: Measurement = Measurement(None, None, None, None, null, null)

    def updateMeasurement(ms: Measurement): Unit = lastSocketMessage = ms

    val socket = client.newWebSocket(request, UploadSocketListener(updateMeasurement))

    var count: Long = 0

    val minMessageSize = 1 << 13
    val maxMessageSize = 1 << 24

    firstRequestTime = System.nanoTime()
    lastRequestTime = System.nanoTime()
    while ((lastRequestTime - firstRequestTime).nanos.toSeconds < 8) {
      val byteCount = if (count > minMessageSize * 5) maxMessageSize else minMessageSize
      val message = ByteString.of(Random.nextBytes(byteCount), 0, byteCount)
      count = count + byteCount
      socket.send(message)
      while (socket.queueSize() > 0) {}
      lastRequestTime = System.nanoTime()
      lastMeasurementResult = intermediateCallBack(count, lastSocketMessage)
    }
    socket.close(1000, null)

    val executorService = client.dispatcher().executorService()
    executorService.shutdown()
    executorService.awaitTermination(Int.MaxValue, TimeUnit.MINUTES)

    testRunning = false
    lastMeasurementResult
  }

  def runDownload(intermediateMeasurementCallBack: (MeasurementResult) => Unit = defaultMeasurementCallback): MeasurementResult = {
    measurementCallBack = intermediateMeasurementCallBack
    if (testRunning) {
      throw new Exception("Test is already running")
    }

    val uri: URI = new URI("wss://" + server.fqdn + "/ndt/v7/download")
    val client: OkHttpClient = httpClient()
    val request: Request = buildRequest(uri)

    client.newWebSocket(request, DownloadSocketListener(intermediateCallBack, finalDownloadCallBack, () => {
      firstRequestTime = System.nanoTime()
    }))

    val executorService = client.dispatcher().executorService()
    executorService.shutdown()
    executorService.awaitTermination(Int.MaxValue, TimeUnit.MINUTES)

    testRunning = false
    lastMeasurementResult
  }

  protected def generateMeasurementResult(count: Double, lastMeasurement: Measurement): MeasurementResult = {
    val bandwidth = count / ((lastRequestTime - firstRequestTime).doubleValue() / 1.second.toNanos)
    val info = if (lastMeasurement != null && lastMeasurement.ConnectionInfo.isDefined)
      ConnectionInfo(lastMeasurement.ConnectionInfo.get.Client, lastMeasurement.ConnectionInfo.get.Server)
    else
      ConnectionInfo("not_set", "not_set")
    val result = MeasurementResult(Bandwidth(bandwidth), info, count.toLong)
    result
  }

  protected def intermediateCallBack(count: Double, lastMeasurement: Measurement): MeasurementResult = {
    lastRequestTime = System.nanoTime()
    val result = generateMeasurementResult(count, lastMeasurement)
    measurementCallBack(result)
    result
  }

  protected def finalDownloadCallBack(count: Double, lastMeasurement: Measurement): MeasurementResult = {
    lastRequestTime = System.nanoTime()
    val result = generateMeasurementResult(count, lastMeasurement)
    lastMeasurementResult = result
    result
  }

  protected def finalUploadCallBack(count: Double, lastMeasurement: Measurement): MeasurementResult = {
    lastRequestTime = System.nanoTime()
    val result = generateMeasurementResult(count, lastMeasurement)
    lastMeasurementResult = result
    result
  }

  private def defaultMeasurementCallback(measurement: MeasurementResult): Unit = {
    logger.info("intermediate measurement: %s".format(measurement))
  }

}

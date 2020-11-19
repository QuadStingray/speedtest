package com.quadstingray.speedtest.ndt7

import java.net.URI

import com.quadstingray.speedtest.ndt7.exception.ConcurrentTestException
import com.quadstingray.speedtest.ndt7.lib.MeasurementResult._
import com.quadstingray.speedtest.ndt7.lib.api.{BbrInfo, Measurement}
import com.quadstingray.speedtest.ndt7.lib.{Bandwidth, ConnectionInfo, MeasurementResult, Server}
import com.quadstingray.speedtest.ndt7.listener.{DownloadSocketListener, UploadSocketListener}
import okhttp3._
import okio.ByteString

import scala.concurrent.duration._
import scala.util.Random

case class TestClient(server: Server) extends HttpClient {
  private var testRunning: Boolean                             = false
  private var firstRequestTime: Long                           = 0
  private var lastRequestTime: Long                            = 0
  private var lastMeasurementResult: MeasurementResult         = _
  protected var measurementCallBack: MeasurementResult => Unit = SpeedTest.defaultMeasurementCallback

  def runUpload(intermediateMeasurementCallBack: MeasurementResult => Unit = SpeedTest.defaultMeasurementCallback): MeasurementResult = {
    measurementCallBack = intermediateMeasurementCallBack
    if (testRunning) {
      throw ConcurrentTestException()
    }
    else {
      testRunning = true
    }

    val uri: URI             = new URI(server.urls("wss:///ndt/v7/upload"))
    val client: OkHttpClient = httpClient()
    val request: Request     = buildRequest(uri)

    var lastSocketMessage: Measurement = Measurement()

    def updateMeasurement(ms: Measurement): Unit = lastSocketMessage = ms

    val socket = client.newWebSocket(request, UploadSocketListener(updateMeasurement))

    var count: Long         = 0
    var sendByteCount: Long = 0

    val minMessageSize = 8
    val maxMessageSize = 8192

    firstRequestTime = System.nanoTime()
    lastRequestTime = System.nanoTime()

    while ((System.nanoTime() - firstRequestTime).nanos.toSeconds < 8) {
      count = count + 1
      val byteCount: Int = 1 << 13
      val message        = Random.nextBytes(byteCount)
      //Ws max queuesize is 16mb
      if (socket.queueSize() < message.length) {
        sendByteCount += message.length.toLong
        socket.send(ByteString.of(message, 0, message.length))
      }

      lastRequestTime = System.nanoTime()
      lastMeasurementResult = intermediateCallBack(TestKindUpload, sendByteCount, lastSocketMessage)
    }

    socket.close(1000, null)

    val executorService = client.dispatcher().executorService()
    executorService.shutdown()

    testRunning = false

    if (lastMeasurementResult == null)
      MeasurementResult(TestKindUpload)
    else
      lastMeasurementResult
  }

  def runDownload(intermediateMeasurementCallBack: MeasurementResult => Unit = SpeedTest.defaultMeasurementCallback): MeasurementResult = {
    measurementCallBack = intermediateMeasurementCallBack
    if (testRunning) {
      throw ConcurrentTestException()
    }
    else {
      testRunning = true
    }

    val uri: URI             = new URI(server.urls("wss:///ndt/v7/download"))
    val client: OkHttpClient = httpClient()
    val request: Request     = buildRequest(uri)

    val ws = client.newWebSocket(request, DownloadSocketListener(intermediateCallBack, finalDownloadCallBack, () => {
      firstRequestTime = System.nanoTime()
    }))

    val executorService = client.dispatcher().executorService()
    ws.send("test")

    firstRequestTime = System.nanoTime()
    lastRequestTime = System.nanoTime()

    while (testRunning && (System.nanoTime() - firstRequestTime).nanos.toSeconds < 30) {
      ""
    }

    executorService.shutdown()
    testRunning = false

    if (lastMeasurementResult == null)
      MeasurementResult(TestKindDownload)
    else
      lastMeasurementResult
  }

  protected def generateMeasurementResult(testKind: String, count: Double, lastMeasurement: Measurement): MeasurementResult = {
    val bandwidth = count / ((lastRequestTime - firstRequestTime).doubleValue() / 1.second.toNanos)
    val info =
      if (lastMeasurement != null && lastMeasurement.ConnectionInfo.isDefined)
        ConnectionInfo(lastMeasurement.ConnectionInfo.get.Client, lastMeasurement.ConnectionInfo.get.Server)
      else
        ConnectionInfo("not_set", "not_set")
    val latency = lastMeasurement.BBRInfo.getOrElse(BbrInfo()).MinRTT
    val result  = MeasurementResult(testKind, Bandwidth(bandwidth), info, count.toLong, latency)
    result
  }

  protected def intermediateCallBack(testKind: String, count: Double, lastMeasurement: Measurement): MeasurementResult = {
    lastRequestTime = System.nanoTime()
    val result = generateMeasurementResult(testKind, count, lastMeasurement)
    lastMeasurementResult = result
    measurementCallBack(result)
    result
  }

  protected def finalDownloadCallBack(count: Double, lastMeasurement: Measurement): MeasurementResult = {
    lastRequestTime = System.nanoTime()
    val result = generateMeasurementResult(TestKindDownload, count, lastMeasurement)
    lastMeasurementResult = result
    testRunning = false
    result
  }

}

package com.quadstingray.speedtest.ndt7

import java.net.URI

import com.quadstingray.speedtest.ndt7.exception.ConcurrentTestException
import com.quadstingray.speedtest.ndt7.lib.MeasurementResult._
import com.quadstingray.speedtest.ndt7.lib.api.{ BbrInfo, Measurement }
import com.quadstingray.speedtest.ndt7.lib.{ Bandwidth, ConnectionInfo, MeasurementResult, Server }
import com.quadstingray.speedtest.ndt7.listener.{ DownloadSocketListener, UploadSocketListener }
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

    val uri: URI             = new URI("wss://" + server.fqdn + "/ndt/v7/upload")
    val client: OkHttpClient = httpClient()
    val request: Request     = buildRequest(uri)

    var lastSocketMessage: Measurement = Measurement()

    def updateMeasurement(ms: Measurement): Unit = lastSocketMessage = ms

    val socket = client.newWebSocket(request, UploadSocketListener(updateMeasurement))

    var count: Long = 0

    val minMessageSize = 1 << 13
    val maxMessageSize = 1 << 24

    firstRequestTime = System.nanoTime()
    lastRequestTime = System.nanoTime()
    while ((System.nanoTime() - firstRequestTime).nanos.toSeconds < 8) {
      val byteCount = if (count > minMessageSize * 5) maxMessageSize else minMessageSize
      val message   = ByteString.of(Random.nextBytes(byteCount), 0, byteCount)
      count = count + byteCount
      socket.send(message)
      var break = false
      var lastQueueSize = 0L
      var sameQueueSize = 0
      while (socket.queueSize() > 0 && !break) {
        if ((System.nanoTime() - firstRequestTime).nanos.toSeconds > 8 && socket.queueSize() == lastQueueSize) {
          sameQueueSize = sameQueueSize + 1
          if (sameQueueSize > 64) {
            Thread.sleep(10)
            break = true
            count = count - socket.queueSize()
          }
        } else {
          sameQueueSize = 0
        }
        lastQueueSize = socket.queueSize()
      }
      lastRequestTime = System.nanoTime()
      lastMeasurementResult = intermediateCallBack(TestKindUpload, count, lastSocketMessage)
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

    val uri: URI             = new URI("wss://" + server.fqdn + "/ndt/v7/download")
    val client: OkHttpClient = httpClient()
    val request: Request     = buildRequest(uri)

    client.newWebSocket(request, DownloadSocketListener(intermediateCallBack, finalDownloadCallBack, () => {
      firstRequestTime = System.nanoTime()
    }))

    val executorService = client.dispatcher().executorService()
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

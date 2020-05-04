package com.quadstingray.speedtest.ndt7.listener

import com.github.plokhotnyuk.jsoniter_scala.core.readFromString
import com.quadstingray.speedtest.ndt7.lib.api.Measurement
import com.typesafe.scalalogging.LazyLogging
import okhttp3.{Response, WebSocket, WebSocketListener}
import okio.ByteString
import com.github.plokhotnyuk.jsoniter_scala.macros._
import com.github.plokhotnyuk.jsoniter_scala.core._

private [ndt7] case class DownloadSocketListener(messageCallBack: (Double, Measurement) => Unit, closeCallBack: (Double, Measurement) => Unit, openCallBack: () => Unit) extends WebSocketListener with LazyLogging {
  private var count = 0.0
  private var lastMeasurement: Measurement = _
  private var connected: Boolean = false

  implicit val codec: JsonValueCodec[Measurement] = JsonCodecMaker.make[Measurement]

  def isConnected: Boolean = connected

  override def onOpen(ws: WebSocket, resp: Response) {
    connected = true
    openCallBack()
    logger.debug("Download WebSocket is opened")
  }

  override def onMessage(ws: WebSocket, text: String) {
    logger.debug("text message received")
    count += text.length.doubleValue
    try {
      val measurement: Measurement = readFromString[Measurement](text)
      lastMeasurement = measurement
      messageCallBack(count, measurement)
    } catch {
      case e: Exception => {
        e
      }
    }
  }

  override def onMessage(ws: WebSocket, bytes: ByteString) {
    logger.debug("ByteString received")
    count += bytes.size().doubleValue()
    messageCallBack(count, lastMeasurement)
  }

  override def onClosing(ws: WebSocket, code: Int, reason: String) {
    closeCallBack(count, lastMeasurement)
    connected = false
    ws.close(1000, null)
  }

  def resetStats() : Unit = {
    connected = false
    count = 0
    lastMeasurement = null
  }

}

package com.quadstingray.speedtest.ndt7.listener

import com.quadstingray.speedtest.ndt7.lib.api.Measurement
import com.typesafe.scalalogging.LazyLogging
import okhttp3.{Response, WebSocket, WebSocketListener}
import okio.ByteString
import com.github.plokhotnyuk.jsoniter_scala.macros._
import com.github.plokhotnyuk.jsoniter_scala.core._
import com.quadstingray.speedtest.ndt7.lib.Server

private [ndt7] case class UploadSocketListener(messageCallBack: (Measurement) => Unit) extends WebSocketListener with LazyLogging {
  private var lastMeasurement: Measurement = _
  private var connected: Boolean = false

  implicit val codec: JsonValueCodec[Measurement] = JsonCodecMaker.make[Measurement]

  def isConnected: Boolean = connected

  override def onOpen(ws: WebSocket, resp: Response) {
    connected = true
    logger.debug("Download WebSocket is opened")
  }

  override def onMessage(ws: WebSocket, text: String) {
    logger.debug("text message received")
    try {
      val measurement: Measurement = readFromString[Measurement](text)
      lastMeasurement = measurement
      messageCallBack(measurement)
    } catch {
      case e: Exception => {
        e
      }
    }
  }

  override def onMessage(ws: WebSocket, bytes: ByteString) {
    logger.debug("ByteString received")
  }

  override def onClosing(ws: WebSocket, code: Int, reason: String) {
    ws.close(1000, null)
  }

  def resetStats() : Unit = {
    lastMeasurement = null
  }

}

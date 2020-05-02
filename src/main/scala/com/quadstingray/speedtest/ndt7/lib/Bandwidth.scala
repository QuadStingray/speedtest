package com.quadstingray.speedtest.ndt7.lib

case class Bandwidth(bytePerSeconds: Double) {

  def bitPerSecond: Double = {
    bytePerSeconds.toDouble * 8
  }

  def kiloBytePerSecond: Double = {
    bytePerSeconds.toDouble / 1000
  }

  def kiloBitPerSecond: Double = {
    bytePerSeconds.toDouble / 125000
  }

  def megaBytePerSecond: Double = {
    bytePerSeconds.toDouble / 1000000
  }

  def megaBitPerSecond: Double = {
    bytePerSeconds.toDouble / 125000
  }

  def gigaBytePerSecond: Double = {
    bytePerSeconds.toDouble / 1000000000
  }

  def gigaBitPerSecond: Double = {
    bytePerSeconds.toDouble / 125000000
  }

}

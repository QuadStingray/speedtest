package com.quadstingray.speedtest.ndt7.lib.api

case class Measurement(
                        AppInfo: Option[AppInfo],
                        Origin: Option[String],
                        Test: Option[String],
                        ConnectionInfo: Option[ConnectionInfo],
                        TcpInfo: Option[TcpInfo],
                        BBRInfo: Option[BBRInfo]
                      )

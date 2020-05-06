package com.quadstingray.speedtest.ndt7.lib

case class SpeedTestResult(download: Bandwidth,
                           upload: Bandwidth,
                           latency: Long,
                           connectionInfo: ConnectionInfo,
                           server: Server,
                           testResults: List[MeasurementResult] = List())

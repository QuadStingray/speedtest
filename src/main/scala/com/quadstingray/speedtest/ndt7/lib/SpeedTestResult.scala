package com.quadstingray.speedtest.ndt7.lib

case class SpeedTestResult(download: Bandwidth,
                           upload: Bandwidth,
                           latency: Long, //in nanos
                           connectionInfo: ConnectionInfo,
                           server: Server,
                           testResults: List[MeasurementResult] = List())

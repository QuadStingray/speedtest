package com.quadstingray.speedtest.ndt7.lib

case class ServerDetails(site: String,
                         city: String,
                         country: String,
                         metro: List[String],
                         latitude: Double,
                         longitude: Double,
                         uplink_speed: String,
                         roundrobin: Boolean)

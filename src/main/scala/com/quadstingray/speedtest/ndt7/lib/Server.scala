package com.quadstingray.speedtest.ndt7.lib

case class ServerResult(results: List[Server])

case class Server(machine: String, location: LocationInfo, urls: Map[String, String])

case class LocationInfo(city: String, country: String)
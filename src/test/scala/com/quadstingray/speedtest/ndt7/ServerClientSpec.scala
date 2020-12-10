package com.quadstingray.speedtest.ndt7

import com.quadstingray.speedtest.ndt7.lib.{ Server, ServerDetails }
import org.specs2.mutable.Specification

class ServerClientSpec extends Specification {
  sequential

  "ServerClient" >> {

    "locateNext Server" >> {
      val client     = ServerClient()
      val testServer = client.nextServer
      testServer.machine.contains("measurement-lab.org") must beTrue
      testServer.location.country.length must beLessThanOrEqualTo(5)
    }

    "find server by site fra05" >> {
      val client       = ServerClient()
      val testServer   = client.serverDetailsBySite("fra05").get
      val serverToTest = ServerDetails("fra05", "Frankfurt", "DE", List("fra05", "fra"), 50.0379, 8.5622, "10g", roundrobin = true)
      testServer.site must beEqualTo(serverToTest.site)
      testServer.city must beEqualTo(serverToTest.city)
      testServer.country must beEqualTo(serverToTest.country)
      testServer.latitude must beEqualTo(serverToTest.latitude)
      testServer.longitude must beEqualTo(serverToTest.longitude)
      testServer.uplink_speed must beEqualTo(serverToTest.uplink_speed)
    }

    "locateNext Server and get server details" >> {
      val client     = ServerClient()
      val testServer = client.nextServer
      testServer.machine.contains("measurement-lab.org") must beTrue
      testServer.location.country.length must beLessThanOrEqualTo(5)
      val testServerDetails   = client.serverDetailsByServer(testServer).get

      testServer.machine.contains(testServerDetails.site) must beTrue
      testServer.location.city must beEqualTo(testServerDetails.city)
      testServer.location.country must beEqualTo(testServerDetails.country)
    }
  }

}

package com.quadstingray.speedtest.ndt7

import com.quadstingray.speedtest.ndt7.lib.{ Server, ServerDetails }
import org.specs2.mutable.Specification

class ServerClientSpec extends Specification {
  sequential

  "ServerClient" >> {

    "locateNext Server" >> {
      val client     = ServerClient()
      val testServer = client.nextServer
      testServer.fqdn.contains("ndt") must beTrue
      testServer.ip.size must beGreaterThanOrEqualTo(1)
      testServer.country.length must beLessThanOrEqualTo(5)
    }

    "find all servers'" >> {
      val client         = ServerClient()
      val testServerList = client.allServer
      testServerList.size must beGreaterThanOrEqualTo(100)
      val testServer = testServerList.head
      testServer.fqdn.contains("ndt") must beTrue
      testServer.ip.size must beGreaterThanOrEqualTo(1)
      testServer.country.length must beLessThanOrEqualTo(5)
    }

    "find server by site fra05" >> {
      val client       = ServerClient()
      val testServer   = client.serverBySite("fra05").get
      val serverToTest = Server("fra05", "ndt-iupui-mlab1-fra05.measurement-lab.org", "Frankfurt", "DE", List("193.142.125.24", "2a01:3e0:ff20:401::24"))
      testServer.site must beEqualTo(serverToTest.site)
      testServer.fqdn.matches("ndt.iupui.(.*?).fra05.measurement-lab.org") must beTrue
      testServer.city must beEqualTo(serverToTest.city)
      testServer.country must beEqualTo(serverToTest.country)
    }

    "find no server by site 'unknownServer'" >> {
      val client     = ServerClient()
      val testServer = client.serverBySite("unknownServer")
      testServer must beNone
    }

    "find all servers'" >> {
      val client         = ServerClient()
      val testServerList = client.allServerDetails
      testServerList.size must beGreaterThanOrEqualTo(100)
      val testServer = testServerList.head
      testServer.country.length must beLessThanOrEqualTo(5)
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

  }

}

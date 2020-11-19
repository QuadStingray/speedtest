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

  }

}

package com.quadstingray.speedtest.ndt7.exception

final case class ConcurrentTestException(private val message: String = "Concurrent test is running.", private val cause: Throwable = None.orNull) extends Exception(message, cause)

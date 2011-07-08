package net.foobarfreaks.akka.chat

import org.slf4j.LoggerFactory

trait Logging {

	val log = LoggerFactory.getLogger(getClass)

}
package com.crealytics.common

import scala.reflect.ClassTag
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object Retry {

  /**
    * Execute a given block X amount of time, with a quadratick backoff mechanism.
    * @param retries Number of time the block should be retried in case of Non-Fatal failure.
    *                Default value: 4
    * @param backoff Time in milliseconds to wait before trying again the block.
    *                Default value: 10000ms
    * @param block Block of code to be executed until retries are expired.
    * @tparam T Type returned by the block.
    * @return
    */
  @annotation.tailrec
  def retry[T, L <: Exception: ClassTag](retries: Int = 4, backoff: Long = 10000)(block: => T): T = {
    Try(block) match {
      case Success(e) => e
      case Failure(e: L) if retries > 0 && NonFatal(e) => {
        Thread.sleep(backoff)
        retry(retries - 1, backoff * 10)(block)
      }
      case Failure(e) => throw e
    }
  }

}

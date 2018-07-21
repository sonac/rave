package com.crealytics.common

import scala.collection.GenTraversable
import shapeless._, ops.traversable.FromTraversable

object ServiceHelpers {

  def checkAndConvert(str: String): Either[String, Int] = {
    val dotIdx: Int = str.indexOf('.')
    val clrStr = if (dotIdx != 0) str.replace(".", "") else str
    if (clrStr.forall(_ isDigit)) Right(str.toDouble.toInt)
    else Left(str)
  }

  def rowMapToList(row: Map[String, String], cols: Iterable[String]): List[Any] = {
    val opts: Iterable[Option[String]] = cols.map(row.get(_))
    opts.map{x => x match {
      case Some(s) => checkAndConvert(s).merge
      case None => None
    }}.toList
  }

  class GenListToClass[T] {
    def apply[R <: HList](l: GenTraversable[_])
                         (implicit gen: Generic.Aux[T, R], tl: FromTraversable[R]): Option[T] =
      tl(l).map(gen.from)
  }

  def listToCaseClass[T] = new GenListToClass[T]

}

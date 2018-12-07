package sonac.common

import scala.collection.GenTraversable
import shapeless._, ops.traversable.FromTraversable

object ServiceHelpers {

  def checkAndConvert(str: String): Any = {
    if (str.endsWith(".0") || (str forall Character.isDigit)) str.toDouble.toInt
    else if (str.replace(".", "") forall Character.isDigit) str.toDouble
    else str
  }

  def rowMapToList(row: Map[String, String], cols: Iterable[String]): List[Any] = {
    val opts: Seq[Option[String]] = cols.toSeq.map(row.get(_))
    val t = opts.map{x => x match {
      case Some(s) => checkAndConvert(s)
      case None => None
    }}.toList

    t
  }

  class FromListToCaseClass[T] {
    def apply[R <: HList](l: GenTraversable[_])
                         (implicit gen: Generic.Aux[T, R], tl: FromTraversable[R]): Option[T] = {
      tl(l).map(gen.from)
    }

  }
  def listToCaseClass[T] = new FromListToCaseClass[T]


}

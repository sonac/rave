package sonac

import java.util.Properties
import scala.language.implicitConversions

object PropertiesImplicits {

  /** Converts a [[Map[String, String]]] into a [[Properties]] instance. */
  implicit def mapToProperties(map: Map[String, String]): Properties =
    map.foldLeft(new Properties) {
      case (properties, (key, value)) =>
        properties.put(key, value)
        properties
    }
}
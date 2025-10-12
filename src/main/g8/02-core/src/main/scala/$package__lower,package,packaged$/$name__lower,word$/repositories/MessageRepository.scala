package $package;format="lower,package"$
package $name;format="lower,word"$
package repositories

import $package;format="lower,package"$.$name;format="lower,word"$.Message

/** Algebra describing how to retrieve application messages in effect `F`. */
trait MessageRepository[F[_]] {
  /** Loads the greeting message from the underlying data source. */
  def getMessage: F[Message]
}

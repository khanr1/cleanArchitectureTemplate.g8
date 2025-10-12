package $package;format="lower,package"$
package $name;format="lower,word"$
package services

import $package;format="lower,package"$.$name;format="lower,word"$.Message
import $package;format="lower,package"$.$name;format="lower,word"$.repositories.*

/** Public interface exposing high-level message operations. */
trait MessageServices[F[_]] {
  /** Retrieves the message that should be shared with API consumers. */
  def sayHello: F[Message]
}

object MessageServices:
  /** Creates a [[MessageServices]] backed by the provided repository. */
  def make[F[_]](repo: MessageRepository[F]): MessageServices[F] =
    new MessageServices[F] {

      override def sayHello: F[Message] = repo.getMessage

    }

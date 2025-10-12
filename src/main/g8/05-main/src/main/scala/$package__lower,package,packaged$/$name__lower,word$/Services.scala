package $package;format="lower,package"$
package $name;format="lower,word"$

import cats.Monad
import $package;format="lower,package"$.$name;format="lower,word"$.services.MessageServices
import $package;format="lower,package"$.$name;format="lower,word"$.repositories.MessageRepository

/** Aggregates all high-level application services. */
sealed trait Services[F[_]] private (
    val messages: MessageServices[F]
)

object Services {
  /** Instantiates [[Services]] wired with the provided repository dependencies. */
  def make[F[_]: Monad](
      messagesR: MessageRepository[F]
  ): Services[F] = new Services[F](
    messages = MessageServices.make[F](messagesR)
  ) {}
}

package $package;format="lower,package"$
package $name;format="lower,word"$

import cats.effect.kernel.Async
import cats.syntax.all.*
import $package;format="lower,package"$.$name;format="lower,word"$.controllers.MessageController
import $package;format="lower,package"$.$name;format="lower,word"$.repositories.MessageInMemoryRepo

/** Assembles the application graph and starts the HTTP server. */
object Program {
  /** Bootstraps all modules and runs the server until the effect is canceled. */
  def make[F[_]: Async: org.typelevel.log4cats.Logger]: F[Unit] =
    AppResource
      .makeInMemory[F]()
      .flatMap(res =>
        val services =
          Services.make(MessageInMemoryRepo.make(res.messageRef))
        val controllers = (
          MessageController.make(services.messages)
        )
        val api = HttpApi.make(controllers)
        HttpServer.make(api).serve
      )
      .useForever
      .void

}

package $package;format="lower,package"$
package $name;format="lower,word"$

import org.http4s.HttpRoutes

/** Base contract for delivery-layer controllers exposing HTTP routes. */
trait Controller[F[_]] {
  val routes: HttpRoutes[F]
}

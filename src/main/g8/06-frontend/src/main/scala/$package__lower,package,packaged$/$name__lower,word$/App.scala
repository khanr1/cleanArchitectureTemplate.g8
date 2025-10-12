package $package;format="lower,package"$
package $name;format="lower,word"$

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import $package;format="lower,package"$.$name;format="lower,word"$.api.MessageAPI

@main
def main(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    div(
      "If you see text below the code is working",
      div(
        child.text <-- MessageAPI.getMessage
          .toSignal("loading")
      )
    )
  )

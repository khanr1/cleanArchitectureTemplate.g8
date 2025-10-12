package $package;format="lower,package"$.$name;format="lower,word"$.api

import com.raquo.airstream.web.FetchStream

object MessageAPI {
  private val endpoint: String = "/api/hello"

  def getMessage =
    FetchStream
      .get(endpoint)

}

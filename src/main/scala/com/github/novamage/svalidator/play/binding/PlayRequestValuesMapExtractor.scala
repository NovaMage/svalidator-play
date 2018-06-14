package com.github.novamage.svalidator.play.binding

import play.api.mvc.Request

trait PlayRequestValuesMapExtractor {

  def extractValuesMapFromRequest(request: Request[_]): Map[String, Seq[String]]

}

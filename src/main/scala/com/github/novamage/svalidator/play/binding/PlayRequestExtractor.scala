package com.github.novamage.svalidator.play.binding

import play.api.mvc.Request

trait PlayRequestExtractor {

  def extractValuesMapFromRequest(request: Request[_]): Map[String, Seq[String]]

  def extractBindingMetadataFromRequest(request: Request[_]): Map[String, Any]
}

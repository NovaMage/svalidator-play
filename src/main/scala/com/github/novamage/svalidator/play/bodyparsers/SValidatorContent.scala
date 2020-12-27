package com.github.novamage.svalidator.play.bodyparsers

import io.circe.Json
import play.api.mvc.AnyContent

sealed trait SValidatorContent

sealed case class SValidatorContentAsJson(json: Json) extends SValidatorContent

sealed case class SValidatorContentAsAnyContent(anyContent: AnyContent) extends SValidatorContent

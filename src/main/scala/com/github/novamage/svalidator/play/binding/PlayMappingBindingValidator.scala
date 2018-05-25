package com.github.novamage.svalidator.play.binding

import com.github.novamage.svalidator.validation.binding.{BindingAndValidationSummary, MappingBindingValidator}
import play.api.mvc.{MessagesRequest, Request}

import scala.reflect.runtime.{universe => ru}

abstract class PlayMappingBindingValidator[A: ru.TypeTag] extends MappingBindingValidator[A] {

  def bindFromRequest[B](mapOp: B => A)(implicit request: Request[_]): BindingAndValidationSummary[A] = {
    val valuesMap = PlayRequestValuesMapExtractor.extractValuesMapFromRequest(request)
    super.bindAndValidate(valuesMap, mapOp)
  }

  def bindLocalized[B](mapOp: B => A)(implicit messagesRequest: MessagesRequest[_]): BindingAndValidationSummary[A] = {
    val valuesMap = PlayRequestValuesMapExtractor.extractValuesMapFromRequest(messagesRequest)
    super.bindAndValidate(valuesMap, mapOp).localize((key: String) => messagesRequest.messages(key))
  }

}

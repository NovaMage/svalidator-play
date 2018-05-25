package com.github.novamage.svalidator.play.binding

import com.github.novamage.svalidator.validation.binding.{BindingAndValidationSummary, BindingValidator}
import play.api.mvc.{MessagesRequest, Request}

import scala.reflect.runtime.{universe => ru}

abstract class PlayBindingValidator[A: ru.TypeTag] extends BindingValidator[A] {

  def bindFromRequest(implicit request: Request[_]): BindingAndValidationSummary[A] = {
    val valuesMap = PlayRequestValuesMapExtractor.extractValuesMapFromRequest(request)
    super.bindAndValidate(valuesMap)
  }


  def bindLocalized(implicit messagesRequest: MessagesRequest[_]): BindingAndValidationSummary[A] = {
    val valuesMap = PlayRequestValuesMapExtractor.extractValuesMapFromRequest(messagesRequest)
    super.bindAndValidate(valuesMap).localize((key: String) => messagesRequest.messages(key))
  }

}

package com.github.novamage.svalidator.play.binding

import com.github.novamage.svalidator.validation.binding.{BindingAndValidationSummary, BindingValidator}
import play.api.mvc.{MessagesRequest, Request}

import scala.reflect.runtime.{universe => ru}

abstract class PlayBindingValidator[A: ru.TypeTag] extends BindingValidator[A] {

  def bindFromRequest(implicit request: Request[_]): BindingAndValidationSummary[A] = {
    extractFromRequest(request, DefaultPlayRequestValuesMapExtractor)
  }


  def bindLocalized(implicit messagesRequest: MessagesRequest[_]): BindingAndValidationSummary[A] = {
    extractLocalized(messagesRequest, DefaultPlayRequestValuesMapExtractor)
  }

  def extractFromRequest(implicit request: Request[_], extractor: PlayRequestValuesMapExtractor): BindingAndValidationSummary[A] = {
    val valuesMap = extractor.extractValuesMapFromRequest(request)
    super.bindAndValidate(valuesMap)
  }

  def extractLocalized(implicit messagesRequest: MessagesRequest[_], extractor: PlayRequestValuesMapExtractor): BindingAndValidationSummary[A] = {
    val valuesMap = extractor.extractValuesMapFromRequest(messagesRequest)
    super.bindAndValidate(valuesMap).localize((key: String) => messagesRequest.messages(key))
  }

}

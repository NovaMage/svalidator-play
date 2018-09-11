package com.github.novamage.svalidator.play.binding

import com.github.novamage.svalidator.validation.binding.{BindingAndValidationWithData, BindingValidatorWithData}
import play.api.mvc.{MessagesRequest, Request}

import scala.reflect.runtime.{universe => ru}

abstract class PlayBindingValidatorWithData[A: ru.TypeTag, B] extends BindingValidatorWithData[A, B] {

  def bindFromRequest(implicit request: Request[_]): BindingAndValidationWithData[A, B] = {
    extractFromRequest(request, DefaultPlayRequestValuesMapExtractor)
  }


  def bindLocalized(implicit messagesRequest: MessagesRequest[_]): BindingAndValidationWithData[A, B] = {
    extractLocalized(messagesRequest, DefaultPlayRequestValuesMapExtractor)
  }

  def extractFromRequest(implicit request: Request[_], extractor: PlayRequestValuesMapExtractor): BindingAndValidationWithData[A, B] = {
    val valuesMap = extractor.extractValuesMapFromRequest(request)
    super.bindAndValidate(valuesMap)
  }

  def extractLocalized(implicit messagesRequest: MessagesRequest[_], extractor: PlayRequestValuesMapExtractor): BindingAndValidationWithData[A, B] = {
    val valuesMap = extractor.extractValuesMapFromRequest(messagesRequest)
    super.bindAndValidate(valuesMap).localize((key: String) => messagesRequest.messages(key))
  }

}


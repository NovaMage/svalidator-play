package com.github.novamage.svalidator.play.binding

import com.github.novamage.svalidator.validation.binding.{BindingAndValidationWithData, BindingValidatorWithData}
import play.api.mvc.{MessagesRequest, Request}

import scala.reflect.runtime.{universe => ru}

abstract class PlayBindingValidatorWithData[A: ru.TypeTag, B] extends BindingValidatorWithData[A, B] {

  def bindFromRequest(implicit request: Request[_]): BindingAndValidationWithData[A, B] = {
    extractFromRequest(request, DefaultPlayRequestExtractor)
  }

  def bindLocalized(implicit messagesRequest: MessagesRequest[_]): BindingAndValidationWithData[A, B] = {
    extractLocalized(messagesRequest, DefaultPlayRequestExtractor)
  }

  def extractFromRequest(implicit request: Request[_], extractor: PlayRequestExtractor): BindingAndValidationWithData[A, B] = {
    val valuesMap = extractor.extractValuesMapFromRequest(request)
    val metadata = extractor.extractBindingMetadataFromRequest(request)
    super.bindAndValidate(valuesMap, metadata)
  }

  def extractLocalized(implicit messagesRequest: MessagesRequest[_], extractor: PlayRequestExtractor): BindingAndValidationWithData[A, B] = {
    val valuesMap = extractor.extractValuesMapFromRequest(messagesRequest)
    val metadata = extractor.extractBindingMetadataFromRequest(messagesRequest)
    super.bindAndValidate(valuesMap, metadata).localize((key: String) => messagesRequest.messages(key))
  }

}


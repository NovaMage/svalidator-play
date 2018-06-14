package com.github.novamage.svalidator.play.binding

import com.github.novamage.svalidator.validation.binding.{BindingAndValidationSummary, MappingBindingValidator}
import play.api.mvc.{MessagesRequest, Request}

import scala.reflect.runtime.{universe => ru}

abstract class PlayMappingBindingValidator[A: ru.TypeTag] extends MappingBindingValidator[A] {

  def bindFromRequest[B: ru.TypeTag](mapOp: B => A)(implicit request: Request[_]): BindingAndValidationSummary[A] = {
    extractFromRequest[B](mapOp)(implicitly, request, DefaultPlayRequestValuesMapExtractor)
  }

  def bindLocalized[B: ru.TypeTag](mapOp: B => A)(implicit messagesRequest: MessagesRequest[_]): BindingAndValidationSummary[A] = {
    extractLocalized[B](mapOp)(implicitly, messagesRequest, DefaultPlayRequestValuesMapExtractor)
  }

  def extractFromRequest[B: ru.TypeTag](mapOp: B => A)(implicit request: Request[_], extractor: PlayRequestValuesMapExtractor): BindingAndValidationSummary[A] = {
    val valuesMap = extractor.extractValuesMapFromRequest(request)
    super.bindAndValidate(valuesMap, mapOp)
  }

  def extractLocalized[B: ru.TypeTag](mapOp: B => A)(implicit messagesRequest: MessagesRequest[_], extractor: PlayRequestValuesMapExtractor): BindingAndValidationSummary[A] = {
    val valuesMap = extractor.extractValuesMapFromRequest(messagesRequest)
    super.bindAndValidate(valuesMap, mapOp).localize((key: String) => messagesRequest.messages(key))
  }

}

package com.github.novamage.svalidator.play.binding

import com.github.novamage.svalidator.play.bodyparsers.{SValidatorContent, SValidatorContentAsAnyContent, SValidatorContentAsJson}
import com.github.novamage.svalidator.validation.binding.{BindingAndValidationWithData, MappingBindingValidatorWithData}
import play.api.mvc.{MessagesRequest, Request}

import scala.reflect.runtime.{universe => ru}

abstract class PlayMappingBindingValidatorWithData[A: ru.TypeTag, B] extends MappingBindingValidatorWithData[A, B] {

  def bindFromRequest[C: ru.TypeTag](mapOp: C => A)(implicit request: Request[_]): BindingAndValidationWithData[A, B] = {
    extractFromRequest[C](mapOp)(implicitly, request, DefaultPlayRequestExtractor)
  }

  def bindLocalized[C: ru.TypeTag](mapOp: C => A)(implicit messagesRequest: MessagesRequest[_]): BindingAndValidationWithData[A, B] = {
    extractLocalized[C](mapOp)(implicitly, messagesRequest, DefaultPlayRequestExtractor)
  }

  def extractFromRequest[C: ru.TypeTag](mapOp: C => A)(implicit request: Request[_], extractor: PlayRequestExtractor): BindingAndValidationWithData[A, B] = {
    request.body match {
      case SValidatorContentAsJson(json) =>
        val metadata = extractor.extractBindingMetadataFromRequest(request)
        super.bindAndValidate(json, mapOp, metadata)
      case _ =>
        val valuesMap = extractor.extractValuesMapFromRequest(request)
        val metadata = extractor.extractBindingMetadataFromRequest(request)
        super.bindAndValidate(valuesMap, mapOp, metadata)
    }
  }

  def extractLocalized[C: ru.TypeTag](mapOp: C => A)(implicit messagesRequest: MessagesRequest[_], extractor: PlayRequestExtractor): BindingAndValidationWithData[A, B] = {
    messagesRequest.body match {
      case SValidatorContentAsJson(json) =>
        val metadata = extractor.extractBindingMetadataFromRequest(messagesRequest)
        super.bindAndValidate(json, mapOp, metadata).localize((key: String) => messagesRequest.messages(key))
      case _ =>
        val valuesMap = extractor.extractValuesMapFromRequest(messagesRequest)
        val metadata = extractor.extractBindingMetadataFromRequest(messagesRequest)
        super.bindAndValidate(valuesMap, mapOp, metadata).localize((key: String) => messagesRequest.messages(key))
    }
  }

}


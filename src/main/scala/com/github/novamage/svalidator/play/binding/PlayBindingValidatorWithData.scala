package com.github.novamage.svalidator.play.binding

import com.github.novamage.svalidator.validation.binding.BindingAndValidationWithData
import play.api.mvc.{MessagesRequest, Request}

import scala.reflect.runtime.{universe => ru}

abstract class PlayBindingValidatorWithData[A: ru.TypeTag, B] extends PlayMappingBindingValidatorWithData[A, B] {

  def bindFromRequest(implicit request: Request[_]): BindingAndValidationWithData[A, B] = {
    super.bindFromRequest((x: A) => x)
  }

  def bindLocalized(implicit messagesRequest: MessagesRequest[_]): BindingAndValidationWithData[A, B] = {
    super.bindLocalized((x: A) => x)
  }

  def extractFromRequest(implicit request: Request[_], extractor: PlayRequestExtractor): BindingAndValidationWithData[A, B] = {
    super.extractFromRequest((x: A) => x)
  }

  def extractLocalized(implicit messagesRequest: MessagesRequest[_], extractor: PlayRequestExtractor): BindingAndValidationWithData[A, B] = {
    super.extractLocalized((x: A) => x)
  }

}


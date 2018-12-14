package com.github.novamage.svalidator.play.binding

import com.github.novamage.svalidator.validation.simple.ValidatorWithoutData

import scala.reflect.runtime.{universe => ru}

abstract class PlayMappingBindingValidator[A: ru.TypeTag]
  extends PlayMappingBindingValidatorWithData[A, Nothing]
    with ValidatorWithoutData[A] {

}

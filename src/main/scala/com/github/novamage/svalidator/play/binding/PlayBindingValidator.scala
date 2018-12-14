package com.github.novamage.svalidator.play.binding

import com.github.novamage.svalidator.validation.simple.ValidatorWithoutData

import scala.reflect.runtime.{universe => ru}

abstract class PlayBindingValidator[A: ru.TypeTag]
  extends PlayBindingValidatorWithData[A, Nothing]
    with ValidatorWithoutData[A] {

}

package com.github.novamage.svalidator.play.binding

import scala.reflect.runtime.{universe => ru}

abstract class PlayMappingBindingValidator[A: ru.TypeTag] extends PlayMappingBindingValidatorWithData[A, Nothing] {

}

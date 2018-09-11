package com.github.novamage.svalidator.play.binding

import scala.reflect.runtime.{universe => ru}

abstract class PlayBindingValidator[A: ru.TypeTag] extends PlayBindingValidatorWithData[A, Nothing] {

}

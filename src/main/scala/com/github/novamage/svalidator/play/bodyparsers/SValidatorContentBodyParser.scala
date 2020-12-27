package com.github.novamage.svalidator.play.bodyparsers

import akka.util.ByteString
import play.api.libs.streams.Accumulator
import play.api.mvc._

import scala.concurrent.ExecutionContext

class SValidatorContentBodyParser(parsers: PlayBodyParsers)(implicit context: ExecutionContext) extends BodyParser[AnyContent] {

  override def apply(requestHeader: RequestHeader): Accumulator[ByteString, Either[Result, AnyContent]] = {
    if (requestHeader.contentType.contains("text/json") || requestHeader.contentType.contains("application/json")) {
      parsers.tolerantText.apply(requestHeader).map {
        case Left(result) => Left(result)
        case Right(value) => Right(AnyContentAsText(value))
      }
    } else {
      parsers.anyContent.apply(requestHeader)
    }
  }
}

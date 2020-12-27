package com.github.novamage.svalidator.play.bodyparsers

import akka.util.ByteString
import play.api.http.HttpEntity
import play.api.libs.streams.Accumulator
import play.api.mvc._
import play.mvc.Http.Status

import scala.concurrent.ExecutionContext

class SValidatorContentBodyParser(parsers: PlayBodyParsers)(implicit context: ExecutionContext) extends BodyParser[SValidatorContent] {

  override def apply(requestHeader: RequestHeader): Accumulator[ByteString, Either[Result, SValidatorContent]] = {
    if (requestHeader.contentType.contains("text/json") || requestHeader.contentType.contains("application/json")) {
      parsers.tolerantText.apply(requestHeader).map {
        case Left(result) => Left(result)
        case Right(value) => io.circe.parser.parse(value) match {
          case Left(parsingFailure) => Left(
            Result(
              header = ResponseHeader(Status.UNSUPPORTED_MEDIA_TYPE),
              body = HttpEntity.Strict(
                ByteString.fromString(parsingFailure.getMessage()), Some("text/plain")
              )
            )
          )
          case Right(json) => Right(SValidatorContentAsJson(json))
        }
      }
    } else {
      parsers.anyContent.apply(requestHeader).map {
        case Left(result) => Left(result)
        case Right(value) => Right(SValidatorContentAsAnyContent(value))
      }
    }
  }
}

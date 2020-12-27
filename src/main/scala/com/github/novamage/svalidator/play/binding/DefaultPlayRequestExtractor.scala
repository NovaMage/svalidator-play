package com.github.novamage.svalidator.play.binding

import play.api.mvc.{AnyContentAsFormUrlEncoded, AnyContentAsMultipartFormData, Request}

object DefaultPlayRequestExtractor extends PlayRequestExtractor {

  override def extractValuesMapFromRequest(request: Request[_]): Map[String, Seq[String]] = {
    val queryString = request.queryString
    val formUrlEncodedBody = request.body match {
      case AnyContentAsFormUrlEncoded(data) => data
      case AnyContentAsMultipartFormData(multipartFormData) => multipartFormData.dataParts
      case _ => Map.empty[String, Seq[String]]
    }
    val allKeys = queryString.keySet ++ formUrlEncodedBody.keySet
    allKeys.map { key =>
      key -> (formUrlEncodedBody.getOrElse(key, Nil) ++ queryString.getOrElse(key, Nil))
    }.toMap
  }

  override def extractBindingMetadataFromRequest(request: Request[_]): Map[String, Any] = Map.empty
}

package com.github.novamage.svalidator.play.binding

import play.api.mvc.{AnyContentAsFormUrlEncoded, Request}

object PlayRequestValuesMapExtractor {

  def extractValuesMapFromRequest(request: Request[_]): Map[String, Seq[String]] = {
    val queryString = request.queryString
    val formUrlEncodedBody = request.body match {
      case AnyContentAsFormUrlEncoded(data) => data
      case _ => Map.empty[String, Seq[String]]
    }
    val allKeys = queryString.keySet ++ formUrlEncodedBody.keySet
    allKeys.map { key =>
      key -> (formUrlEncodedBody.getOrElse(key, Nil) ++ queryString.getOrElse(key, Nil))
    }.toMap
  }

}

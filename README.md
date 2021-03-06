svalidator-play
==============

An integration library for the [SValidator](https://github.com/NovaMage/SValidator) validation library with the 
[Play! framework](https://www.playframework.com/)

You may see this library in action in the [svalidator-play-sample application](https://github.com/NovaMage/svalidator-play-sample)

Installation
===========

svalidator-play is available on Maven for scala 2.12.  Just add the following line to your build.sbt:

```
libraryDependencies += "com.github.novamage" % "svalidator-play_2.12" % "2.3.0"
```

svalidator-play transitively depends on svalidator, so it's not necessary to add both to your project.

Prerequisites
============

It is necessary to read the documentation of [SValidator](https://github.com/NovaMage/SValidator/wiki) before 
reading this documentation.  Also, it is assumed that you are familiarized with how the Play! framework operates.

Usage
=====

svalidator-play provides the classes `PlayBindingValidator` and `PlayMappingBindingValidator` to adapt the regular 
`BindingValidator` and `MappingBindingValidator` functionalities with Play! framework's `Request`.  Additionally, you may 
use `PlayBindingValidatorWithData` and `PlayMappingBindingValidatorWithData` if you'd like to store some type of data 
along with the validation result.  The `PlayBindingValidator` provides the following four methods:

```scala
  def bindFromRequest(implicit request: Request[_]): BindingAndValidationWithData[A, Nothing]

  def bindLocalized(implicit messagesRequest: MessagesRequest[_]): BindingAndValidationWithData[A, Nothing]

  def extractFromRequest(implicit request: Request[_], 
                                   extractor: PlayRequestValuesMapExtractor): BindingAndValidationWithData[A, Nothing]

  def extractLocalized(implicit messagesRequest: MessagesRequest[_], 
                                 extractor: PlayRequestValuesMapExtractor): BindingAndValidationWithData[A, Nothing]
```

The `bindFromRequest` method will take the received request and map the values in the `body` and the `queryString` into a 
values map of type `Map[String, Seq[String]]` to pass into the regular `bindAndValidate` method of BindingValidator.

The `bindLocalized` will do the same as above, and in addition, will call the `localize` method in the resulting
`BindingAndValidationSummary`, passing the `messagesRequest.messages.apply` method as the function that performs the 
localization for you (this is Play's default way of localization)

By default, the operation of the extracting the values from the request into the values map is performed by the
`DefaultPlayRequestExtractor`, which you can see here:

```scala
package com.github.novamage.svalidator.play.binding

import play.api.mvc.{AnyContentAsFormUrlEncoded, Request}

object DefaultPlayRequestExtractor extends PlayRequestExtractor {

  override def extractValuesMapFromRequest(request: Request[_]): Map[String, Seq[String]] = {
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
  
    override def extractBindingMetadataFromRequest(request: Request[_]): Map[String, Any] = Map.empty

}
```

In short, it takes all keys from both an url encoded body and the query string, and puts all of the values in the 
resulting values map, concatenating the lists of values for matching keys if necessary.

However, if you'd like to customize the way that the values are extracted from the request, or append metadata to be passed to the binders later on, you may call instead the methods `extractFromRequest` and `extractLocalized`.  These methods will implicitly receive a `PlayRequestExtractor`, which you may provide to fully customize the way values and metadata are extracted from the request.

Finally, the `PlayMappingBindingValidator` works in a similar to the `PlayBindingValidator`, with the only
difference being that, just like the regular `MappingBindingValidator`, it allows you to pass a `TypeTag[B]` and a
 `mapOperation: B => A` to bind the request into a specific type of object and then apply the conversion function
 to transform the bound object into something else for validation.  It has the same four methods as `PlayBindingValidator`, so you may choose to perform regular binding, localized binding, or either of them with a custom extractor.

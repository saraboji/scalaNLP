package com.emirates.hackathon.vo

class InputDataVO(val message: String, val tokens: Option[List[String]], val namedEntities: Option[List[String]],
    val posTags: Option[List[String]], val normalizedEntities: Option[List[String]]) {
  
  var isFlightStatus: Boolean = false;
  var isFlightSearch: Boolean = false;
  var isFlightSchedule: Boolean = false;
}
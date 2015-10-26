package com.emirates.hackathon.vo

class InputDataVO(message: String, parsedMessage: Array[String], parsedNERs: Array[String]) {
  
  var isFlightStatus: Boolean = false;
  var isFlightSearch: Boolean = false;
  var isFlightSchedule: Boolean = false;
}
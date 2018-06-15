package org.example.beans

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/*
 * Available values form api
"ip" : "8.8.8.8"
"city" : "Mountain View"
"region" : "California"
"region_code" : "CA"
"country" : "US"
"country_name" : "United States"
"continent_code" : "NA"
"in_eu" : false
"postal" : "94035"
"latitude" : 37.386
"longitude" : -122.0838
"timezone" : "America/Los_Angeles"
"utc_offset" : "-0700"
"country_calling_code" : "+1"
"currency" : "USD"
"languages" : "en-US,es-US,haw"
"asn" : AS15169
"org" : "Google LLC"
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class LocationInformation(
    val ip: String,
    val city: String,
    val region: String,
    val latitude: Float,
    val longitude: Float,
    val country: String
)


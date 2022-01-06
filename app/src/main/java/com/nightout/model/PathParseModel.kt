package com.nightout.model

data class PathParseModel(
    val geocoded_waypoints: ArrayList<GeocodedWaypoint>,
    val routes: ArrayList<Route>,
    val status: String
)

data class GeocodedWaypoint(
    val geocoder_status: String,
    val place_id: String,
    val types: ArrayList<String>
)

data class Route(
    val bounds: Bounds,
    val copyrights: String,
    val legs: ArrayList<Leg>,
    val overview_polyline: OverviewPolyline,
    val summary: String,
    val warnings: ArrayList<Any>,
    val waypoint_order: ArrayList<Any>
)

data class Bounds(
    val northeast: Northeast,
    val southwest: Southwest
)

data class Leg(
    val distance: Distance,
    val duration: Duration,
    val end_address: String,
    val end_location: EndLocation,
    val start_address: String,
    val start_location: StartLocation,
    val steps: ArrayList<Step>,
    val traffic_speed_entry: ArrayList<Any>,
    val via_waypoint: ArrayList<Any>
)

data class OverviewPolyline(
    val points: String
)

data class Northeast(
    val lat: Double,
    val lng: Double
)

data class Southwest(
    val lat: Double,
    val lng: Double
)

data class Distance(
    val text: String,
    val value: Int
)

data class Duration(
    val text: String,
    val value: Int
)

data class EndLocation(
    val lat: Double,
    val lng: Double
)

data class StartLocation(
    val lat: Double,
    val lng: Double
)

data class Step(
    val distance: DistanceX,
    val duration: DurationX,
    val end_location: EndLocationX,
    val html_instructions: String,
    val maneuver: String,
    val polyline: Polyline,
    val start_location: StartLocationX,
    val travel_mode: String
)

data class DistanceX(
    val text: String,
    val value: Int
)

data class DurationX(
    val text: String,
    val value: Int
)

data class EndLocationX(
    val lat: Double,
    val lng: Double
)

data class Polyline(
    val points: String
)

data class StartLocationX(
    val lat: Double,
    val lng: Double
)
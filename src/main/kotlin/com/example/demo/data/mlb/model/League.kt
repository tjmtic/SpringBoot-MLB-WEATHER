package com.example.demo.data.mlb.model

import java.io.Serializable

data class League(val id: Int,
                  val name: String,
                  val link: String,
                  val venueId: Int,
                  val allStarStatus: String,
                  val season: Int,
                  val teamCode: String,
                  val fileCode: String,
                  val abbreviation: String,
                  val teamName: String,
                  val locationName: String,
                  val firstYearOfPlay: String,
                  val leagueId: Int,
                  val divisionId: Int,
                  val sportId: Int,
                  val shortName: String,
                  val parentOrgName: String,
                  val parentOrgId: Int,
                  val franchiseName: String,
                  val clubName: String,
                  val active: Boolean,
) : Serializable
/** SAMPLE RAW - FULL FIELD VALUES
 *
 * {
 *             "allStarStatus": "N",
 *             "id": 4124,
 *             "name": "Pensacola Blue Wahoos",
 *             "link": "/api/v1/teams/4124",
 *             "season": 2024,
 *             "venue": {
 *                 "id": 4329,
 *                 "name": "Blue Wahoos Stadium",
 *                 "link": "/api/v1/venues/4329"
 *             },
 *             "teamCode": "pen",
 *             "fileCode": "t4124",
 *             "abbreviation": "PNS",
 *             "teamName": "Blue Wahoos",
 *             "locationName": "Pensacola",
 *             "firstYearOfPlay": "2012",
 *             "league": {
 *                 "id": 111,
 *                 "name": "Southern League",
 *                 "link": "/api/v1/league/111"
 *             },
 *             "division": {
 *                 "id": 240,
 *                 "name": "Southern League South",
 *                 "link": "/api/v1/divisions/240"
 *             },
 *             "sport": {
 *                 "id": 12,
 *                 "link": "/api/v1/sports/12",
 *                 "name": "Double-A"
 *             },
 *             "shortName": "Pensacola",
 *             "parentOrgName": "Miami Marlins",
 *             "parentOrgId": 146,
 *             "franchiseName": "Pensacola",
 *             "clubName": "Blue Wahoos",
 *             "active": true
 *         }
 */
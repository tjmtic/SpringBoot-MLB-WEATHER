package com.example.demo.data.mlb.model

import java.io.Serializable

data class Division(val id: Int,
                    val name: String,
                    val season: String,
                    val nameShort: String,
                    val link: String,
                    val abbreviation: String,
                    val leagueId: Int,
                    val sportId: Int,
                    val hasWildcard: Boolean,
                    val sortOrder: Int,
                    val active: Boolean,

                    ) : Serializable
/** SAMPLE RAW - FULL FIELD VALUES
 *
 * {
 *             "id": 6007,
 *             "name": "North Region",
 *             "season": "2024",
 *             "nameShort": "North",
 *             "link": "/api/v1/divisions/6007",
 *             "abbreviation": "ASL-N",
 *             "league": {
 *                 "id": 6006,
 *                 "link": "/api/v1/league/6006"
 *             },
 *             "sport": {
 *                 "id": 6005,
 *                 "link": "/api/v1/sports/6005"
 *             },
 *             "hasWildcard": false,
 *             "sortOrder": 3602,
 *             "active": true
 *         },
 */
package com.example.demo.data.mlb.model

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.serializer.support.SerializationFailedException
import java.io.Serializable

data class League(val id: Int,
                  val name: String,
                  val link: String,
                  val abbreviation: String,
                  val nameShort: String,
                  val seasonState: String,
                  val hasWildCard: Boolean,
                  val hasSplitSeason: Boolean,
                  val numGames: Int,
                  val hasPlayoffPoints: Boolean,
                  val numTeams: Int,
                  val numWildcardTeams: Int,
                //SeasonDateInfo
                  val season: String,
                  val orgCode: String,
                  val conferenceInUse: Boolean,
                  val divisionInUse: Boolean,
                  val sportId: Int,
                  val sortOrder: Int,
                  val active: Boolean,

) : Serializable, StandardMapping<League>() {

    override fun map(json: String): League {
        try {
            val objectMapper = ObjectMapper()
            val teamNode = objectMapper.readTree(json)

            val id = teamNode["id"].asInt()
            val name = teamNode["name"].asText()
            val link = teamNode["link"].asText()
            val venueId = teamNode["venue"]["id"].asInt()
            val allStarStatus = teamNode["allStarStatus"].asText()
            val season = teamNode["season"].asInt()
            val teamCode = teamNode["teamCode"].asText()
            val fileCode = teamNode["fileCode"].asText()
            val abbreviation = teamNode["abbreviation"].asText()
            val teamName = teamNode["teamName"].asText()
            val locationName = teamNode["locationName"].asText()
            val firstYearOfPlay = teamNode["firstYearOfPlay"].asText()
            val leagueId = teamNode["league"]["id"].asInt()
            val divisionId = teamNode["division"]["id"].asInt()
            val sportId = teamNode["sport"]["id"].asInt()
            val shortName = teamNode["shortName"].asText()
            val parentOrgName = teamNode["parentOrgName"].asText()
            val parentOrgId = teamNode["parentOrgId"].asInt()
            val franchiseName = teamNode["franchiseName"].asText()
            val clubName = teamNode["clubName"].asText()
            val active = teamNode["active"].asBoolean()


            return League(id,
                name,
                link,
                allStarStatus,
                teamCode,
                fileCode,
                false,
                false,
                0,
                false,
                leagueId,
                divisionId,
                "sportId",
                shortName,
                false,
                false,
                0,
                0,
                active)

        } catch(e: JsonProcessingException){
            throw SerializationFailedException("Team Serialization Failure", e)
        }
        //TODO :
        // Double-Check this error propagation
        catch(e: Exception){
            throw SerializationFailedException("Team Serialization Error", e)
        }
    }
}

abstract class StandardMapping<T>{
    abstract fun map(json:String): T
}
/** SAMPLE RAW - FULL FIELD VALUES
 *
 * {
 *             "id": 103,
 *             "name": "American League",
 *             "link": "/api/v1/league/103",
 *             "abbreviation": "AL",
 *             "nameShort": "American",
 *             "seasonState": "preseason",
 *             "hasWildCard": true,
 *             "hasSplitSeason": false,
 *             "numGames": 162,
 *             "hasPlayoffPoints": false,
 *             "numTeams": 15,
 *             "numWildcardTeams": 3,
 *             "seasonDateInfo": {
 *                 "seasonId": "2024",
 *                 "preSeasonStartDate": "2024-01-01",
 *                 "preSeasonEndDate": "2024-02-21",
 *                 "seasonStartDate": "2024-02-22",
 *                 "springStartDate": "2024-02-22",
 *                 "springEndDate": "2024-03-26",
 *                 "regularSeasonStartDate": "2024-03-28",
 *                 "lastDate1stHalf": "2024-07-14",
 *                 "allStarDate": "2024-07-16",
 *                 "firstDate2ndHalf": "2024-07-19",
 *                 "regularSeasonEndDate": "2024-09-29",
 *                 "postSeasonStartDate": "2024-10-01",
 *                 "postSeasonEndDate": "2024-10-31",
 *                 "seasonEndDate": "2024-10-31",
 *                 "offseasonStartDate": "2024-11-01",
 *                 "offSeasonEndDate": "2024-12-31",
 *                 "seasonLevelGamedayType": "P",
 *                 "gameLevelGamedayType": "P",
 *                 "qualifierPlateAppearances": 3.1,
 *                 "qualifierOutsPitched": 3.0
 *             },
 *             "season": "2024",
 *             "orgCode": "AL",
 *             "conferencesInUse": false,
 *             "divisionsInUse": true,
 *             "sport": {
 *                 "id": 1,
 *                 "link": "/api/v1/sports/1"
 *             },
 *             "sortOrder": 21,
 *             "active": true
 *         }
 */
package com.example.demo.data.mlb.model

///import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Sport(val id: Int,
                 val code: String,
                 val link: String,
                 val name: String,
                 val abbreviation: String,
                 val sortOrder: Int,
                 //@SerializedName("activeStatus")
                 val activeStatus: Boolean,

                 ) : Serializable
/** SAMPLE RAW - FULL FIELD VALUES
 *
 * {
 *             "id": 1,
 *             "code": "mlb",
 *             "link": "/api/v1/sports/1",
 *             "name": "Major League Baseball",
 *             "abbreviation": "MLB",
 *             "sortOrder": 11,
 *             "activeStatus": true
 *         },
 */
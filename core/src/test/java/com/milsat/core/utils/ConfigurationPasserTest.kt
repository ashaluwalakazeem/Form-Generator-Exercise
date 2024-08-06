package com.milsat.core.utils

import com.milsat.core.domain.model.Configuration
import org.junit.Assert.assertEquals
import org.junit.Test

internal class ConfigurationPasserTest {

    private val configurationPasser = ConfigurationPasser()

    private val jsonString = """
        {
          "forms": {
            "BUILDING_MAPPING": {
              "pages": {
                "BUILDING_MAPPING": {
                  "fields": { 
                        "NAME OF BUILDING": {
                            "ui_type": "text_field",
                            "column_type": "TEXT",
                            "column_name": "NAME_BLD",
                            "required": true,
                            "min_length": 0,
                            "max_length": 500,
                                "showOnList":true
                        },
                        "ADDRESSS OF BUILDING": {
                            "ui_type": "text_field",
                            "column_type": "TEXT",
                            "column_name": "ADDRESS",
                            "required": false,
                            "min_length": 0,
                            "max_length": 500
                        },
                        "BUILDING OWNERSHIP": {
                            "ui_type": "drop_down",
                            "column_type": "TEXT",
                            "column_name": "OWNER",
                            "required": false,
                            "values": [
                            "SELECT OPTION",
                            "GOVERNMENT",
                            "GROUP",
                            "PRIVATE INDIVIDUAL"
        
                            ]
                        },
                        "BUILDING STATUS": {
                            "ui_type": "drop_down",
                            "column_type": "TEXT",
                            "column_name": "BLD_STAT",
                            "required": false,
                            "values": [
                            "SELECT OPTION",
                            "COMMERCIAL BUILDING",
                            "INSTITUTIONAL/PUBLIC SERVICE",
                            "RESIDENTIAL BUILDING",
                            "RELIGIOUS BUILDING"
        
                            ]
                        },
                        "OWNER'S FULLNAME": {
                            "ui_type": "text_field",
                            "column_type": "TEXT",
                            "column_name": "NAM_OWN",
                            "required": false,
                            "min_length": 0,
                            "max_length": 500
                        },
                        "OWNER'S PHONE NUMBER": {
                            "ui_type": "text_field",
                            "column_type": "TEXT",
                            "column_name": "NUM_OWN",
                            "required": false,
                            "min_length": 11,
                            "max_length": 11
                        },
                        "BUILDING USES": {
                            "ui_type": "drop_down",
                            "column_type": "TEXT",
                            "column_name": "BLD_USE",
                            "required": false,
                            "values": [
                            "SELECT OPTION",
                            "PRIVATE RESIDENCE",
                            "KITCHEN",
                            "BATHROOM/TOILET",
                            "GATE HOUSE",
                            "GENERATOR HOUSE",
                            "RESTAURANT/EATERY",
                            "SHOP/PLAZA",
                            "OFFICE",
                            "CHURCH",
                            "MOSQUE"
                            ]
                      }      
                     }
                  }
                }
              }
            }
        }
    """.trimIndent()

    @Test
    fun `test parseJson parses JSON correctly`() {
        // Act
        val configuration = configurationPasser.parseJson(jsonString)

        // Assert
        val expectedConfiguration = Configuration(
            forms = mapOf(
                "BUILDING_MAPPING" to Configuration.Form(
                    pages = mapOf(
                        "BUILDING_MAPPING" to Configuration.Page(
                            fields = mapOf(
                                "NAME OF BUILDING" to Configuration.Field(
                                    uiType = "text_field",
                                    columnType = "TEXT",
                                    columnName = "NAME_BLD",
                                    required = true,
                                    minLength = 0,
                                    maxLength = 500,
                                    showOnList = true
                                ),
                                "ADDRESSS OF BUILDING" to Configuration.Field(
                                    uiType = "text_field",
                                    columnType = "TEXT",
                                    columnName = "ADDRESS",
                                    required = false,
                                    minLength = 0,
                                    maxLength = 500
                                ),
                                "BUILDING OWNERSHIP" to Configuration.Field(
                                    uiType = "drop_down",
                                    columnType = "TEXT",
                                    columnName = "OWNER",
                                    required = false,
                                    values = listOf(
                                        "SELECT OPTION",
                                        "GOVERNMENT",
                                        "GROUP",
                                        "PRIVATE INDIVIDUAL"
                                    )
                                ),
                                "BUILDING STATUS" to Configuration.Field(
                                    uiType = "drop_down",
                                    columnType = "TEXT",
                                    columnName = "BLD_STAT",
                                    required = false,
                                    values = listOf(
                                        "SELECT OPTION",
                                        "COMMERCIAL BUILDING",
                                        "INSTITUTIONAL/PUBLIC SERVICE",
                                        "RESIDENTIAL BUILDING",
                                        "RELIGIOUS BUILDING"
                                    )
                                ),
                                "OWNER'S FULLNAME" to Configuration.Field(
                                    uiType = "text_field",
                                    columnType = "TEXT",
                                    columnName = "NAM_OWN",
                                    required = false,
                                    minLength = 0,
                                    maxLength = 500
                                ),
                                "OWNER'S PHONE NUMBER" to Configuration.Field(
                                    uiType = "text_field",
                                    columnType = "TEXT",
                                    columnName = "NUM_OWN",
                                    required = false,
                                    minLength = 11,
                                    maxLength = 11
                                ),
                                "BUILDING USES" to Configuration.Field(
                                    uiType = "drop_down",
                                    columnType = "TEXT",
                                    columnName = "BLD_USE",
                                    required = false,
                                    values = listOf(
                                        "SELECT OPTION",
                                        "PRIVATE RESIDENCE",
                                        "KITCHEN",
                                        "BATHROOM/TOILET",
                                        "GATE HOUSE",
                                        "GENERATOR HOUSE",
                                        "RESTAURANT/EATERY",
                                        "SHOP/PLAZA",
                                        "OFFICE",
                                        "CHURCH",
                                        "MOSQUE"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        assertEquals(expectedConfiguration, configuration)
    }
}

package com.milsat.core.utils

import android.content.Context
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.*
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.milsat.core.data.db.CapstoneDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.InputStream
import java.io.IOException
import java.security.GeneralSecurityException
import kotlin.coroutines.resume

class GoogleSheetsUtils(context: Context) : KoinComponent {
    private val capstoneDao: CapstoneDao by inject()

    private val sheetsService: Sheets
    private val spreadsheetId = "1y05DXWOEqldnBzF5yjRMwgfFsDxBvWhCBgP_A6d3dZM"
    private val range = "Sheet1!A1:Z"

    init {
        val credential = getGoogleCredential(context.assets.open("capstone_service_account.json"))
            .createScoped(listOf("https://www.googleapis.com/auth/spreadsheets"))

        sheetsService = Sheets.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            com.google.api.client.json.gson.GsonFactory(),
            credential
        ).setApplicationName("Your Application Name")
            .build()
    }

    // Load and create GoogleCredential from the JSON key file
    private fun getGoogleCredential(inputStream: InputStream): GoogleCredential {
        return try {
            GoogleCredential.fromStream(inputStream)
        } catch (e: IOException) {
            throw RuntimeException("Failed to load service account credentials", e)
        }
    }

    // Override sheet values with new values
    private suspend fun updateCapstoneSheet(csvData: String): Result<Boolean> =
        suspendCancellableCoroutine {
            try {
                // Clear existing values
                sheetsService.spreadsheets().values()
                    .clear(spreadsheetId, range, ClearValuesRequest()).execute()

                // Update with new values
                val values = parseCsvData(csvData)
                val body = ValueRange().setValues(values)
                val k = sheetsService.spreadsheets().values().update(spreadsheetId, range, body)
                    .setValueInputOption("RAW")
                    .execute()
                if(it.isActive){
                    it.resume(Result.Success(true, msg = "File Synced successfully"))
                }
            } catch (e: GeneralSecurityException) {
                if (it.isActive) {
                    it.resume(
                        Result.Error(
                            ErrorResponse.Unknown(
                                e.message ?: "An unexpected error occurred"
                            )
                        )
                    )
                }
                e.printStackTrace()
                // Handle security exception
            } catch (e: IOException) {
                if (it.isActive) {
                    it.resume(Result.Error(ErrorResponse.ConnectionError()))
                }
                e.printStackTrace()
            } catch (e: Exception) {
                if (it.isActive) {
                    it.resume(Result.Error(ErrorResponse.Unknown()))
                }
                e.printStackTrace()
            }
        }

    suspend fun sync(formInt: Int): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            val fieldEntities = capstoneDao.getFieldsByFormId(formInt)
            val csvData = CsvUtils.convertFormToCsv(fieldEntities)
            updateCapstoneSheet(csvData)
        }
    }

    private fun parseCsvData(csvData: String): List<List<Any>> {
        return csvData.lines().map { line ->
            line.split(",").map { cell -> cell.trim() }
        }
    }

    // Read values from the sheet
    fun readValues(): List<List<Any>> {
        return try {
            val response = sheetsService.spreadsheets().values().get(spreadsheetId, range).execute()
            response.getValues() ?: emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }
}

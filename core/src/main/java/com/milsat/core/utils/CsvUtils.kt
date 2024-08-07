package com.milsat.core.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.milsat.core.data.db.CapstoneDao
import com.milsat.core.data.db.entities.FieldsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.random.Random

class CsvUtils : KoinComponent {
    private val capstoneDao: CapstoneDao by inject()
    private val context: Context by inject()

    // Convert the list of objects to CSV format
    private fun convertFormToCsv(fieldEntities: List<FieldsEntity>): String {
        return buildString {
            append("S/N, Column Name, Title, Value\n")
            fieldEntities.forEachIndexed { index, fieldEntity ->
                append("${index + 1},${fieldEntity.columnName},${fieldEntity.fieldTitle},${fieldEntity.columnValue}\n")
            }
        }
    }

    // Save the CSV string to a temporary file
    private fun saveCsvToTempFile(csvData: String, fileName: String): File? {
        val tempFile = File(context.cacheDir, fileName)
        return try {
            FileOutputStream(tempFile).use { outputStream ->
                outputStream.write(csvData.toByteArray())
            }
            tempFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    // Share the file using an intent
    private fun shareFile(file: File) {
        val fileUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, fileUri)
            type = "text/csv"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant temporary read permission
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share CSV File").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    // Export and share the field entities as a CSV file
    suspend fun exportFieldEntitiesToCsv(formName: String, formInt: Int) {
        withContext(Dispatchers.IO) {
            val fieldEntities = capstoneDao.getFieldsByFormId(formInt)
            val csvData = convertFormToCsv(fieldEntities)
            val fileName = "$formName-${Random.nextLong(100, 1000000)}.csv"
            val csvFile = saveCsvToTempFile(csvData, fileName)

            withContext(Dispatchers.Main) {
                csvFile?.let {
                    shareFile(it)
                }
            }
        }
    }
}

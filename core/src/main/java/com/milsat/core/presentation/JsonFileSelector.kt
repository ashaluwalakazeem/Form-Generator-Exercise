package com.milsat.core.presentation


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.result.ActivityResultLauncher
import com.milsat.core.common.Logger
import java.io.BufferedReader
import java.io.InputStreamReader

class JsonFileSelector(private val context: Context) {

    private var onJsonFileSelected: ((String) -> Unit)? = null
    private var onError: ((String) -> Unit)? = null

    fun selectJsonFile(launcher: ActivityResultLauncher<Intent>, onJsonFileSelected: (String) -> Unit, onError: (String) -> Unit) {
        this.onJsonFileSelected = onJsonFileSelected
        this.onError = onError
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
        }
        launcher.launch(intent)
    }

    fun handleFileSelection(uri: Uri) {
        try {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                val fileName = cursor.getString(nameIndex)
                if (!fileName.endsWith(".json")) {
                    onError?.invoke("Selected file is not a JSON file")
                    return
                }
            }

            val inputStream = context.contentResolver.openInputStream(uri)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val content = reader.readText()
            reader.close()
            onJsonFileSelected?.invoke(content)
        } catch (e: Exception) {
            Logger.error("JsonFileSelector", "Error reading file: ${e.message}",)
            e.printStackTrace()
            onError?.invoke("Error reading file: ${e.message}")
        }
    }
}


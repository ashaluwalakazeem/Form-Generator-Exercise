package com.milsat.core.data.repository

import com.milsat.core.data.db.CapstoneDao
import com.milsat.core.data.db.entities.FieldsEntity
import com.milsat.core.data.db.entities.FormEntity
import com.milsat.core.domain.model.Configuration
import com.milsat.core.domain.repository.FormRepository
import com.milsat.core.utils.ConfigurationPasser
import com.milsat.core.utils.ErrorResponse
import com.milsat.core.utils.Logger
import com.milsat.core.utils.Result
import com.milsat.core.utils.parseError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class FormRepositoryImpl(
    private val capstoneDao: CapstoneDao,
    private val configurationPasser: ConfigurationPasser
): FormRepository {

    override suspend fun createNewForm(config: String): Result<FormEntity> {
        return try {
            withContext(Dispatchers.IO){
                // Parse Json
                val configuration = configurationPasser.parseJson(jsonString = config) ?: return@withContext Result.Error(ErrorResponse.UnProcessableEntityError())


                if(configuration.forms.isEmpty()) return@withContext Result.Error(ErrorResponse.UnProcessableEntityError("This configuration file contains an empty form. Please select a valid one."))
                val configurationForm = configuration.forms.entries.first()
                val configurationPages = configurationForm.value.pages.entries
                val formName = configurationForm.key
                val pageSize = configurationPages.size

                // Add form to the db
                var formEntity = FormEntity(name = formName, pageCount = pageSize)
                val insertedFormId = capstoneDao.insertFormEntity(formEntity = formEntity)
                formEntity = formEntity.copy(id = insertedFormId.toInt())

                val fields = configurationPages.flatMap { entry: Map.Entry<String, Configuration.Page> ->
                    entry.value.fields.map { field: Map.Entry<String, Configuration.Field> ->
                        field.value.toDomain(formId = formEntity.id, fieldTitle = field.key, pageName = entry.key)
                    }
                }

                // Add fields to the db
                capstoneDao.insertFieldsEntities(fields)

                Result.Success(formEntity)
            }
        }catch (e: Exception){
            Result.Error(e.parseError())
        }
    }

    override fun getAllFormEntities(): Flow<List<FormEntity>> {
        return capstoneDao.getAllFormEntities()
    }

    override suspend fun fetchAllFormFields(formId: Int): List<FieldsEntity> {
        return capstoneDao.getFieldsByFormId(formId)
    }

    override suspend fun submitForm(fieldsEntities: List<FieldsEntity>) {
        capstoneDao.updateFieldEntities(fieldsEntities)
    }

    companion object{
        private const val TAG = "FormRepositoryImpl"
    }
}
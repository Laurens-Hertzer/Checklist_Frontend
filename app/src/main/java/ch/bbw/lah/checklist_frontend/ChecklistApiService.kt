package ch.bbw.lah.checklist_frontend

import ch.bbw.lah.checklist_frontend.model.Checklist
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ChecklistApiService {

    @GET("api/checklist")
    suspend fun getChecklist(): List<Checklist>

    @POST("api/checklist")
    suspend fun saveItem(@Body item: Checklist): Checklist

    @PUT("api/checklist/{id}")
    suspend fun updateItem(@Path("id") id: Int?, @Body item: Checklist): Checklist
}
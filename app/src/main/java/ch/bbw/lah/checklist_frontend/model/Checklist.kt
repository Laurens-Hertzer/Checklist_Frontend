package ch.bbw.lah.checklist_frontend.model

import kotlinx.serialization.Serializable

@Serializable
data class Checklist(
    val checklistId: Int? = null,
    val task: String,
    var completed: Boolean,
    val dueDate: String
)
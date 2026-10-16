package ch.bbw.lah.checklist_frontend.model

import java.time.LocalDate

data class Setting(
    var settingId: Long,
    var trainStarting: String,
    var trainDestination: String,
    var weatherPlace: String
)
package sdt.tkm.at.steeldarttrainer.social

data class Achievement(val id: Int, val title: String, val description: String, val type: AchievementType, val amount: Int, var successful: Boolean, var currentAmount: Int = 0)
package sdt.tkm.at.steeldarttrainer.social

data class Achivement(val id: Int, val title: String, val description: String, val type: AchivementType, val amount: Int, var successful: Boolean, var currentAmount: Int = 0)
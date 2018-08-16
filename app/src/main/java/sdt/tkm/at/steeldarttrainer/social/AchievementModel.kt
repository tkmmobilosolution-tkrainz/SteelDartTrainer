package sdt.tkm.at.steeldarttrainer.social

import android.content.Context
import sdt.tkm.at.steeldarttrainer.R
import sdt.tkm.at.steeldarttrainer.base.DataHolder
import sdt.tkm.at.steeldarttrainer.base.LogEventsHelper

class AchievementModel(val context: Context) {

    private fun achievementList(): ArrayList<Achievement> {
        val achievements: ArrayList<Achievement> = arrayListOf()

        achievements.addAll(playedGamesAchievements())
        achievements.addAll(dartsAchievements())
        achievements.addAll(sixtyAchievements())
        achievements.addAll(hundretAchievements())
        achievements.addAll(hundretFourtyAchievements())
        achievements.addAll(hundretEightyAchievements())
        achievements.addAll(dailyOpenAchievements())
        return achievements
    }

    fun dailyOpenAchievements(): ArrayList<Achievement> {
        val darts = arrayListOf<Int>(7, 14, 30, 100, 365)
        var list: ArrayList<Achievement> = arrayListOf()

        darts.forEachIndexed { index, element ->
            list.add(Achievement(70+index, String.format(context.getString(R.string.achievment_open_day_title), element), String.format(context.getString(R.string.achievment_open_day_description), element), AchievementType.DAILY_OPEN, element, false))
        }

        return list
    }

    fun getFirstLockedDailyOpenAchievement(): Achievement {
        val allList = DataHolder(context).getAchievements()
        var dartList = arrayListOf<Achievement>()

        for (achievement in allList) {
            if (achievement.type == AchievementType.DAILY_OPEN) {
                dartList.add(achievement)
            }
        }
        for (dart in dartList) {
            if (!dart.successful) {
                return dart
            }
        }

        return dartList.last()
    }

    fun hundretFourtyAchievements(): ArrayList<Achievement> {
        val darts = arrayListOf<Int>(5, 10, 25, 50, 100)
        var list: ArrayList<Achievement> = arrayListOf()

        darts.forEachIndexed { index, element ->
            list.add(Achievement(50+index, String.format(context.getString(R.string.achievment_hundret_fourty_title), element), String.format(context.getString(R.string.achievment_hundret_fourty_description), element), AchievementType.HUNDRET_FOURTY_PLUS, element, false))
        }

        return list
    }

    fun getFirstLockedHundretAchievement(): Achievement {
        val allList = DataHolder(context).getAchievements()
        var dartList = arrayListOf<Achievement>()

        for (achievement in allList) {
            if (achievement.type == AchievementType.HUNDRET_PLUS) {
                dartList.add(achievement)
            }
        }
        for (dart in dartList) {
            if (!dart.successful) {
                return dart
            }
        }

        return dartList.last()
    }

    fun hundretAchievements(): ArrayList<Achievement> {
        val darts = arrayListOf<Int>(10, 25, 50, 100, 250)
        var list: ArrayList<Achievement> = arrayListOf()

        darts.forEachIndexed { index, element ->
            list.add(Achievement(40+index, String.format(context.getString(R.string.achievment_hundret_title), element), String.format(context.getString(R.string.achievment_hundret_description), element), AchievementType.HUNDRET_PLUS, element, false))
        }

        return list
    }

    fun getFirstLockedSixtyAchievement(): Achievement {
        val allList = DataHolder(context).getAchievements()
        var dartList = arrayListOf<Achievement>()

        for (achievement in allList) {
            if (achievement.type == AchievementType.SIXTY_PLUS) {
                dartList.add(achievement)
            }
        }
        for (dart in dartList) {
            if (!dart.successful) {
                return dart
            }
        }

        return dartList.last()
    }

    fun sixtyAchievements(): ArrayList<Achievement> {
        val darts = arrayListOf<Int>(25, 50, 100, 250, 500, 100)
        var list: ArrayList<Achievement> = arrayListOf()

        darts.forEachIndexed { index, element ->
            list.add(Achievement(30+index, String.format(context.getString(R.string.achievment_sixty_title), element), String.format(context.getString(R.string.achievment_sixty_description), element), AchievementType.SIXTY_PLUS, element, false))
        }

        return list
    }

    fun getFirstLockedHundretFourtyAchievement(): Achievement {
        val allList = DataHolder(context).getAchievements()
        var dartList = arrayListOf<Achievement>()

        for (achievement in allList) {
            if (achievement.type == AchievementType.HUNDRET_FOURTY_PLUS) {
                dartList.add(achievement)
            }
        }
        for (dart in dartList) {
            if (!dart.successful) {
                return dart
            }
        }

        return dartList.last()
    }

    fun hundretEightyAchievements(): ArrayList<Achievement> {
        val darts = arrayListOf<Int>(5, 10)
        var list: ArrayList<Achievement> = arrayListOf()

        list.add(Achievement(60, context.getString(R.string.achievment_hundret_eighty_title_first), context.getString(R.string.achievment_hundret_eighty_description_first), AchievementType.HUNDRET_EIGHTIES, 1, false))

        darts.forEachIndexed { index, element ->
            list.add(Achievement(61+index, String.format(context.getString(R.string.achievment_hundret_eighty_title_multiple), element), String.format(context.getString(R.string.achievment_hundret_eighty_description_multiple), element), AchievementType.HUNDRET_EIGHTIES, element, false))
        }

        return list
    }

    fun getFirstLockedHundretEightyAchievement(): Achievement {
        val allList = DataHolder(context).getAchievements()
        var dartList = arrayListOf<Achievement>()

        for (achievement in allList) {
            if (achievement.type == AchievementType.HUNDRET_EIGHTIES) {
                dartList.add(achievement)
            }
        }
        for (dart in dartList) {
            if (!dart.successful) {
                return dart
            }
        }

        return dartList.last()
    }



    fun dartsAchievements(): ArrayList<Achievement> {
        val darts = arrayListOf<Int>(100, 250, 500, 1000, 2500, 5000, 10000)
        var list: ArrayList<Achievement> = arrayListOf()

        darts.forEachIndexed { index, element ->
            list.add(Achievement(20+index, String.format(context.getString(R.string.achievment_darts_title), element), String.format(context.getString(R.string.achievment_darts_description), element), AchievementType.DART_AMOUNT, element, false))
        }

        return list
    }

    fun getFirstLockedDartAchievement(): Achievement {
        val allList = DataHolder(context).getAchievements()
        var dartList = arrayListOf<Achievement>()

        for (achievement in allList) {
            if (achievement.type == AchievementType.DART_AMOUNT) {
                dartList.add(achievement)
            }
        }
        for (dart in dartList) {
            if (!dart.successful) {
                return dart
            }
        }

        return dartList.last()
    }

    fun getFirstLockedPlayedGamesAchievement(): Achievement {
        val allList = DataHolder(context).getAchievements()
        var exercisesList = arrayListOf<Achievement>()

        for (achievement in allList) {
            if (achievement.type == AchievementType.EXERCISES_DONE) {
                exercisesList.add(achievement)
            }
        }
        for (dart in exercisesList) {
            if (!dart.successful) {
                return dart
            }
        }

        return exercisesList.last()
    }

    fun playedGamesAchievements(): ArrayList<Achievement> {
        val darts = arrayListOf<Int>(5, 10, 25, 50, 100, 250, 500)
        var list: ArrayList<Achievement> = arrayListOf()

        list.add(Achievement(10, context.getString(R.string.achievment_exercise_done_title_one), context.getString(R.string.achievment_exercise_done_description_one), AchievementType.EXERCISES_DONE, 1, false))

        darts.forEachIndexed { index, element ->
            list.add(Achievement(11 + index, String.format(context.getString(R.string.achievment_exercise_done_title_multiple), element), String.format(context.getString(R.string.achievment_exercise_done_description_multiple), element), AchievementType.EXERCISES_DONE, element, false))
        }

        return list
    }

    fun checkAchievements() {
        var achievements: ArrayList<Achievement> = achievementList()
        var dataholderAchievements = DataHolder(context).getAchievements()
        if (dataholderAchievements.size == 0 || achievements.size > dataholderAchievements.size) {
            DataHolder(context).updateAchievements(achievements)
        }
        checkAchievmentModiefied()

        sortSuccessAchievements(DataHolder(context).getAchievements())
    }

    fun checkAchievmentModiefied(): Boolean {
        var achievements: ArrayList<Achievement> = achievementList()
        var dataholderAchievements = DataHolder(context).getAchievements()
        if (dataholderAchievements.size == 0 || achievements.size > dataholderAchievements.size) {
            DataHolder(context).updateAchievements(achievements)
        }

        DataHolder(context).updateAchievements(achievements)

        val logEventHelper = LogEventsHelper(context)
        achievements = DataHolder(context).getAchievements()
        var modiefied = false

        for (achievement in achievements) {

            if (!achievement.successful) {
                when (achievement.type) {
                    AchievementType.DART_AMOUNT -> {
                        val amount = achievement.amount
                        if (amount <= getDartsFromTrainings()) {
                            achievement.successful = true
                            logEventHelper.logAchievementSuccess(achievement.id)
                            modiefied = true
                        }
                        achievement.currentAmount = getDartsFromTrainings()
                    }
                    AchievementType.SIXTY_PLUS -> {
                        val amount = achievement.amount
                        if (amount <= countSixtyPlus()) {
                            achievement.successful = true
                            logEventHelper.logAchievementSuccess(achievement.id)
                            modiefied = true
                        }
                        achievement.currentAmount = countSixtyPlus()
                    }
                    AchievementType.HUNDRET_PLUS -> {
                        val amount = achievement.amount
                        if (amount <= countHundretPlus()) {
                            achievement.successful = true
                            logEventHelper.logAchievementSuccess(achievement.id)
                            modiefied = true
                        }
                        achievement.currentAmount = countHundretPlus()
                    }
                    AchievementType.HUNDRET_FOURTY_PLUS -> {
                        val amount = achievement.amount
                        if (amount <= countHundretFourtyPlus()) {
                            achievement.successful = true
                            logEventHelper.logAchievementSuccess(achievement.id)
                            modiefied = true
                        }
                        achievement.currentAmount = countHundretFourtyPlus()
                    }
                    AchievementType.HUNDRET_EIGHTIES -> {
                        val amount = achievement.amount
                        if (amount <= countHundretEighties()) {
                            achievement.successful = true
                            logEventHelper.logAchievementSuccess(achievement.id)
                            modiefied = true
                        }
                        achievement.currentAmount = countHundretEighties()
                    }
                    AchievementType.EXERCISES_DONE -> {
                        val amount = achievement.amount
                        if (amount <= countExcersices()) {
                            achievement.successful = true
                            logEventHelper.logAchievementSuccess(achievement.id)
                            modiefied = true
                        }
                        achievement.currentAmount = countExcersices()
                    }
                    AchievementType.DAILY_OPEN -> {
                        val amount = achievement.amount
                        if (amount <= DataHolder(context).getOpenCount()) {
                            achievement.successful = true
                            logEventHelper.logAchievementSuccess(achievement.id)
                            modiefied = true
                        }
                        achievement.currentAmount = DataHolder(context).getOpenCount()
                    }
                }
            }
        }

        DataHolder(context).updateAchievements(achievements)

        return modiefied
    }

    private fun sortSuccessAchievements(achievements: ArrayList<Achievement>) {

        var newList: ArrayList<Achievement> = arrayListOf()
        achievements.forEachIndexed { index, achievement ->
            if (achievement.successful) {
                newList.add(newList.size, achievement)
            } else {
                newList.add(index, achievement)
            }
        }
        for (achievement in achievements) {

        }

        DataHolder(context).updateAchievements(newList)
    }

    fun getDartsFromTrainings(): Int {
        var darts = 0
        val dataholder = DataHolder(context)
        val xoiTrainings = dataholder.getXOITrainingsList()
        val hsTrainings = dataholder.getHighscoreTrainingsList()
        val randomTrainings = dataholder.getRandomTrainingsList()
        val atcTrainings = dataholder.getATCTrainingsList()
        val xxTrainings = dataholder.getXXTrainingsList()

        if (xoiTrainings.size > 0) {
            for (training in xoiTrainings) {
                darts += training.dartAmount
            }
        }

        if (hsTrainings.size > 0) {
            darts += (hsTrainings.size * 30)
        }

        if (randomTrainings.size > 0) {
            darts += (randomTrainings.size * 30)
        }

        if (xxTrainings.size > 0) {
            darts += xxTrainings.size * 99
        }

        if (atcTrainings.size > 0) {
            for (training in atcTrainings) {
                darts += training.darts
            }
        }

        return darts
    }

    fun countHundretEighties(): Int {
        var hundretEighties = 0
        val dataholder = DataHolder(context)
        val xoiTrainings = dataholder.getXOITrainingsList()
        val hsTrainings = dataholder.getHighscoreTrainingsList()

        if (xoiTrainings.size > 0) {
            for (training in xoiTrainings) {
                if (training.hundretEighty > 0) {
                    hundretEighties += training.hundretEighty
                }
            }
        }

        if (hsTrainings.size > 0) {
            for (training in hsTrainings) {
                if (training.hundretEighty > 0) {
                    hundretEighties += training.hundretEighty
                }
            }
        }

        return hundretEighties
    }

    fun countExcersices(): Int {
        var count = 0
        val dataholder = DataHolder(context)
        val xoiTrainings = dataholder.getXOITrainingsList()
        val hsTrainings = dataholder.getHighscoreTrainingsList()
        val randomTrainings = dataholder.getRandomTrainingsList()
        val atcTrainings = dataholder.getATCTrainingsList()
        val xxTrainings = dataholder.getXXTrainingsList()

        if (xoiTrainings.size > 0) {
            count += xoiTrainings.size
        }

        if (hsTrainings.size > 0) {
            count += hsTrainings.size
        }

        if (randomTrainings.size > 0) {
            count += randomTrainings.size
        }

        if (xxTrainings.size > 0) {
            count += xxTrainings.size
        }

        if (atcTrainings.size > 0) {
            count += atcTrainings.size
        }

        return count
    }

    fun countHundretFourtyPlus(): Int {
        var hundretFourty = 0
        val dataholder = DataHolder(context)
        val xoiTrainings = dataholder.getXOITrainingsList()
        val hsTrainings = dataholder.getHighscoreTrainingsList()

        if (xoiTrainings.size > 0) {
            for (training in xoiTrainings) {
                if (training.hundretFourtyPlus > 0) {
                    hundretFourty += training.hundretFourtyPlus
                }
            }
        }

        if (hsTrainings.size > 0) {
            for (training in hsTrainings) {
                if (training.hundretFourtyPlus > 0) {
                    hundretFourty += training.hundretFourtyPlus
                }
            }
        }

        return hundretFourty
    }

    fun countHundretPlus(): Int {
        var hundret = 0
        val dataholder = DataHolder(context)
        val xoiTrainings = dataholder.getXOITrainingsList()
        val hsTrainings = dataholder.getHighscoreTrainingsList()

        if (xoiTrainings.size > 0) {
            for (training in xoiTrainings) {
                if (training.hundretPlus > 0) {
                    hundret += training.hundretPlus
                }
            }
        }

        if (hsTrainings.size > 0) {
            for (training in hsTrainings) {
                if (training.hundretPlus > 0) {
                    hundret += training.hundretPlus
                }
            }
        }

        return hundret
    }

    fun countSixtyPlus(): Int {
        var sixty = 0
        val dataholder = DataHolder(context)
        val xoiTrainings = dataholder.getXOITrainingsList()
        val hsTrainings = dataholder.getHighscoreTrainingsList()

        if (xoiTrainings.size > 0) {
            for (training in xoiTrainings) {
                if (training.sixtyPlus > 0) {
                    sixty += training.sixtyPlus
                }
            }
        }

        if (hsTrainings.size > 0) {
            for (training in hsTrainings) {
                if (training.sixtyPlus > 0) {
                    sixty += training.sixtyPlus
                }
            }
        }

        return sixty
    }

}
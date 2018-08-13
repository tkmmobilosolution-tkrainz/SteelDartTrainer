package sdt.tkm.at.steeldarttrainer.social

import android.content.Context
import sdt.tkm.at.steeldarttrainer.base.DataHolder
import sdt.tkm.at.steeldarttrainer.base.LogEventsHelper

class AchivementModel(val context: Context) {

    private fun achivementList(): ArrayList<Achivement> {
        val achivements: ArrayList<Achivement> = arrayListOf()
        val hundretEigthyAchievment = Achivement(1, "First 180", "Throw your first 180 in XOI or Highscore exercises", AchivementType.HUNDRET_EIGHTIES, 1, false)

        val hundretThronDarts = Achivement(2, "Throw 100 Darts", "Throw 100 Darts in all exercises", AchivementType.DART_AMOUNT, 100, false)
        val fivehundretThronDarts = Achivement(3, "Throw 500 Darts", "Throw 500 Darts in all exercises", AchivementType.DART_AMOUNT, 500, false)
        val tausendThronDarts = Achivement(4, "Throw 1000 Darts", "Throw 1000 Darts in all exercises", AchivementType.DART_AMOUNT, 1000, false)

        val fivePlayedGames = Achivement(5, "5 Exercises", "Play 5 exercises", AchivementType.EXERCISES_DONE, 5, false)
        val twentyPlayedGames = Achivement(6, "20 Exercises", "Play 20 exercises", AchivementType.EXERCISES_DONE, 20, false)

        achivements.add(hundretEigthyAchievment)

        achivements.add(hundretThronDarts)
        achivements.add(fivehundretThronDarts)
        achivements.add(tausendThronDarts)

        achivements.add(fivePlayedGames)
        achivements.add(twentyPlayedGames)
        return achivements
    }

    fun checkAchevements(): ArrayList<Achivement> {
        var achivements: ArrayList<Achivement> = achivementList()
        var dataholderAchivements = DataHolder(context).getAchivements()
        if (dataholderAchivements.size == 0 || achivements.size > dataholderAchivements.size) {
            DataHolder(context).updateAchivements(achivements)
            dataholderAchivements = DataHolder(context).getAchivements()
        }

        checkAchivmentModiefied()
        return sortSuccessAchivements(dataholderAchivements)
    }

    fun checkAchivmentModiefied(): Boolean {
        val logEventHelper = LogEventsHelper(context)
        val achivements = DataHolder(context).getAchivements()
        var modiefied = false

        for (achivement in achivements) {

            if (!achivement.successful) {
                when (achivement.type) {
                    AchivementType.DART_AMOUNT -> {
                        val amount = achivement.amount
                        if (amount <= getDartsFromTrainings()) {
                            achivement.successful = true
                            logEventHelper.logAchivementSuccess(achivement.id)
                            modiefied = true
                        }
                        achivement.currentAmount = getDartsFromTrainings()
                    }
                    AchivementType.HUNDRET_EIGHTIES -> {
                        val amount = achivement.amount
                        if (amount <= countHundretEighties()) {
                            achivement.successful = true
                            logEventHelper.logAchivementSuccess(achivement.id)
                            modiefied = true
                        }
                        achivement.currentAmount = countHundretEighties()
                    }
                    AchivementType.EXERCISES_DONE -> {
                        val amount = achivement.amount
                        if (amount <= countExcersices()) {
                            achivement.successful = true
                            logEventHelper.logAchivementSuccess(achivement.id)
                            modiefied = true
                        }
                        achivement.currentAmount = countExcersices()
                    }
                }
            }
        }

        if (modiefied) {
            DataHolder(context).updateAchivements(achivements)
        }

        return modiefied
    }

    private fun sortSuccessAchivements(achivements: ArrayList<Achivement>): ArrayList<Achivement> {

        var newList: ArrayList<Achivement> = arrayListOf()
        for (achivement in achivements) {
            if (achivement.successful) {
                newList.add(newList.size, achivement)
            } else {
                newList.add(0, achivement)
            }
        }

        return newList
    }

    private fun getDartsFromTrainings(): Int {
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

    private fun countHundretEighties(): Int {
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

    private fun countExcersices(): Int {
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
}
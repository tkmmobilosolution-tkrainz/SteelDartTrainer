package sdt.tkm.at.steeldarttrainer.social

import android.content.Context
import sdt.tkm.at.steeldarttrainer.base.DataHolder

class AchivementModel(val context: Context) {

    private fun achivementList(): ArrayList<Achivement> {
        val achivements: ArrayList<Achivement> = arrayListOf()
        val hundretEigthyAchievment = Achivement("First 180", "Throw your first 180 in XOI or Highscore exercises", AchivementType.HUNDRET_EIGHTIES, 1, false)
        val tausendThronDarts = Achivement("Throw 1000 Darts", "Throw 1000 Darts in all exercises", AchivementType.DART_AMOUNT, 1000, false)
        val twentyPlayedGames = Achivement("20 Exercises", "Play 20 exercises", AchivementType.EXERCISES_DONE, 20, false)
        achivements.add(hundretEigthyAchievment)
        achivements.add(tausendThronDarts)
        achivements.add(twentyPlayedGames)
        return achivements
    }

    fun checkAchevements(): ArrayList<Achivement> {
        val achivements: ArrayList<Achivement> = achivementList()

        for (achivement in achivements) {

            when (achivement.type) {
                AchivementType.DART_AMOUNT -> {
                    val amount = achivement.amount
                    if (amount <= getDartsFromTrainings()) {
                        achivement.successful = true
                    }
                    achivement.currentAmount = getDartsFromTrainings()
                }
                AchivementType.HUNDRET_EIGHTIES -> {
                    val amount = achivement.amount
                    if (amount <= countHundretEighties()) {
                        achivement.successful = true
                    }
                    achivement.currentAmount = countHundretEighties()
                }
                AchivementType.EXERCISES_DONE -> {
                    val amount = achivement.amount
                    if (amount <= countExcersices()) {
                        achivement.successful = true
                    }
                    achivement.currentAmount = countExcersices()
                }
            }
        }

        return sortSuccessAchivements((achivements))
    }

    private fun sortSuccessAchivements(achivements: ArrayList<Achivement>): ArrayList<Achivement> {

        var newList: ArrayList<Achivement> = arrayListOf()
        for (achivement in achivements) {
            if (achivement.successful) {
                newList.add(0, achivement)
            } else {
                newList.add(newList.size, achivement)
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
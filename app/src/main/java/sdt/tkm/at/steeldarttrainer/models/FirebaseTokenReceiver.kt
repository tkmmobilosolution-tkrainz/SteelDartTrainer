package sdt.tkm.at.steeldarttrainer.models

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import sdt.tkm.at.steeldarttrainer.base.DataHolder

class FirebaseTokenReceiver: FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        super.onTokenRefresh()

        DataHolder(applicationContext).setFirebaseToken(FirebaseInstanceId.getInstance().token)
    }
}
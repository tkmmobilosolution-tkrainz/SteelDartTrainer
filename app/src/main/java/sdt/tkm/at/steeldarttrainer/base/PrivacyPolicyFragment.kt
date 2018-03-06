package sdt.tkm.at.steeldarttrainer.base

import android.app.Fragment
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import sdt.tkm.at.steeldarttrainer.R

/**
 * [Add class description here]
 *
 * Created 06.03.18
 *
 * @author Thomas Krainz-Mischitz (Level1 GmbH)
 * @version %I%, %G%
 */
class PrivacyPolicyFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.privacy_policy_fragment, container, false)
        val textView = view.findViewById<TextView>(R.id.privacyPolicyTextView)
        textView.text = Html.fromHtml(getString(R.string.privacy_policy))
        return view
    }
}
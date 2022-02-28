package com.example.bal_mukund.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.example.bal_mukund.R
import com.example.bal_mukund.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment(), CompoundButton.OnCheckedChangeListener {



    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var notificationSp: SharedPreferences
    private lateinit var notificationEditor: SharedPreferences.Editor

    private lateinit var mListener:NotificationsListener

    private val binding by lazy {
        FragmentSettingsBinding.inflate(layoutInflater)
    }

    companion object {
        var NIGHT_MODE = false
        var SEND_NOTIFICATIONS = "true"
        const val NIGHT_MODE_STATE = "dark_mode"
        const val NOTIFICATIONS_STATE = "notification_state"
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        mListener = context as NotificationsListener
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareSp()
        when (resources.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> NIGHT_MODE = true
            Configuration.UI_MODE_NIGHT_NO -> NIGHT_MODE = false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> NIGHT_MODE = false
        }

        SEND_NOTIFICATIONS = notificationSp.getString(NOTIFICATIONS_STATE, "true").toString()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nightmodeSwitch.isChecked = NIGHT_MODE
        binding.nightmodeSwitch.setOnCheckedChangeListener(this)
        binding.notificationCheckbox.setOnCheckedChangeListener(this)
        binding.notificationCheckbox.isChecked = SEND_NOTIFICATIONS.toBoolean()
    }


    override fun onStop() {
        super.onStop()
        saveSettings()
    }

    private fun prepareSp() {
        sp = requireContext().getSharedPreferences(NIGHT_MODE_STATE, AppCompatActivity.MODE_PRIVATE)
        editor = sp.edit()
        notificationSp = requireContext().getSharedPreferences(NOTIFICATIONS_STATE, AppCompatActivity.MODE_PRIVATE)
        notificationEditor = notificationSp.edit()
    }

    private fun saveSettings() {
        editor.putBoolean(NIGHT_MODE_STATE, NIGHT_MODE)
        editor.apply()
    }

    override fun onCheckedChanged(compoundBtn: CompoundButton?, isChecked: Boolean) {
        if (compoundBtn is SwitchCompat) nightModeChecker(isChecked) else notificationChecker(isChecked)
    }

    private fun nightModeChecker(check: Boolean) {
        NIGHT_MODE = if (check) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            true
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            false
        }
    }

    private fun notificationChecker(check: Boolean) {
        check.also {
            val isChecked = it.toString()
            SEND_NOTIFICATIONS = isChecked
            binding.notificationCheckbox.isChecked = it
            mListener.onNotificationsStateChanged(isChecked)
        }
        saveNotificationState()
    }

    private fun saveNotificationState(){
        notificationEditor.putString(NOTIFICATIONS_STATE, SEND_NOTIFICATIONS)
        notificationEditor.apply()
    }

    interface NotificationsListener {
        fun onNotificationsStateChanged(isChecked: String)
    }

}
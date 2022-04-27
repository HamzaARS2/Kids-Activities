package com.example.kidsactivities.ui

import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.example.kidsactivities.databinding.ActivitySettingsBinding
import com.example.kidsactivities.viewmodels.MainViewModel

class SettingsActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private val binding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var notificationSp: SharedPreferences
    private lateinit var notificationEditor: SharedPreferences.Editor


    companion object {
        var NIGHT_MODE = false
        var SEND_NOTIFICATIONS = "true"
        const val NIGHT_MODE_STATE = "dark_mode"
        const val NOTIFICATIONS_STATE = "notification_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.settingsToolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }

        prepareSp()
        when (resources.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> NIGHT_MODE = true
            Configuration.UI_MODE_NIGHT_NO -> NIGHT_MODE = false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> NIGHT_MODE = false
        }

        SEND_NOTIFICATIONS = notificationSp.getString(NOTIFICATIONS_STATE, "true").toString()


        binding.nightmodeSwitch.isChecked = NIGHT_MODE
        binding.nightmodeSwitch.setOnCheckedChangeListener(this)
        binding.notificationCheckbox.setOnCheckedChangeListener(this)
        binding.notificationCheckbox.isChecked = SEND_NOTIFICATIONS.toBoolean()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun prepareSp() {
        sp = getSharedPreferences(NIGHT_MODE_STATE, MODE_PRIVATE)
        editor = sp.edit()
        notificationSp = getSharedPreferences(NOTIFICATIONS_STATE, MODE_PRIVATE)
        notificationEditor = notificationSp.edit()
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
            viewModel.notificationsStateChanged(isChecked)
        }
        saveNotificationState()
    }

    private fun saveSettings() {
        editor.putBoolean(NIGHT_MODE_STATE,NIGHT_MODE)
        editor.apply()
    }

    private fun saveNotificationState(){
        notificationEditor.putString(
            NOTIFICATIONS_STATE,
            SEND_NOTIFICATIONS
        )
        notificationEditor.apply()
    }



    override fun onStop() {
        super.onStop()
        saveSettings()
    }
}
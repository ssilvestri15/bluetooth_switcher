package com.silvered.bluetoothswitcher.onBoarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityOptionsCompat
import com.silvered.bluetoothswitcher.SearchPcActivity
import com.silvered.bluetoothswitcher.databinding.ActivityMainBinding
import com.silvered.bluetoothswitcher.sha256

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shared = getSharedPreferences("info", MODE_PRIVATE)
        var device_id = shared.getString("device_id", null)
        if (device_id == null) {
            shared.edit().apply {
                device_id = sha256(
                    Settings.Global.getString(
                    contentResolver,
                    Settings.Global.DEVICE_NAME
                ))
                putString("device_id", device_id)
                apply()
            }
        }

        binding.btnNext.setOnClickListener {
            val intent = Intent(this, SearchPcActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, binding.pcImage, "pcTransition")
            startActivity(intent, options.toBundle())
        }

    }
}
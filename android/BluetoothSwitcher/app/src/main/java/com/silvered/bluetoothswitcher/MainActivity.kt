package com.silvered.bluetoothswitcher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import com.silvered.bluetoothswitcher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            val intent = Intent(this, SearchPcActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, binding.pcImage, "pcTransition")
            startActivity(intent, options.toBundle())
        }

    }
}
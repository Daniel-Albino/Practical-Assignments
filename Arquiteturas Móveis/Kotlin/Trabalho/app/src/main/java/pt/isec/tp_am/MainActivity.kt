package pt.isec.tp_am

import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.isec.tp_am.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnTopFive.setOnClickListener{
            startActivity(Intent(this,Top5::class.java))
        }

        binding.btnSingleplayer.setOnClickListener {
            /*val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)*/
            startActivity(GameActivity.getSinglePlayerIntent(this))
        }

        binding.btnMultiplayer.setOnClickListener {
            startActivity(Intent(this, MultiplayerActivity::class.java))
        }

        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.btnCredits.setOnClickListener {
            startActivity(Intent(this, CreditsActivity::class.java))
        }

    }
}
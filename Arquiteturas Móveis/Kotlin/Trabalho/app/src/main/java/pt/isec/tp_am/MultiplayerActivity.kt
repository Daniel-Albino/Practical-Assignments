package pt.isec.tp_am

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import pt.isec.tp_am.databinding.ActivityMultiplayerBinding
import java.io.File

class MultiplayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMultiplayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        perfil()

        binding.btnServerMode.setOnClickListener {
            startActivity(GameActivity.getServerModeIntent(this))
        }
        binding.btnClientMode.setOnClickListener {
            startActivity(GameActivity.getClientModeIntent(this))
        }
    }

    private fun perfil(){
        val map: Map<String, File>? = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            ?.listFiles { file -> file.extension  == "json" }
            ?.associateBy { it.nameWithoutExtension }

        if ((map == null) || map.isEmpty()) {
            Log.i("PROFILE_INFO", "sendClientInfo: NO userInfoJSON FOUND")
            Toast.makeText(this, getString(R.string.toast_profile), Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
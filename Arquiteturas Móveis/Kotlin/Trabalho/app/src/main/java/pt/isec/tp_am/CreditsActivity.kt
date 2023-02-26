package pt.isec.tp_am

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pt.isec.tp_am.databinding.CreditosBinding

class CreditsActivity : AppCompatActivity() {
    private lateinit var binding : CreditosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreditosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}
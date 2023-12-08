package pl.ozodbek.pdfviewerpractice.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.ozodbek.pdfviewerpractice.databinding.ActivityMainBinding
import pl.ozodbek.pdfviewerpractice.util.oneliner_viewbinding.viewBinding

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

}

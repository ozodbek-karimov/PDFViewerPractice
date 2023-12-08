package pl.ozodbek.pdfviewerpractice.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import pl.ozodbek.pdfviewerpractice.util.oneliner_viewbinding.viewBinding
import pl.ozodbek.pdfviewerpractice.util.onBackPressed
import pl.ozodbek.pdfviewerpractice.util.popBackStack
import pl.ozodbek.pdfviewerpractice.R
import pl.ozodbek.pdfviewerpractice.databinding.FragmentDetailBinding
import java.io.File

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val binding by viewBinding(FragmentDetailBinding::bind)
    private val safeArgs:DetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressed {
            popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

    }

    private fun setupUI() {
        setupActionBar()
        setupUiElements()
    }

    private fun setupUiElements() {
        val filePath = safeArgs.pdfList.filePath
        val file = File(filePath)
        binding.pdfViewer.fromFile(file).load()
    }

    private fun setupActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).title = safeArgs.pdfList.fileTitle
        binding.toolbar.setNavigationOnClickListener {
            popBackStack()
        }

    }


}
package pl.ozodbek.pdfviewerpractice.ui.fragments

import android.os.Bundle
import android.os.Environment
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pl.ozodbek.pdfviewerpractice.R
import pl.ozodbek.pdfviewerpractice.data.MyList
import pl.ozodbek.pdfviewerpractice.adapters.PdfListAdapter
import pl.ozodbek.pdfviewerpractice.util.oneliner_viewbinding.viewBinding
import pl.ozodbek.pdfviewerpractice.util.changeFragmentTo
import pl.ozodbek.pdfviewerpractice.databinding.FragmentMainBinding
import java.io.File


class MainFragment : Fragment(R.layout.fragment_main_) {

    private val binding by viewBinding(FragmentMainBinding::bind)
    private val pdfListAdapter: PdfListAdapter by lazy { PdfListAdapter() }

    private var firstTimeLoading = false

    private val originalList: MutableList<MyList> = mutableListOf()
    private val filteredList: MutableList<MyList> = originalList.toMutableList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findPdfFiles(Environment.getExternalStorageDirectory())
        setupUI()
    }

    private fun setupUI() {
        setupActionBar()
        setupRecyclerview()
        setupReadingPdfFromFile()
        setupSwipeToRefresh()
    }

    private fun filterList(query: String?) {
        filteredList.clear()
        query?.let {
            if (query.isEmpty()) {
                filteredList.addAll(originalList)
            } else {
                for (item in originalList) {
                    if (item.fileTitle.lowercase().contains(query.lowercase().trim { it <= ' ' })) {
                        filteredList.add(item)
                    }
                }
            }
        }
        updateAdapter(filteredList)
    }


    private fun updateAdapter(newList: List<MyList>) {
        pdfListAdapter.submitList(newList)
    }



    private fun setupReadingPdfFromFile() {


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                filterList(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })


    }

    private fun setupRecyclerview() {
        binding.recyclerView.adapter = pdfListAdapter

        pdfListAdapter.submitList(originalList)

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    binding.searchView.animate()
                        .translationY(-binding.searchView.height.toFloat())
                        .alpha(0f)
                        .setDuration(200)
                        .withStartAction {
                        }
                        .start()

                } else if (dy < 0) {
                    binding.searchView.animate()
                        .translationY(0f)
                        .alpha(1f)
                        .setDuration(200)
                        .start()
                }
            }
        })

        pdfListAdapter.setItemClickListener { pdfList ->
            changeFragmentTo(
               MainFragmentDirections.actionMainFragmentToDetailFragment(pdfList)
            )
        }
    }


    private fun setupActionBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener {

            binding.swipeRefresh.isRefreshing = false

            if (firstTimeLoading) {
                findPdfFiles(Environment.getExternalStorageDirectory())
                firstTimeLoading = true
            }
        }

        if (firstTimeLoading) {
            findPdfFiles(Environment.getExternalStorageDirectory())
            firstTimeLoading = true
        }
    }


    private fun findPdfFiles(directory: File) {

        val fileFormat = ".pdf"
        val listOfFiles = directory.listFiles()
        if (listOfFiles != null && listOfFiles.isNotEmpty()) {
            for (i in listOfFiles.indices) {
                if (listOfFiles[i].isDirectory) {
                    findPdfFiles(listOfFiles[i])
                } else {
                    if (listOfFiles[i].name.endsWith(fileFormat)) {
                        originalList.add(
                            MyList(
                                listOfFiles[i].name,
                                formatFileSize(listOfFiles[i].length()),
                                listOfFiles[i].absolutePath
                            )
                        )
                    }
                }
            }


        }


    }

    private fun formatFileSize(size: Long): String {
        val kilobytes = size / 1024.0
        val megabytes = kilobytes / 1024.0

        return when {
            megabytes >= 1 -> String.format("%.2f MB", megabytes)
            kilobytes >= 1 -> String.format("%.2f KB", kilobytes)
            else -> String.format("%d Bytes", size)
        }
    }


}

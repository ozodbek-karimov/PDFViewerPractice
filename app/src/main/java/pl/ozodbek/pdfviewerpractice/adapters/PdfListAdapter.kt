package pl.ozodbek.pdfviewerpractice.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.ozodbek.pdfviewerpractice.data.MyList
import pl.ozodbek.pdfviewerpractice.util.onClick
import pl.ozodbek.pdfviewerpractice.util.viewBinding
import pl.ozodbek.pdfviewerpractice.databinding.PdfItemBinding

class PdfListAdapter :
    ListAdapter<MyList, PdfListAdapter.MyViewHolder>(ToDoDiffUtil()) {

    private var itemClickListener: ((MyList) -> Unit)? = null

    fun setItemClickListener(listener: (MyList) -> Unit) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(parent.viewBinding(PdfItemBinding::inflate))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val todoList = getItem(position)
        todoList?.let { holder.bind(it, itemClickListener) }
    }


    class MyViewHolder(private val binding: PdfItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pdf: MyList, clickListener: ((MyList) -> Unit)?) {
            binding.pdfName.text = pdf.fileTitle
            binding.pdfSize.text = pdf.fileSize

            binding.root.onClick {
                clickListener?.invoke(pdf)
            }
        }
    }


    private class ToDoDiffUtil : DiffUtil.ItemCallback<MyList>() {
        override fun areItemsTheSame(oldItem: MyList, newItem: MyList) =
            oldItem.filePath == newItem.filePath

        override fun areContentsTheSame(oldItem: MyList, newItem: MyList) =
            oldItem == newItem

    }
}



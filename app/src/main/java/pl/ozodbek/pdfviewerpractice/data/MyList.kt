package pl.ozodbek.pdfviewerpractice.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyList(
    var fileTitle: String,
    val fileSize: String,
    val filePath: String
): Parcelable

package kg.docplus.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import id.zelory.compressor.Compressor
import kg.docplus.R
import java.io.*

abstract class ImagePickerHelper : AppCompatActivity() {

    companion object {
        const val PERMISSION_REQUEST_CAMERA = 1
        const val PERMISSION_REQUEST_GALLERY = 2
    }

    public fun showPickImageDialog() {
        val args = arrayOf<String>(getString(R.string.pick_photo_from_camera), getString(R.string.pick_photo_from_gallery))
        AlertDialog.Builder(this)
                .setItems(args) { dialog, w ->
                    if (w == 0) {
                        takePhotoFromCamera()
                    }
                    else{
                        Log.e("FFFSSDF","fewgdsf")
                        pickPhotoFromGallery()
                    }
                    dialog.dismiss()
                }.show()
    }



    var fileName: String = ""

     private fun takePhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.CAMERA)) {
                // TODO what to do if user deny
            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.CAMERA),
                        PERMISSION_REQUEST_CAMERA)
            }
        } else {
            fileName = System.nanoTime().toString()
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val uri = getCaptureImageOutputUri(this, fileName)
            if (uri != null) {
                val file = File(uri.path)
                if (Build.VERSION.SDK_INT >= 24) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            FileProvider.getUriForFile(this,
                                    "io.jachoteam.kaska.fileprovider", file))
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } else
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                startActivityForResult(intent, 102)
            }
        }
    }

    fun getCaptureImageOutputUri(context: Context, fileName: String): Uri? {
        var outputFileUri: Uri? = null
        val getImage = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (getImage != null) {
            outputFileUri = Uri.fromFile(File(getImage.path, "$fileName.jpeg"))
        }
        return outputFileUri
    }

    private fun pickPhotoFromGallery() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // TODO what to do if user deny
            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST_GALLERY)
            }
        } else {
            val intent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, 101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                102 -> {
                    val uri = getPickImageResultUri(this, data, fileName)
                    val file = getNormalizedUri(this, uri)
                    val path = Compressor(this)
                            .setQuality(70)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .compressToFile(File(file!!.getPath()))
                    setImagePath(Uri.fromFile(path))
                }
                101 -> {
                    if (data == null || data.data == null) return
                    val fileName = getImagePathFromInputStreamUri(this, data.data)
                    val file = File(fileName)
                    val path = Compressor(this)
                            .setQuality(70)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .compressToFile(file)
                    if (file.exists()) {
                        setImagePath(Uri.fromFile(path))
                    } else Toast.makeText(this, "Error ", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun getNormalizedUri(context: Context, uri: Uri?): Uri? {
        return if (uri != null && uri.toString().contains("content:"))
            Uri.fromFile(getPath(context, uri, MediaStore.Images.Media.DATA))
        else
            uri
    }

    private fun getPath(context: Context, uri: Uri, column: String): File? {
        val columns = arrayOf(column)
        val cursor = context.contentResolver.query(uri, columns, null, null, null) ?: return null
        val column_index = cursor.getColumnIndexOrThrow(column)
        cursor.moveToFirst()
        val path = cursor.getString(column_index)
        cursor.close()
        return File(path)
    }

    fun getPickImageResultUri(context: Context, data: Intent?, fileName: String): Uri? {
        var isCamera = true
        if (data != null && data.data != null) {
            val action = data.action
            isCamera = action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
        }
        return if (isCamera || data!!.data == null)
            getCaptureImageOutputUri(context, fileName)
        else
            data.data
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhotoFromCamera()
                }
            }
            PERMISSION_REQUEST_GALLERY -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickPhotoFromGallery()
                }
            }
        }
    }

    fun getImagePathFromInputStreamUri(context: Context, uri: Uri): String? {
        var inputStream: InputStream? = null
        var filePath: String? = null

        if (uri.getAuthority() != null) {
            try {
                inputStream = context.getContentResolver().openInputStream(uri) // context needed
                val photoFile = createTemporalFileFrom(context, inputStream)

                filePath = photoFile?.getPath()

            } catch (e: FileNotFoundException) {
                // log
            } catch (e: IOException) {
                // log
            } finally {
                try {
                    inputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

        return filePath
    }

    @Throws(IOException::class)
    private fun createTemporalFileFrom(context: Context, inputStream: InputStream?): File? {
        var targetFile: File? = null

        if (inputStream != null) {
            var read: Int
            val buffer = ByteArray(8 * 1024)

            targetFile = createTemporalFile(context)
            val outputStream = FileOutputStream(targetFile)

            do {
                read = inputStream.read(buffer)
                if (read == -1)
                    break
                outputStream.write(buffer, 0, read)
            } while (true);
            outputStream.flush()

            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return targetFile
    }

    private fun createTemporalFile(context: Context): File {
        return File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), java.util.Calendar.getInstance().timeInMillis.toString() + ".jpg") // context needed
    }

    abstract fun setImagePath(imgpath: Uri)
    abstract fun openGallery(mItemId: String)
}

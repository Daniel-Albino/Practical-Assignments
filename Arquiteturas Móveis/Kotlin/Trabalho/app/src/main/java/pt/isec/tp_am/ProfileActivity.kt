package pt.isec.tp_am

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.drawToBitmap
import androidx.core.widget.addTextChangedListener
import org.json.JSONObject
import pt.isec.tp_am.databinding.UserProfileBinding
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import kotlin.math.max
import kotlin.math.min

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : UserProfileBinding
    companion object {
        private const val TAG = "Profile"
        private const val IMAGE_EXTENSION = "png"//"img"
        enum class Mode {
            GALLERY, CAMERA
        }
    }
    private var mode = Mode.GALLERY
    private var imagePath : String? = null
    private var userName : String? = null
    private var permissionsGranted = false
        set(value) {
            field = value
            binding.profilePhoto.isEnabled = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // getUserPhoto()
        binding.profilePhotoBtn.setOnClickListener {
            Log.i(TAG, "profilePhotoBtn Clicked")
            showUserProfileDialog()
        }

        /*binding.profilePhoto.setOnClickListener {
            Log.i(TAG, "profilePhoto Clicked")
            showUserProfileDialog()
        }*/

        binding.username.addTextChangedListener {
            userName = binding.username.text.toString()
            Log.i(TAG, "usernameTextChangedListener: $userName")
        }

        binding.btnUpdateUser.setOnClickListener {
            Log.i(TAG, "btnUpdateUser Clicked")
            saveUpdate()
        }

        getUserPhoto()
        verifyPermissions()
        getUsername()
        //updatePreview()
    }

    override fun onDestroy() {
        super.onDestroy()
        externalCacheDir?.deleteRecursively()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        imagePath = String.format("%s/%s.%s", cacheDir.absolutePath,
            "_userProfilePhoto", IMAGE_EXTENSION)

        FileOutputStream(imagePath).use { fos ->
            binding.profilePhoto.drawToBitmap()
                .compress(Bitmap.CompressFormat.PNG,
                    100,fos)
        }
        Log.i(TAG, "onSaveInstanceState: $imagePath $userName")
        outState.putString("imagePathKey", imagePath)
        outState.putString("userNameKey", userName)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        imagePath = savedInstanceState.getString("imagePathKey")
        userName = savedInstanceState.getString("userNameKey")
        Log.i(TAG, "onRestoreInstanceState: $imagePath $userName")
        val imgFile = File(imagePath!!)
        if (imgFile.exists()) {
            Log.i(TAG, "onRestoreInstanceState: imgFile.exists()")
            binding.profilePhoto.setImageBitmap(
                BitmapFactory.decodeFile(imgFile.absolutePath))
        } else
            Log.i(TAG, "onRestoreInstanceState: !imgFile.exists()")
        cacheDir?.deleteRecursively()
    }

    /*******************************************************************************************/

    private fun showUserProfileDialog() {
        val builder = AlertDialog.Builder(this)
        with (builder) {
            setTitle("Pick a photo")
            setItems(arrayOf("Gallery", "Camera")) { _, index ->
                Log.i(TAG, "${Mode.values()[index]} is clicked")
                when (Mode.values()[index]) {
                    Mode.GALLERY -> {
                        mode = Mode.GALLERY
                        chooseImage()
                    }
                    Mode.CAMERA -> {
                        mode = Mode.CAMERA
                        takePhoto()
                    }
                }
            }
            show()
        }
    }

    private var startActivityForContentResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            Log.i(TAG, "startActivityForContentResult: $uri")
            imagePath = uri?.let { createFileFromUri(this, it) }
            updatePreview()
        }

    private fun chooseImage() {
        Log.i(TAG, "chooseImage: ")
        startActivityForContentResult.launch("image/*")
    }

    private var startActivityForTakePhotoResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        Log.i(TAG, "startActivityForTakePhotoResult: $success img: $imagePath")
        if (!success) imagePath = null
        updatePreview()
    }

    private fun takePhoto() {
        imagePath = getTempFilename(this)
        Log.i(TAG, "takePhoto: $imagePath")
        startActivityForTakePhotoResult.launch(FileProvider.getUriForFile(this,
            "pt.isec.tp_am.android.fileprovider", File(imagePath!!)))
    }

    private fun verifyPermissions() {
        Log.i(TAG, "verifyPermissions: ")
        if (mode == Mode.CAMERA) {
            permissionsGranted = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
            if (!permissionsGranted)
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            return
        }
        //mode == GALLERY
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsGranted = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED

            if (!permissionsGranted)
                requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            return
        }
        // GALLERY, versions < API33
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsGranted = false
            requestPermissionsLauncher.launch(
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            )
        } else
            permissionsGranted = true
    }

    private fun updatePreview() {
        if (imagePath != null) {
            setPic(binding.profilePhoto, imagePath!!)
        } else {
            binding.profilePhoto.background = ResourcesCompat.getDrawable(
                resources, android.R.drawable.ic_menu_report_image, null)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionsGranted = isGranted
    }

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { grantResults ->
        permissionsGranted = grantResults.values.any { it }
    }

    private fun getTempFilename(
        context: Context, prefix : String = "image", extension : String = ".$IMAGE_EXTENSION"
    ) : String = File.createTempFile(
        prefix, extension,
        context.externalCacheDir
    ).absolutePath

    private fun createFileFromUri(
        context: Context, uri : Uri, filename : String = getTempFilename(context)
    ) : String {
        FileOutputStream(filename).use { outputStream ->
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return filename
    }

    private fun setPic(view: View, path: String) {
        Log.i(TAG, "setPic: $path")
        getPic(view, path)?.also {
            when (view) {
                is ImageView -> (view as ImageView).setImageBitmap(it)
                else -> view.background = BitmapDrawable(view.resources, it)
            }

            when (view) { //  Log.i
                is ImageView -> Log.i(TAG, "setPic: is ImageView $view")
                else -> Log.i(TAG, "setPic: else $view")
            }
        }
    }

    private fun getPic(view: View, path: String) : Bitmap? {
        val targetW = view.width
        val targetH = view.height
        if (targetH < 1 || targetW < 1) return null
        val bmpOptions = BitmapFactory.Options()
        bmpOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, bmpOptions)
        val photoW = bmpOptions.outWidth
        val photoH = bmpOptions.outHeight
        val scale = max(1, min(photoW / targetW, photoH / targetH))
        bmpOptions.inSampleSize = scale
        bmpOptions.inJustDecodeBounds = false
        val imageBitmap : Bitmap = BitmapFactory.decodeFile(path, bmpOptions);
        //Log.i(TAG, "getPic: $photoW x $photoH - $targetW x $targetH  $scale")
        //Log.i(TAG,  "Bitmap: ${imageBitmap.width} x ${imageBitmap.height}")
        //return BitmapFactory.decodeFile(path, bmpOptions)
        return Bitmap.createBitmap(
            imageBitmap,imageBitmap.width/2 - targetW/2,
            imageBitmap.height/2 - targetH/2, targetW, targetH
        )
    }

    private fun saveUpdate() {
        if (binding.username.text.trim().isEmpty()) {
            /*Snackbar.make(binding.username,
                R.string.msg_empty_username,
                Snackbar.LENGTH_LONG).show()*/
            Toast.makeText(this@ProfileActivity,
                R.string.msg_empty_username,
                Toast.LENGTH_SHORT).show()
            binding.username.requestFocus()
            return
        }

        val filename = String.format("%s/%s.%s", getExternalFilesDir(Environment.DIRECTORY_PICTURES), "userProfilePhoto", IMAGE_EXTENSION)
        FileOutputStream(filename).use { fos ->
            binding.profilePhoto.drawToBitmap().compress(Bitmap.CompressFormat.PNG,100,fos)
        }

        val userinfo = JSONObject()
        userinfo.put("username", binding.username.text.toString())
        userinfo.put("photoImagePath", filename)
        val userinfofilename = String.format("%s/%s.%s", getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "userInformation", "json")
        FileWriter(userinfofilename).use { it.write(userinfo.toString()) }

        finish()
    }

    private fun getUserPhoto() {
        val map: Map<String, File>? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ?.listFiles { file -> file.extension == IMAGE_EXTENSION }
            ?.associateBy { it.nameWithoutExtension }
        if (map == null || map.isEmpty()) {
            Log.i(TAG, "getUserPhoto: No profile photo was found")
            return
        }

        val title = map.keys.toTypedArray()[0]
        imagePath = map[title]?.absolutePath
        Log.i(TAG, "getUserPhoto: $title, $imagePath")
        val imgFile = File(imagePath)
        if (imgFile.exists()) {
            binding.profilePhoto.setImageBitmap(
                BitmapFactory.decodeFile(imgFile.absolutePath))
        }
    }

    private fun getUsername(){
        val map: Map<String, File>? = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            ?.listFiles { file -> file.extension  == "json" }
            ?.associateBy { it.nameWithoutExtension }

        if ((map == null) || map.isEmpty()) {
            Log.i("PROFILE_INFO", "sendClientInfo: NO userInfoJSON FOUND")
            return
        }

        val title = map.keys.toTypedArray()[0]
        val userInfoFile = map[title]?.absolutePath?.let { File(it) }

        val userInfoJSON = JSONObject(userInfoFile!!.bufferedReader().use { it.readText() })

        Log.i("testeJson",userInfoJSON.get("username") as String)

        var username = userInfoJSON.get("username") as String

        binding.username.setText(username)
    }
}
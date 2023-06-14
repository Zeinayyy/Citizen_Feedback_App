package com.bangkit.citisnap.ui.addpost

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.citisnap.R
import com.bangkit.citisnap.databinding.ActivityAddPostBinding
import com.bangkit.citisnap.ui.createCustomTempFile
import com.bangkit.citisnap.ui.main.MainActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "DataUser")

class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding
    private lateinit var auth: FirebaseAuth
    private var getUri: Uri? = null
    private var getFile: File? = null
    private lateinit var addPostViewModel: AddPostViewModel

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = this.getString(R.string.add_post)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        auth = Firebase.auth
        val currentUser = auth.currentUser

        Glide.with(this).load(currentUser?.photoUrl).into(binding.photoProfile)

        setupViewModel()

        binding.post.isEnabled = false
        addTextChangeListener(binding.description, binding.post)

        binding.gallery.setOnClickListener { startGallery() }
        binding.camera.setOnClickListener { startTakePhoto() }
        binding.post.setOnClickListener {
            val desc = binding.description.text.toString()
            val name = currentUser?.displayName.toString()
            addPostViewModel.refreshToken(name, desc, getUri, dataStore)
        }

        addPostViewModel.isLoading.observe(this) { isLoading(it) }
        addPostViewModel.updateUI.observe(this) { updateUI(it) }
        addPostViewModel.message.observe(this) {
            Toast.makeText(
                this@AddPostActivity,
                it.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setupViewModel() {
        addPostViewModel = ViewModelProvider(
            this,
            ViewModelFactory(com.bangkit.citisnap.preferences.Preferences.getInstance(dataStore))
        )[AddPostViewModel::class.java]
    }

    private fun updateUI(state: Boolean) {
        if (state) {
            startActivity(Intent(this@AddPostActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun isLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
            binding.post.isEnabled = false
            binding.post.text = ""
        } else {
            binding.progressBar.visibility = View.GONE
            binding.post.isEnabled = true
            binding.post.text = getString(R.string.post)
        }

    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddPostActivity,
                "com.bangkit.citisnap",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val fileUri: Uri = FileProvider.getUriForFile(
                applicationContext,
                "com.bangkit.citisnap",
                myFile
            )
            getUri = fileUri

            binding.photo.visibility = View.VISIBLE
            binding.photo.setImageURI(getUri)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            getUri = selectedImg

            binding.photo.visibility = View.VISIBLE
            binding.photo.setImageURI(selectedImg)
        }
    }

    private fun addTextChangeListener(editText: EditText, button: Button) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                button.isEnabled = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
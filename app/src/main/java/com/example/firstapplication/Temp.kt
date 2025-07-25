//package com.example.firstapplication
//
//// Step 1: Dependencies needed in build.gradle (Module: app)
///*
//dependencies {
//    // For image loading and caching
//    implementation 'com.github.bumptech.glide:glide:4.15.1'
//    annotationProcessor 'com.github.bumptech.glide:compiler:4.15.1'
//
//    // For HTTP requests
//    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
//    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
//    implementation 'com.squareup.okhttp3:logging-interceptor:4.11.0'
//
//    // For coroutines (recommended for async operations)
//    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1'
//
//    // ViewModel and lifecycle
//    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
//    implementation 'androidx.activity:activity-ktx:1.8.2'
//}
//*/
//
//// Step 2: Required Permissions in AndroidManifest.xml
///*
//<uses-permission android:name="android.permission.INTERNET" />
//<!-- For Android 10 and below, or when targeting shared storage -->
//<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
//                 android:maxSdkVersion="29" />
//<!-- Optional: if you need to read downloaded files -->
//<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
//                 android:maxSdkVersion="32" />
//
//<!-- For Android 13+ if you want to show download notifications -->
//<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
//*/
//
//// Step 3: Layout file (activity_image_download.xml)
///*
//<?xml version="1.0" encoding="utf-8"?>
//<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:orientation="vertical"
//    android:padding="16dp">
//
//    <Button
//        android:id="@+id/btnLoadImage"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:text="Load Random Image"
//        android:layout_marginBottom="16dp" />
//
//    <ImageView
//        android:id="@+id/imageView"
//        android:layout_width="match_parent"
//        android:layout_height="300dp"
//        android:layout_marginBottom="16dp"
//        android:scaleType="centerCrop"
//        android:background="@drawable/image_placeholder"
//        android:contentDescription="Downloaded image" />
//
//    <TextView
//        android:id="@+id/tvImageInfo"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:text="No image loaded"
//        android:textSize="14sp"
//        android:layout_marginBottom="16dp" />
//
//    <Button
//        android:id="@+id/btnDownload"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:text="Download Image"
//        android:enabled="false"
//        android:background="@drawable/button_download_background" />
//
//    <TextView
//        android:id="@+id/tvDownloadStatus"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:layout_marginTop="8dp"
//        android:text=""
//        android:textSize="12sp"
//        android:gravity="center" />
//
//</LinearLayout>
//*/
//
//// Step 4: Data classes and API interface
//data class RandomImageResponse(
//    val id: String,
//    val author: String,
//    val width: Int,
//    val height: Int,
//    val url: String,
//    val download_url: String
//)
//
//// Using Picsum Photos API - free random images
//interface ImageApiService {
//    @GET("v2/list")
//    suspend fun getRandomImages(
//        @Query("page") page: Int = 1,
//        @Query("limit") limit: Int = 1
//    ): List<RandomImageResponse>
//}
//
//// Step 5: Main Activity Implementation
//class ImageDownloadActivity : AppCompatActivity() {
//    // UI Components
//    private lateinit var btnLoadImage: Button
//    private lateinit var btnDownload: Button
//    private lateinit var imageView: ImageView
//    private lateinit var tvImageInfo: TextView
//    private lateinit var tvDownloadStatus: TextView
//
//    // Current image data
//    private var currentImageUrl: String? = null
//    private var currentImageName: String? = null
//
//    // Permission management
//    private lateinit var permissionManager: PermissionManager
//
//    // API service
//    private val imageApiService by lazy { createApiService() }
//
//    // Permission launchers
//    private val storagePermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        if (isGranted) {
//            downloadCurrentImage()
//        } else {
//            showPermissionDeniedMessage()
//        }
//    }
//
//    private val notificationPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        // Notification permission granted or denied
//        // Continue with download regardless
//        downloadCurrentImage()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_image_download)
//
//        setupClickListeners()
//        setupPermissionManager()
//    }
//
//    private fun setupClickListeners() {
//        btnLoadImage.setOnClickListener {
//            loadRandomImage()
//        }
//
//        btnDownload.setOnClickListener {
//            requestDownloadPermissionsAndDownload()
//        }
//    }
//
//    private fun setupPermissionManager() {
//        permissionManager = PermissionManager(this)
//    }
//
//    private fun createApiService(): ImageApiService {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://picsum.photos/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        return retrofit.create(ImageApiService::class.java)
//    }
//
//    // Step 6: Load image from API
//    private fun loadRandomImage() {
//        btnDownload.isEnabled = false
//        tvDownloadStatus.text = ""
//
//        lifecycleScope.launch {
//            try {
//                val images = imageApiService.getRandomImages(
//                    page = (1..100).random(), // Random page for variety
//                    limit = 1
//                )
//
//                if (images.isNotEmpty()) {
//                    val image = images[0]
//                    currentImageUrl = image.downloadUrl
//                    currentImageName = "image_${image.id}.jpg"
//
//                    // Load image with Glide
//                    Glide.with(this@ImageDownloadActivity)
//                        .load(image.downloadUrl)
//                        .placeholder(R.drawable.image_placeholder)
//                        .error(R.drawable.image_error)
//                        .into(imageView)
//
//                    // Update UI
//                    tvImageInfo.text = "Image by ${image.author}\n${image.width} x ${image.height}"
//                    btnDownload.isEnabled = true
//
//                } else {
//                    showError("No images found")
//                }
//
//            } catch (e: Exception) {
//                showError("Failed to load image: ${e.message}")
//            }
//        }
//    }
//
//    // Step 7: Permission handling for download
//    private fun requestDownloadPermissionsAndDownload() {
//        when {
//            // Android 10+ with scoped storage - no permission needed for app-specific directories
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
//                // For Android 13+, request notification permission for download progress
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                    requestNotificationPermissionIfNeeded()
//                } else {
//                    downloadCurrentImage()
//                }
//            }
//
//            // Android 9 and below - need storage permission
//            else -> {
//                requestStoragePermissionIfNeeded()
//            }
//        }
//    }
//
//    private fun requestStoragePermissionIfNeeded() {
//        when {
//            ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ) == PackageManager.PERMISSION_GRANTED -> {
//                downloadCurrentImage()
//            }
//
//            ActivityCompat.shouldShowRequestPermissionRationale(
//                this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ) -> {
//                showStoragePermissionRationale()
//            }
//
//            else -> {
//                storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            }
//        }
//    }
//
//    private fun requestNotificationPermissionIfNeeded() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            when {
//                ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.POST_NOTIFICATIONS
//                ) == PackageManager.PERMISSION_GRANTED -> {
//                    downloadCurrentImage()
//                }
//
//                ActivityCompat.shouldShowRequestPermissionRationale(
//                    this,
//                    Manifest.permission.POST_NOTIFICATIONS
//                ) -> {
//                    showNotificationPermissionRationale()
//                }
//
//                else -> {
//                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
//                }
//            }
//        } else {
//            downloadCurrentImage()
//        }
//    }
//
//    // Step 8: Download implementation
//    private fun downloadCurrentImage() {
//        val imageUrl = currentImageUrl
//        val imageName = currentImageName
//
//        if (imageUrl == null || imageName == null) {
//            showError("No image to download")
//            return
//        }
//
//        btnDownload.isEnabled = false
//        tvDownloadStatus.text = "Downloading..."
//
//        lifecycleScope.launch {
//            try {
//                val success = downloadImageToStorage(imageUrl, imageName)
//
//                if (success) {
//                    tvDownloadStatus.text = "✅ Downloaded successfully!"
//                    showDownloadSuccessMessage(imageName)
//                } else {
//                    tvDownloadStatus.text = "❌ Download failed"
//                    showError("Failed to download image")
//                }
//
//            } catch (e: Exception) {
//                tvDownloadStatus.text = "❌ Download failed"
//                showError("Download error: ${e.message}")
//            } finally {
//                btnDownload.isEnabled = true
//            }
//        }
//    }
//
//    private suspend fun downloadImageToStorage(imageUrl: String, fileName: String): Boolean {
//        return withContext(Dispatchers.IO) {
//            try {
//                // Download image using OkHttp (you can also use Glide's downloadOnly)
//                val client = OkHttpClient()
//                val request = Request.Builder().url(imageUrl).build()
//                val response = client.newCall(request).execute()
//
//                if (!response.isSuccessful) return@withContext false
//
//                val inputStream = response.body?.byteStream() ?: return@withContext false
//
//                // Save to appropriate location based on Android version
//                val success = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    saveImageToMediaStore(inputStream, fileName)
//                } else {
//                    saveImageToExternalStorage(inputStream, fileName)
//                }
//
//                inputStream.close()
//                success
//
//            } catch (e: Exception) {
//                Log.e("ImageDownload", "Download failed", e)
//                false
//            }
//        }
//    }
//
//    // For Android 10+ - Use MediaStore API (Scoped Storage)
//    @RequiresApi(Build.VERSION_CODES.Q)
//    private fun saveImageToMediaStore(inputStream: InputStream, fileName: String): Boolean {
//        return try {
//            val contentValues = ContentValues().apply {
//                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
//                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyApp")
//            }
//
//            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//            uri?.let { imageUri ->
//                contentResolver.openOutputStream(imageUri)?.use { outputStream ->
//                    inputStream.copyTo(outputStream)
//                }
//                true
//            } ?: false
//
//        } catch (e: Exception) {
//            Log.e("ImageDownload", "Failed to save to MediaStore", e)
//            false
//        }
//    }
//
//    // For Android 9 and below - Use traditional external storage
//    private fun saveImageToExternalStorage(inputStream: InputStream, fileName: String): Boolean {
//        return try {
//            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//            val appDir = File(downloadsDir, "MyApp")
//
//            if (!appDir.exists()) {
//                appDir.mkdirs()
//            }
//
//            val file = File(appDir, fileName)
//            FileOutputStream(file).use { outputStream ->
//                inputStream.copyTo(outputStream)
//            }
//
//            // Notify media scanner about the new file
//            MediaScannerConnection.scanFile(
//                this,
//                arrayOf(file.absolutePath),
//                arrayOf("image/jpeg"),
//                null
//            )
//
//            true
//
//        } catch (e: Exception) {
//            Log.e("ImageDownload", "Failed to save to external storage", e)
//            false
//        }
//    }
//
//    private fun showError(message: String) {
//        tvDownloadStatus.text = "❌ $message"
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//    }
//
//    private fun showDownloadSuccessMessage(fileName: String) {
//        val message = "Image saved as $fileName in Pictures/MyApp folder"
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//
//        // Optional: Show snackbar with option to view in gallery
//        Snackbar.make(findViewById(android.R.id.content), "Image downloaded successfully!", Snackbar.LENGTH_LONG)
//            .setAction("View") {
//                openGallery()
//            }
//            .show()
//    }
//
//    private fun openGallery() {
//        val intent = Intent(Intent.ACTION_VIEW).apply {
//            type = "image/*"
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        }
//        try {
//            startActivity(intent)
//        } catch (e: ActivityNotFoundException) {
//            Toast.makeText(this, "No gallery app found", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // Permission rationale dialogs
//    private fun showStoragePermissionRationale() {
//        AlertDialog.Builder(this)
//            .setTitle("Storage Permission Required")
//            .setMessage("This app needs storage permission to download and save images to your device.")
//            .setPositiveButton("Grant") { _, _ ->
//                storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            }
//            .setNegativeButton("Cancel") { _, _ ->
//                showPermissionDeniedMessage()
//            }
//            .show()
//    }
//
//    private fun showNotificationPermissionRationale() {
//        AlertDialog.Builder(this)
//            .setTitle("Notification Permission")
//            .setMessage("Allow notifications to see download progress and completion status.")
//            .setPositiveButton("Allow") { _, _ ->
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
//                }
//            }
//            .setNegativeButton("Skip") { _, _ ->
//                downloadCurrentImage()
//            }
//            .show()
//    }
//
//    private fun showPermissionDeniedMessage() {
//        AlertDialog.Builder(this)
//            .setTitle("Permission Denied")
//            .setMessage("Without storage permission, images cannot be downloaded. You can enable it in app settings.")
//            .setPositiveButton("Settings") { _, _ ->
//                openAppSettings()
//            }
//            .setNegativeButton("Cancel", null)
//            .show()
//    }
//
//    private fun openAppSettings() {
//        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//            data = Uri.fromParts("package", packageName, null)
//        }
//        startActivity(intent)
//    }
//}
//
//// Step 9: Permission Manager (reusing from previous example)
//class PermissionManager(private val activity: Activity) {
//
//    fun isPermissionGranted(permission: String): Boolean {
//        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
//    }
//
//    fun isPermanentlyDenied(permission: String): Boolean {
//        return !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
//                && !isPermissionGranted(permission)
//    }
//}
//
///*
//// Step 10: Alternative using Glide's download functionality
//class GlideDownloadHelper {
//
//    fun downloadImageWithGlide(
//        context: Context,
//        imageUrl: String,
//        fileName: String,
//        callback: (Boolean) -> Unit
//    ) {
//        Glide.with(context)
//            .asFile()
//            .load(imageUrl)
//            .into(object : CustomTarget<File>() {
//                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
//                    // File is downloaded to Glide's cache, now copy it to desired location
//                    lifecycleScope.launch {
//                        val success = copyFileToStorage(context, resource, fileName)
//                        callback(success)
//                    }
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                    // Handle cleanup if needed
//                }
//
//                override fun onLoadFailed(errorDrawable: Drawable?) {
//                    callback(false)
//                }
//            })
//    }
//
//    private suspend fun copyFileToStorage(context: Context, sourceFile: File, fileName: String): Boolean {
//        return withContext(Dispatchers.IO) {
//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    copyToMediaStore(context, sourceFile, fileName)
//                } else {
//                    copyToExternalStorage(context, sourceFile, fileName)
//                }
//            } catch (e: Exception) {
//                Log.e("GlideDownload", "Failed to copy file", e)
//                false
//            }
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.Q)
//    private fun copyToMediaStore(context: Context, sourceFile: File, fileName: String): Boolean {
//        val contentValues = ContentValues().apply {
//            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
//            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyApp")
//        }
//
//        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//        return uri?.let { imageUri ->
//            context.contentResolver.openOutputStream(imageUri)?.use { outputStream ->
//                FileInputStream(sourceFile).use { inputStream ->
//                    inputStream.copyTo(outputStream)
//                }
//            }
//            true
//        } ?: false
//    }
//
//    private fun copyToExternalStorage(context: Context, sourceFile: File, fileName: String): Boolean {
//        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//        val appDir = File(downloadsDir, "MyApp")
//
//        if (!appDir.exists()) {
//            appDir.mkdirs()
//        }
//
//        val destinationFile = File(appDir, fileName)
//        sourceFile.copyTo(destinationFile, overwrite = true)
//
//        // Notify media scanner
//        MediaScannerConnection.scanFile(
//            context,
//            arrayOf(destinationFile.absolutePath),
//            arrayOf("image/jpeg"),
//            null
//        )
//
//        return true
//    }
//}
//*/
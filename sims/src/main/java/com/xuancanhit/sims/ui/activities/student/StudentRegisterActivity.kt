package com.xuancanhit.sims.ui.activities.student

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.xuancanhit.sims.MainActivity
import com.xuancanhit.sims.R
import com.xuancanhit.sims.model.Student
import com.xuancanhit.sims.tool.EmailDialog
import com.xuancanhit.sims.tool.InternetDialog
import com.xuancanhit.sims.tool.ProgressButton
import kotlinx.android.synthetic.main.activity_student_register.*
import kotlinx.android.synthetic.main.dialog_email.*
import kotlinx.android.synthetic.main.layout_student_register.*
import kotlinx.android.synthetic.main.progress_button_layout.*
import java.io.ByteArrayOutputStream
import java.util.*

class StudentRegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String


    private lateinit var progressButtonStudentRegister: ProgressButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_register)

        MainActivity.hideStatusBar(window)

        //Firebase services
        auth = Firebase.auth
        database = Firebase.database.reference
        storage = Firebase.storage


        //Setup View
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        //Image View Close
        iv_stu_register_close.setOnClickListener(this)
        //Button Choose Photo
        btn_stu_register_choose_photo.setOnClickListener(this)
        //Button Take Photo
        btn_stu_register_take_photo.setOnClickListener(this)
        //Button Register
        textView_progress_button.text = "Register"
        btn_progress_button.setOnClickListener(this)
        //TextView Login
        tv_stu_register_to_login.setOnClickListener(this)
    }

    //Hieu ung Button Login
    private fun handlerButtonRegister() {
        progressButtonStudentRegister = ProgressButton(
            cardView_progress_button,
            constraint_layout_progress_button,
            progressBar_progress_button,
            textView_progress_button
        )
        progressButtonStudentRegister.buttonActivated(
            "Registering...",
            AnimationUtils.loadAnimation(this, R.anim.fade_in)
        )
        Handler(Looper.getMainLooper()).postDelayed({
            registerStudent()
        }, 1000.toLong())
    }

    private fun registerStudent() {
        // CALL getInternetStatus() function to check for internet and display error dialog
        InternetDialog(this).internetStatus

        name = edt_stu_register_name.text.toString().trim()
        email = edt_stu_register_email.text.toString().trim()
        password = edt_stu_register_password.text.toString().trim()

        if (name.isEmpty()) {
            edt_stu_register_name.error = "Enter your name!"
            edt_stu_register_name.requestFocus()
        }

        if (email.isEmpty()) {
            edt_stu_register_email.error = "Enter email address!"
            edt_stu_register_email.requestFocus()
        }

        if (password.isEmpty()) {
            edt_stu_register_password.error = "Enter password!"
            edt_stu_register_password.requestFocus()
        }

        if (password.length < 6)
            edt_stu_register_password.error = "Password too short, enter minimum 6 characters!"


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_stu_register_email.error = "Email address not valid!"
            edt_stu_register_email.requestFocus()
        }


        if (email.isNotEmpty() && password.isNotEmpty()) {
            //-----------------------Register User by email and password----------------------
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser

                        //-------------Upload Image to Storage and load link img-------------------
                        val storageRef = storage.reference

                        //Load data Image from image view
                        val calendar = Calendar.getInstance()
                        val mountainsRef =
                            storageRef.child("image" + calendar.timeInMillis + ".png")
                        // Get the data from an ImageView as bytes
                        iv_stu_register_avt.isDrawingCacheEnabled = true
                        iv_stu_register_avt.buildDrawingCache()
                        val bitmap = (iv_stu_register_avt.drawable as BitmapDrawable).bitmap
                        val byteAOS = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteAOS)
                        val data = byteAOS.toByteArray()

                        //Put data image
                        var uploadTask = mountainsRef.putBytes(data)
                        uploadTask.addOnFailureListener {
                            // Handle unsuccessful uploads
                            Log.d("SIMS_STUDENT_UP_LOAD_IMG", "Failed when upload image!")
                        }.addOnSuccessListener { taskSnapshot ->
                            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                            Log.d("SIMS_STUDENT_UP_LOAD_IMG", "Successfully when upload image!")

                            // Lay link file vua moi up
                            uploadTask.continueWithTask { task ->
                                if (!task.isSuccessful) {
                                    task.exception?.let {
                                        //Register loi
                                        progressButtonStudentRegister.buttonError(
                                            "Register error",
                                            AnimationUtils.loadAnimation(this, R.anim.fade_in)
                                        )
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            //Reset Button
                                            progressButtonStudentRegister.buttonReset(
                                                "Register",
                                                AnimationUtils.loadAnimation(this, R.anim.fade_in)
                                            )
                                        }, 1000.toLong())
                                        throw it
                                    }
                                }
                                mountainsRef.downloadUrl
                            }.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val downloadUri = task.result
                                    Log.d("SIMS_STUDENT_LINK_IMG", downloadUri.toString())

                                    //Set data user create new node in realtime database
                                    user?.let {
                                        //Set user basic profile
                                        val profileUpdates = userProfileChangeRequest {
                                            displayName = name
                                            photoUri = Uri.parse(downloadUri.toString())
                                        }
                                        user.updateProfile(profileUpdates)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    Log.d(
                                                        "SIMS_STUDENT_UPDATE_PROFILE",
                                                        "User profile updated."
                                                    )
                                                } else {
                                                    Log.d(
                                                        "SIMS_STUDENT_UPDATE_PROFILE",
                                                        "User profile update failed."
                                                    )
                                                }
                                            }

                                        //Set data Realtime Database
                                        val student = Student(
                                            email,
                                            name,
                                            "",
                                            downloadUri.toString(),
                                            "",
                                            "",
                                            "",
                                            ""
                                        )
                                        database.child("Students").child(user.uid)
                                            .setValue(student) { error, ref ->
                                                if (error == null) {
                                                    EmailDialog(this).showVerifyEmailDialog("Successful Registration","You have been registered successfully. Check your email to verify your account")


                                                    //Login thanh cong
                                                    progressButtonStudentRegister.buttonFinished(
                                                        "Registered",
                                                        AnimationUtils.loadAnimation(
                                                            this,
                                                            R.anim.fade_in
                                                        )
                                                    )
                                                    Handler(Looper.getMainLooper()).postDelayed({
                                                        //Reset Button
                                                        progressButtonStudentRegister.buttonReset(
                                                            "Register",
                                                            AnimationUtils.loadAnimation(
                                                                this,
                                                                R.anim.fade_in
                                                            )
                                                        )
                                                    }, 1000.toLong())
                                                    setEmptyUI()
                                                } else {
                                                    Toast.makeText(
                                                        this,
                                                        "Failed to register! Try again!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    Log.d(
                                                        "SIMS_STUDENT_SET_DATA_NODE",
                                                        error.toString()
                                                    )
                                                    //Register loi
                                                    progressButtonStudentRegister.buttonError(
                                                        "Register error",
                                                        AnimationUtils.loadAnimation(
                                                            this,
                                                            R.anim.fade_in
                                                        )
                                                    )
                                                    Handler(Looper.getMainLooper()).postDelayed({
                                                        //Reset Button
                                                        progressButtonStudentRegister.buttonReset(
                                                            "Register",
                                                            AnimationUtils.loadAnimation(
                                                                this,
                                                                R.anim.fade_in
                                                            )
                                                        )
                                                    }, 1000.toLong())
                                                }
                                            }
                                    }

                                } else {
                                    // Handle failures
                                    Log.d("SIMS_STUDENT_LINK_IMG", "Failed when create link image!")
                                }
                            }
                        }
                        //-------------End Upload Image to Storage and load link img-------------------


                    } else {
                        // If sign in fails, display a message to the user.
                        //Log.w("TAG", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                        //Register loi
                        progressButtonStudentRegister.buttonError(
                            "Register error",
                            AnimationUtils.loadAnimation(this, R.anim.fade_in)
                        )
                        Handler(Looper.getMainLooper()).postDelayed({
                            //Reset Button
                            progressButtonStudentRegister.buttonReset(
                                "Register",
                                AnimationUtils.loadAnimation(this, R.anim.fade_in)
                            )
                        }, 1000.toLong())
                    }
                }
        } else {
            Toast.makeText(
                this,
                "Fields cannot be left blank. Please check again!",
                Toast.LENGTH_SHORT
            ).show()
            //Register loi
            progressButtonStudentRegister.buttonError(
                "Register error",
                AnimationUtils.loadAnimation(this, R.anim.fade_in)
            )
            Handler(Looper.getMainLooper()).postDelayed({
                //Reset Button
                progressButtonStudentRegister.buttonReset(
                    "Register",
                    AnimationUtils.loadAnimation(this, R.anim.fade_in)
                )
            }, 1000.toLong())
        }
    }

    //ResetUI
    private fun setEmptyUI() {
        edt_stu_register_email.setText("")
        edt_stu_register_name.setText("")
        edt_stu_register_password.setText("")
        iv_stu_register_avt.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.graduated,
                null
            )
        )
    }
    //-----------------------End register User by email and password----------------------


    //______OPEN CAMERA_______
    private val startForResultOpenCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                // Handle the Intent
                //do stuff here
                if (intent != null) {
                    val bitmap = intent.extras?.get("data") as Bitmap
                    Toast.makeText(this, bitmap.toString(), Toast.LENGTH_SHORT).show()

                    iv_stu_register_avt.setImageBitmap(bitmap)
                }
            }
        }

    private fun openCameraActivityForResult() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startForResultOpenCamera.launch(intent)
    }
    //______END OPEN CAMERA_______

    //______OPEN FILE MANAGER_______
    private val startForResultOpenFileManager =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                // Handle the Intent
                //do stuff here
                if (intent != null) {
                    val selectedImgUri = intent.data
                    if (selectedImgUri != null)
                        iv_stu_register_avt.setImageURI(selectedImgUri)
                }
            }
        }

    private fun openFileManagerActivityForResult() {
        val intent = Intent(ACTION_PICK)
        intent.type = "image/*"
        startForResultOpenFileManager.launch(intent)
    }
    //______END OPEN FILE MANAGER_______


    private fun loginActivity() {
        startActivity(Intent(this, StudentLoginActivity::class.java))
        finish()
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.iv_stu_register_close -> finish()
            R.id.btn_stu_register_take_photo -> openCameraActivityForResult()
            R.id.btn_stu_register_choose_photo -> openFileManagerActivityForResult()
            R.id.btn_progress_button -> handlerButtonRegister()
            R.id.tv_stu_register_to_login -> loginActivity()
        }
    }

    override fun onBackPressed() {
        loginActivity()
    }
}
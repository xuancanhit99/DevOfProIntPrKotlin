package com.xuancanhit.sims.ui.fragments.admin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import com.thecode.aestheticdialogs.DialogStyle
import com.thecode.aestheticdialogs.DialogType
import com.xuancanhit.sims.MainActivity
import com.xuancanhit.sims.R
import com.xuancanhit.sims.model.Admin
import com.xuancanhit.sims.model.LearningResult
import com.xuancanhit.sims.model.Student
import com.xuancanhit.sims.ui.fragments.student.ProfileFragment
import com.xuancanhit.sims.ui.interfaces.PassDataFragmentAndActivity
import kotlinx.android.synthetic.main.fragment_admin_edit_profile.*
import kotlinx.android.synthetic.main.fragment_admin_edit_profile.view.*
import kotlinx.android.synthetic.main.fragment_student_edit_profile.*
import kotlinx.android.synthetic.main.fragment_student_edit_profile.view.*
import kotlinx.android.synthetic.main.layout_student_register.*
import java.io.ByteArrayOutputStream
import java.util.*


class AdminEditProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private var admin: Admin? = null

    private var rootView:View? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_admin_edit_profile, container, false)

        // Initialize firebase
        auth = Firebase.auth
        database = Firebase.database.reference
        storage = Firebase.storage

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            database.child("Admins").child(currentUser.uid).get().addOnSuccessListener { data ->
                admin = data.getValue(Admin::class.java)
                admin?.let {
                    reload(
                        it.name.toString(),
                        it.img.toString(),
                        it.no.toString(),
                        it.phone.toString(),
                        it.email.toString()
                    )
                }
            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
                MainActivity.aestheticDialog(requireActivity(), DialogStyle.RAINBOW, DialogType.ERROR, "Error", "Error getting data!")
            }
        }

        //InitView
        rootView?.btn_ad_update_choose_photo?.setOnClickListener{
            openFileManagerActivityForResult()
        }
        rootView?.btn_ad_update_take_photo?.setOnClickListener {
            openCameraActivityForResult()
        }
        rootView?.btn_ad_update_delete_img?.setOnClickListener {
            rootView?.iv_ad_update_avt?.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.admin,
                    null
                )
            )
        }
        rootView?.btn_ad_update_exit?.setOnClickListener {
            toAdminProfileFragment()
        }
        rootView?.edt_ad_update_no?.setOnClickListener {
            MainActivity.aestheticDialog(requireActivity(),DialogStyle.RAINBOW, DialogType.WARNING, "Warning", "You cannot change this field!")
        }



        rootView?.btn_ad_update_save?.setOnClickListener {
            val user = auth.currentUser

            val email = rootView?.edt_ad_update_email?.text.toString().trim()




            //Check and update email
            if (email.isEmpty()) {
                rootView?.edt_ad_update_email?.error = "Enter email address!"
                rootView?.edt_ad_update_email?.requestFocus()
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                rootView?.edt_ad_update_email?.error = "Email address not valid!"
                rootView?.edt_ad_update_email?.requestFocus()
            }

            if (user?.email.toString() != email) {
                val input = EditText(requireContext())
                input.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                input.transformationMethod = PasswordTransformationMethod()
                var password = ""

                val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
                materialAlertDialogBuilder.setTitle(MainActivity.textWithMyFont(requireContext(),"Please enter your password to continue!", R.font.my_font, Typeface.BOLD))
                    .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                        // Respond to negative button press
                        dialog.cancel()
                    }
                    .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                        // Respond to positive button press
                        password = input.text.toString()

                        if (password !="") {
                            //Re-authenticate a user
                            val credential = EmailAuthProvider
                                .getCredential(user?.email.toString(), password)
                            user?.reauthenticate(credential)
                                ?.addOnSuccessListener {
                                    Log.d("TAG", "User re-authenticated.")
                                    //----------------- Update Email
                                    if (email.isNotEmpty()) {
                                        user.updateEmail(email)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    Log.d("TAG", "User email address updated.")
                                                }
                                            }
                                    }
                                    //----------------- Update Email


                                    //-------------------Update info student database
                                    updateInfo()
                                }?.addOnFailureListener {
                                    rootView?.edt_ad_update_name?.clearFocus()
                                    rootView?.edt_ad_update_phone?.clearFocus()
                                    rootView?.edt_ad_update_email?.clearFocus()
                                    MainActivity.aestheticDialog(requireActivity(), DialogStyle.RAINBOW, DialogType.ERROR, "Error", "User account authentication failed!")
                                }
                        } else {
                            rootView?.edt_ad_update_name?.clearFocus()
                            rootView?.edt_ad_update_phone?.clearFocus()
                            rootView?.edt_ad_update_email?.clearFocus()
                            MainActivity.aestheticDialog(requireActivity(), DialogStyle.RAINBOW, DialogType.ERROR, "Error", "User account authentication failed!")
                        }







                    }
                    .setView(input)
                    .show()
            }
            else
                updateInfo()











        }

        (activity as PassDataFragmentAndActivity?)!!.getViewFragment(rootView!!, resources.getString(R.string.edit_profile))
        (activity as PassDataFragmentAndActivity?)!!.setNavState(R.id.fragment_student_edit_profile)



        return rootView
    }

    private fun updateInfo() {
        val user = auth.currentUser
        val name = rootView?.edt_ad_update_name?.text.toString().trim()
        var no = rootView?.edt_ad_update_no?.text.toString()
        var img = ""
        val phone = rootView?.edt_ad_update_phone?.text.toString()


        //-------------Upload Image to Storage and load link img-------------------
        val storageRef = storage.reference

        //Load data Image from image view
        val calendar = Calendar.getInstance()
        val mountainsRef =
            storageRef.child("image" + calendar.timeInMillis + ".png")
        // Get the data from an ImageView as bytes
        rootView?.iv_ad_update_avt?.isDrawingCacheEnabled = true
        rootView?.iv_ad_update_avt?.buildDrawingCache()
        val bitmap = (rootView?.iv_ad_update_avt?.drawable as BitmapDrawable).bitmap
        val byteAOS = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteAOS)
        val data = byteAOS.toByteArray()
        //Put data image
        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.d("LOAD_IMG", "Failed when upload image!")
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            Log.d("LOAD_IMG", "Successfully when upload image!")
        }
        // Lay link file vua moi up
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    //Register loi
                    Log.d("GET_LINK_IMG", "Failed")
                }
            }
            mountainsRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                Log.d("LINK_IMG", downloadUri.toString())

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
                                    "UPDATE_PROFILE",
                                    "User profile updated."
                                )
                            } else {
                                Log.d(
                                    "UPDATE_PROFILE",
                                    "User profile update failed."
                                )
                            }
                        }

                    //Set data Realtime Database
                    img = downloadUri.toString()
                    updateProfileAdmin(auth.currentUser?.email.toString(), name, no, img, phone)

                }

            } else {
                // Handle failures
                Log.d("LINK_IMG", "Failed when create link image!")
            }
        }


    }

    private fun updateProfileAdmin(
        email: String,
        name: String,
        no: String,
        img: String,
        phone: String
    ) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            //Update Profile
            val profileUpdates = userProfileChangeRequest {
                displayName = name
                photoUri = Uri.parse(img)
            }
            currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "User profile updated.")
                    }
                }


            val key = database.child("Admins").child(currentUser.uid).key
            if (key==null) {
                Log.w("LOG", "Couldn't get push key for posts")
                return
            }

            val post = Admin(email, name, img, no, phone)
            val postValues = post.toMap()

            val childUpdates = hashMapOf<String, Any>(
                "/Admins/$key" to postValues,
                //"/user-posts/$userId/$key" to postValues
            )

            database.updateChildren(childUpdates).addOnSuccessListener {
                rootView?.edt_ad_update_name?.clearFocus()
                rootView?.edt_ad_update_phone?.clearFocus()
                rootView?.edt_ad_update_email?.clearFocus()
                MainActivity.aestheticDialog(requireActivity(), DialogStyle.RAINBOW, DialogType.SUCCESS, "Success", "Successfully updated!")
            }. addOnFailureListener {
                Log.d("LOG", it.toString())
                rootView?.edt_ad_update_name?.clearFocus()
                rootView?.edt_ad_update_phone?.clearFocus()
                rootView?.edt_ad_update_email?.clearFocus()
                MainActivity.aestheticDialog(requireActivity(), DialogStyle.RAINBOW, DialogType.ERROR, "Error", "An error occurred while updating, please try again later!")
            }
        }

    }


    private fun toAdminProfileFragment() {
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.body_container_admin, AdminProfileFragment())
        fragmentTransaction?.commit()
    }

    //______OPEN CAMERA_______
    private val startForResultOpenCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                // Handle the Intent
                //do stuff here
                if (intent != null) {
                    val bitmap = intent.extras?.get("data") as Bitmap
                    rootView?.iv_ad_update_avt?.setImageBitmap(bitmap)
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
                    if (selectedImgUri != null) {
                        rootView?.iv_ad_update_avt?.setImageURI(selectedImgUri)
                    }
                }
            }
        }

    private fun openFileManagerActivityForResult() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startForResultOpenFileManager.launch(intent)
    }
    //______END OPEN FILE MANAGER_______

    private fun reload(
        name: String = "",
        img: String = "",
        no: String = "",
        phone: String = "",
        email: String = "",
    ) {
        rootView?.edt_ad_update_name?.setText(name)
        rootView?.edt_ad_update_no?.setText(no)

        rootView?.edt_ad_update_phone?.setText(phone)
        rootView?.edt_ad_update_email?.setText(email)

        Picasso.get()
            .load(img)
            .placeholder(R.drawable.graduated)
            .error(R.drawable.graduated)
            .into(rootView?.iv_ad_update_avt)
    }

}
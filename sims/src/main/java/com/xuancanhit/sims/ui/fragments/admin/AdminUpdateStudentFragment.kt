package com.xuancanhit.sims.ui.fragments.admin

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
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
import com.xuancanhit.sims.model.LearningResult
import com.xuancanhit.sims.model.Student
import com.xuancanhit.sims.ui.fragments.student.ProfileFragment
import com.xuancanhit.sims.ui.interfaces.PassDataFragmentAndActivity
import kotlinx.android.synthetic.main.fragment_admin_update_student.view.*
import kotlinx.android.synthetic.main.fragment_student_edit_profile.view.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class AdminUpdateStudentFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private var student: Student? = null

    private var uid = ""

    private var updateGender = "1"
    private var rootView:View? = null
    private var stuResults: LearningResult? = null

    //for date of birth
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_admin_update_student, container, false)

        uid = arguments?.getString("UID").toString()

        // Initialize firebase
        auth = Firebase.auth
        database = Firebase.database.reference
        storage = Firebase.storage

        //InitView from uid
        database.child("Students").child(uid).get().addOnSuccessListener { data ->
            student = data.getValue(Student::class.java)
            student?.let {
                (activity as PassDataFragmentAndActivity?)!!.getViewFragment(rootView!!,resources.getString(R.string.student) + ": " + student?.name.toString())
                reload(
                    it.name.toString(),
                    it.img.toString(),
                    it.no.toString(),
                    it.dob.toString(),
                    it.group.toString(),
                    it.gender.toString(),
                    it.phone.toString(),
                    it.email.toString(),
                    it.results!!
                )
            }
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
            MainActivity.aestheticDialog(requireActivity(), DialogStyle.RAINBOW, DialogType.ERROR, "Error", "Error getting data!")
        }

        //InitView
        rootView?.btn_ad_stu_update_choose_photo?.setOnClickListener{
            openFileManagerActivityForResult()
        }
        rootView?.btn_ad_stu_update_take_photo?.setOnClickListener {
            openCameraActivityForResult()
        }
        rootView?.btn_ad_stu_update_delete_img?.setOnClickListener {
            rootView?.iv_ad_stu_update_avt?.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.graduated,
                    null
                )
            )
        }
        rootView?.btn_ad_stu_update_exit?.setOnClickListener {
            toAdminListStudentFragment()
        }
        rootView?.edt_ad_stu_update_no?.setOnClickListener {
            MainActivity.aestheticDialog(requireActivity(),DialogStyle.RAINBOW, DialogType.WARNING, "Warning", "You cannot change this field!")
        }
        rootView?.edt_ad_stu_update_email?.setOnClickListener {
            MainActivity.aestheticDialog(requireActivity(),DialogStyle.RAINBOW, DialogType.WARNING, "Warning", "You cannot change this field!")
        }

        //Set click text view Date of birth
        val date =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month
                calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                updateLabel()
            }
        rootView?.edt_ad_stu_update_dob?.setOnClickListener {
            DatePickerDialog(
                requireContext(), date,
                calendar[Calendar.YEAR], calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        //Class
        rootView?.edt_ad_stu_update_class?.setOnClickListener {
            val items =  resources.getStringArray(R.array.student_group)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.choose_your_class))
                .setItems(items) { dialog, which ->
                    // Respond to item chosen
                    rootView?.edt_ad_stu_update_class?.setText(items[which])
                }
                .show()
        }

        rootView?.rg_ad_stu_update_gender?.setOnCheckedChangeListener { group, checkedId ->
            updateGender = if (checkedId == R.id.rb_ad_stu_update_male) {
                "1"
            } else {
                "0"
            }
        }

        rootView?.btn_ad_stu_update_save?.setOnClickListener {
                updateInfo()
        }

        rootView?.edt_ad_stu_update_result_math?.setOnClickListener {
            val items =  resources.getStringArray(R.array.list_result_rus)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.select_result))
                .setItems(items) { dialog, which ->
                    // Respond to item chosen
                    rootView?.edt_ad_stu_update_result_math?.setText(items[which])
                }
                .show()
        }

        rootView?.edt_ad_stu_update_result_eng?.setOnClickListener {
            val items =  resources.getStringArray(R.array.list_result_rus)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.select_result))
                .setItems(items) { dialog, which ->
                    // Respond to item chosen
                    rootView?.edt_ad_stu_update_result_eng?.setText(items[which])
                }
                .show()
        }

        rootView?.edt_ad_stu_update_result_phy?.setOnClickListener {
            val items =  resources.getStringArray(R.array.list_result_rus)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.select_result))
                .setItems(items) { dialog, which ->
                    // Respond to item chosen
                    rootView?.edt_ad_stu_update_result_phy?.setText(items[which])
                }
                .show()
        }

        rootView?.edt_ad_stu_update_result_info?.setOnClickListener {
            val items =  resources.getStringArray(R.array.list_result_rus)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.select_result))
                .setItems(items) { dialog, which ->
                    // Respond to item chosen
                    rootView?.edt_ad_stu_update_result_info?.setText(items[which])
                }
                .show()
        }
        rootView?.edt_ad_stu_update_result_pro?.setOnClickListener {
            val items =  resources.getStringArray(R.array.list_result_rus)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.select_result))
                .setItems(items) { dialog, which ->
                    // Respond to item chosen
                    rootView?.edt_ad_stu_update_result_pro?.setText(items[which])
                }
                .show()
        }
        rootView?.edt_ad_stu_update_result_eco?.setOnClickListener {
            val items =  resources.getStringArray(R.array.list_result_rus)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.select_result))
                .setItems(items) { dialog, which ->
                    // Respond to item chosen
                    rootView?.edt_ad_stu_update_result_eco?.setText(items[which])
                }
                .show()
        }
        rootView?.edt_ad_stu_update_result_phi?.setOnClickListener {
            val items =  resources.getStringArray(R.array.list_result_rus)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.select_result))
                .setItems(items) { dialog, which ->
                    // Respond to item chosen
                    rootView?.edt_ad_stu_update_result_phi?.setText(items[which])
                }
                .show()
        }


        (activity as PassDataFragmentAndActivity?)!!.setNavState(R.id.fragment_admin_update_student)

        return rootView
    }

    private fun toAdminListStudentFragment() {
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.body_container_admin, AdminListStudentFragment())
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
                    rootView?.iv_ad_stu_update_avt?.setImageBitmap(bitmap)
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
                        rootView?.iv_ad_stu_update_avt?.setImageURI(selectedImgUri)
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

    private fun updateInfo() {

        val name = rootView?.edt_ad_stu_update_name?.text.toString().trim()
        var no = rootView?.edt_ad_stu_update_no?.text.toString()
        var email = rootView?.edt_ad_stu_update_email?.text.toString()
        var img = ""
        var dob = rootView?.edt_ad_stu_update_dob?.text.toString()
        val group = rootView?.edt_ad_stu_update_class?.text.toString()
        val phone = rootView?.edt_ad_stu_update_phone?.text.toString()
        val gender = updateGender

        //-------------Upload Image to Storage and load link img-------------------
        val storageRef = storage.reference

        //Load data Image from image view
        val calendar = Calendar.getInstance()
        val mountainsRef =
            storageRef.child("image" + calendar.timeInMillis + ".png")
        // Get the data from an ImageView as bytes
        rootView?.iv_ad_stu_update_avt?.isDrawingCacheEnabled = true
        rootView?.iv_ad_stu_update_avt?.buildDrawingCache()
        val bitmap = (rootView?.iv_ad_stu_update_avt?.drawable as BitmapDrawable).bitmap
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
        }
        // Lay link file vua moi up
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    //Register loi
                    Log.d("SIMS_STUDENT_GET_LINK_IMG", "Failed")
                }
            }
            mountainsRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                Log.d("SIMS_STUDENT_LINK_IMG", downloadUri.toString())

                //Set data user create new node in realtime database


                    //Set data Realtime Database
                    img = downloadUri.toString()
                    updateProfileStudent(email, name, no, img, dob, group, phone, gender)



            } else {
                // Handle failures
                Log.d("SIMS_STUDENT_LINK_IMG", "Failed when create link image!")
            }
        }
    }

    private fun updateProfileStudent(email: String, name: String, no: String, img: String, dob: String, group: String, phone: String, gender: String) {

            val key = database.child("Students").child(uid).key
            if (key==null) {
                Log.w("LOG", "Couldn't get push key for posts")
                return
            }

        //Update diem---------------------------------------------------------------------------------
            val stuRes = LearningResult(
                rootView?.edt_ad_stu_update_result_math?.text.toString(),
                rootView?.edt_ad_stu_update_result_eng?.text.toString(),
                rootView?.edt_ad_stu_update_result_phy?.text.toString(),
                rootView?.edt_ad_stu_update_result_info?.text.toString(),
                rootView?.edt_ad_stu_update_result_pro?.text.toString(),
                rootView?.edt_ad_stu_update_result_eco?.text.toString(),
                rootView?.edt_ad_stu_update_result_phi?.text.toString()
            )

            val post = Student(email, name, uid, img, dob, group, phone, gender, stuRes)
            val postValues = post.toMap()

            val childUpdates = hashMapOf<String, Any>(
                "/Students/$key" to postValues,
                //"/user-posts/$userId/$key" to postValues
            )

            database.updateChildren(childUpdates).addOnSuccessListener {
                rootView?.edt_ad_stu_update_name?.clearFocus()
                rootView?.edt_ad_stu_update_phone?.clearFocus()
                rootView?.edt_ad_stu_update_email?.clearFocus()
                MainActivity.aestheticDialog(requireActivity(), DialogStyle.RAINBOW, DialogType.SUCCESS, "Success", "Successfully updated!")
            }. addOnFailureListener {
                Log.d("LOG", it.toString())
                rootView?.edt_ad_stu_update_name?.clearFocus()
                rootView?.edt_ad_stu_update_phone?.clearFocus()
                rootView?.edt_ad_stu_update_email?.clearFocus()
                MainActivity.aestheticDialog(requireActivity(), DialogStyle.RAINBOW, DialogType.ERROR, "Error", "An error occurred while updating, please try again later!")
            }
    }

    private fun updateLabel() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        rootView?.edt_ad_stu_update_dob?.setText(sdf.format(calendar.time))
    }





    private fun reload(
        name: String = "",
        img: String = "",
        no: String = "",
        dob: String = "",
        group: String = "",
        gender: String = "",
        phone: String = "",
        email: String = "",
        results: LearningResult
    ) {
        stuResults = results
        rootView?.edt_ad_stu_update_name?.setText(name)
        rootView?.edt_ad_stu_update_no?.setText(no)
        rootView?.edt_ad_stu_update_dob?.setText(dob)
        rootView?.edt_ad_stu_update_class?.setText(group)

        if(gender=="0")
            rootView?.rb_ad_stu_update_female?.isChecked = true
        else
            rootView?.rb_ad_stu_update_male?.isChecked = true

        rootView?.edt_ad_stu_update_phone?.setText(phone)
        rootView?.edt_ad_stu_update_email?.setText(email)

        Picasso.get()
            .load(img)
            .placeholder(R.drawable.graduated)
            .error(R.drawable.graduated)
            .into(rootView?.iv_ad_stu_update_avt)

        rootView?.edt_ad_stu_update_result_math?.setText(results.math)
        rootView?.edt_ad_stu_update_result_eng?.setText(results.eng)
        rootView?.edt_ad_stu_update_result_phy?.setText(results.phy)
        rootView?.edt_ad_stu_update_result_info?.setText(results.info)
        rootView?.edt_ad_stu_update_result_pro?.setText(results.pro)
        rootView?.edt_ad_stu_update_result_eco?.setText(results.eco)
        rootView?.edt_ad_stu_update_result_phi?.setText(results.phi)


    }


}
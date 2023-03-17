package com.example.permissiontaking

import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.permissiontaking.databinding.ActivityMainBinding
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var storageRef = FirebaseStorage.getInstance()
    lateinit var uri : Uri
    var getImageContract = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if(it){
            Toast.makeText(this,"Permission granted", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"Permission not granted", Toast.LENGTH_LONG).show()
        }
    }

    var pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        //uri - universal resource identifier
        System.out.println("in uri $it")
        if (it != null) {
            uri = it
        }
        binding.ivImage.setImageURI(it)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
     binding.btnGetPermission.setOnClickListener {
         getImageContract.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
     }

        binding.ivImage.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                pickImage.launch("image/*")
            else{
                getImageContract.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

            }
        }
        binding.btnUploadImage.setOnClickListener {
            if(this::uri.isInitialized) {
                storageRef.getReference("image").putFile(uri).addOnSuccessListener {
                    Toast.makeText(this, " Successful", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    System.out.println("in failure $it")
                    Toast.makeText(this, " Failure $it", Toast.LENGTH_LONG).show()

                }
            }else{

            }
        }
    }
}
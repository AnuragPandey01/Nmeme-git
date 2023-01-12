package com.example.n_meme.ui.base

import android.Manifest
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

open class BaseFragment :Fragment() {

    fun requestPermission(onPermGranted: () -> Unit) {
        //showing rationale if denied many time
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(requireContext(), "This permission is needed to store image in external storage", Toast.LENGTH_SHORT).show()
            return
        }

        getPermActivityResultLauncher {
            onPermGranted()
        }.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    }

    /**
     * registerForActivityResult basically opens an activity and returns result from the activity
     *
     * params :
     *  1. ActivityResultContract -
     *      A contract specifying that an activity can be called with an input of type I and produce an output of type O.
     *      here, I is string i.e permission to be asked and O is boolean
     *  2. ActivityResultCallback provides the result[in type pf Output O as defined in param 1]
     *
     *  returns :
     *   ActivityResultLauncher -
     *      this can be used to launch the activity for result by using [.launch()] method . This method
     *      takes the input value I of the contract as specified by param 1.
     */

    private fun getPermActivityResultLauncher(onPermGranted: ()-> Unit): ActivityResultLauncher<String> {
        return registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){ isGranted:Boolean ->
            if(isGranted){
                onPermGranted()
            }
            else{
                Toast.makeText(
                    requireContext(),
                    "To save image permission is required",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
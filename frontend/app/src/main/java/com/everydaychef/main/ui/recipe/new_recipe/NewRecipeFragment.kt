package com.everydaychef.main.ui.recipe.new_recipe

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.everydaychef.EverydayChefApplication
import com.everydaychef.R
import com.everydaychef.main.MainActivity
import com.everydaychef.main.helpers.MessageUtility
import com.everydaychef.main.helpers.PermissionsUtility
import kotlinx.android.synthetic.main.fragment_cook.*
import kotlinx.android.synthetic.main.fragment_new_recipe.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


class NewRecipeFragment : Fragment(){

    @Inject lateinit var newRecipeViewModel: NewRecipeViewModel
    @Inject lateinit var permissionsUtility: PermissionsUtility
    @Inject lateinit var messageUtility: MessageUtility

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_recipe, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!!.application as EverydayChefApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageUtility.subscribe(this)
        btn_choose_ingredients.setOnClickListener{addIngredients()}

        btn_choose_image.setOnClickListener{chooseImage()}

        btn_create_recipe.setOnClickListener{
            createRecipe()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                newRecipeViewModel.GALLERY_REQUEST_CODE -> {
                    val uri: Uri? = data!!.data
                    uri?.let {
                        new_recipe_image_view.setImageURI(uri)
                        uploadImage(uri)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            permissionsUtility.RC_STORAGE_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    messageUtility.setMessage("Permission granted!")
                } else {
                    messageUtility.setMessage("Permission denied!")
                }
                return
            }
        }
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor: Cursor? = activity?.contentResolver?.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    private fun uploadImage(uri: Uri) {
        val file: File =  File(getRealPathFromURI(uri))
        val requestFile: RequestBody = RequestBody.create(
            MediaType.parse(activity!!.contentResolver.getType(uri)),
            file
        )
        // MultipartBody.Part is used to send also the actual file name
        newRecipeViewModel.selectedImage = MultipartBody.Part.createFormData("picture", file.name, requestFile)
    }


    private fun chooseImage() {
        Log.d("PRINT", "Checking for required permissions!")
        if(permissionsUtility.checkForStoragePermission(activity!!)){
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(intent, newRecipeViewModel.GALLERY_REQUEST_CODE)
        }
    }

    private fun addIngredients() {
        newRecipeViewModel.getAllIngredients()
        newRecipeViewModel.ingredients.observe(viewLifecycleOwner, Observer {
            if(newRecipeViewModel.receivedIngredients){
                var allIngredientsNames = it.map{ingredient -> ingredient.name }.toTypedArray()
                var previouslyCheckedIngredients = newRecipeViewModel.getPreviouslyChecked()
                AlertDialog.Builder(activity)
                    .setTitle("Add ingredients to recipe!")
                    .setMultiChoiceItems(allIngredientsNames, previouslyCheckedIngredients )
                    { dialog, which, isChecked ->
                        if(isChecked) newRecipeViewModel.selectedIngredientsIndexes.add(which)
                        else newRecipeViewModel.selectedIngredientsIndexes.remove(which)
                    }.setPositiveButton("Add Ingredients"){dialog, which ->
                        if(newRecipeViewModel.selectedIngredientsIndexes.isEmpty()){
                            new_recipe_ingredients_text.text = "No ingredients selected!"
                        }else
                            new_recipe_ingredients_text.text = newRecipeViewModel.selectedIngredientsIndexes
                                .map { index -> newRecipeViewModel.ingredients.value!![index] }
                                .map{ ingredient -> ingredient.name}.reduceRight{r, l -> "$l, $r" }
                    }.setNeutralButton("Cancel"){dialog, which -> }
                    .create().show()
                newRecipeViewModel.receivedIngredients = false
            }
        })


    }


    private fun createRecipe() {
        val recipeName = new_recipe_name.text.toString()
        val recipeDescription = new_recipe_description.text.toString()

        if(recipeName.length > 3 && recipeDescription.length > 3
            && newRecipeViewModel.selectedIngredientsIndexes.isNotEmpty()
            && newRecipeViewModel.selectedImage != null){

            create_recipe_progressbar.visibility = View.VISIBLE
            create_background.elevation = 10f
            newRecipeViewModel.createNewRecipe(recipeName, recipeDescription)
            newRecipeViewModel.newRecipe.observe(viewLifecycleOwner, Observer {
                Log.d("PRINT", "Recipe changed!")
                if(it.ingredients == null  || it.ingredients.isEmpty()){
                    create_recipe_progressbar.visibility = View.GONE
                    create_background.elevation = 0f
                    Log.d("PRINT", "Recipe changed! and inside if!")
                    newRecipeViewModel.updateIngredients()
                }
            })

        }
    }

}
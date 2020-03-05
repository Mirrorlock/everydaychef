package com.everydaychef.main.ui.fridge

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.everydaychef.EverydayChefApplication
import com.everydaychef.R
import com.everydaychef.main.helpers.MessageUtility
import com.everydaychef.main.helpers.PopupUtility
import kotlinx.android.synthetic.main.fragment_fridge.*
import javax.inject.Inject

class FridgeFragment : Fragment() {

    @Inject
    lateinit var fridgeViewModel: FridgeViewModel
    private lateinit var popupUtility: PopupUtility
    private lateinit var alertDialogBuilder: AlertDialog.Builder
    @Inject lateinit var messageUtility: MessageUtility


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fridge, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as EverydayChefApplication).appComponent.inject(this)
        popupUtility = PopupUtility(context)
        alertDialogBuilder = AlertDialog.Builder(activity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fridgeViewModel.getFamilyIngredients()
        messageUtility.subscribe(this)
        add_items_button.setOnClickListener {
            addItems()
        }
        fridgeViewModel.familyIngredients.observe(viewLifecycleOwner, Observer {
            if (context != null) {
                if (items_list_view.adapter == null) {
                    ingredients_progress_bar.visibility = View.GONE
                    Log.println(Log.DEBUG, "PRINT", "Setting adapter: $it")
                    items_list_view.adapter = IngredientDataAdapter(context!!, R.layout.row_item,it)
                    { currentItem ->
                        fridgeViewModel.deleteIngredient(fridgeViewModel.currentUser!!.user.family.id,
                            currentItem)
                    }

                } else {
                    Log.println(Log.DEBUG, "PRINT", "Notifying dataset changed with " + it)
                    (items_list_view.adapter as IngredientDataAdapter).notifyDataSetChanged()
                }
            }
        })
    }

    private fun addItems() {
        fridgeViewModel.getAllOtherIngredients()
        fridgeViewModel.allOtherIngredients.observe(viewLifecycleOwner, Observer {
            if(fridgeViewModel.receivedOtherIngredients){
                var items = it.map { ingredient -> ingredient.name }.toTypedArray()
                var checkedItems = ArrayList<Int>()
                AlertDialog.Builder(activity)
                    .setTitle("Add ingredients")
                    .setMultiChoiceItems(items,  null)
                    { dialog, which, isChecked ->
                        if(isChecked) checkedItems.add(which)
                        else checkedItems.remove(which)
                    }.setPositiveButton("Add Ingredients"){ dialog, which ->
                        fridgeViewModel.addItems(checkedItems)
                    }.setNeutralButton("Cancel"){dialog, which -> }
                    .create().show()
                fridgeViewModel.receivedOtherIngredients = false
            }
        })
    }
}
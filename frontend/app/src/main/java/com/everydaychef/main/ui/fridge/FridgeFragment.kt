package com.everydaychef.main.ui.fridge

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.everydaychef.EverydayChefApplication
import com.everydaychef.R
import kotlinx.android.synthetic.main.fragment_fridge.*
import javax.inject.Inject

class FridgeFragment : Fragment() {

    @Inject
    lateinit var fridgeViewModel: FridgeViewModel

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_items_button.setOnClickListener {
            // go to new fragment to choose ingredients or create alert dialog
        }
//        fridgeViewModel.items.observe()
    }
}
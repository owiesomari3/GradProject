package com.example.graduationproject
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.graduationproject.databinding.ButtonsheetlayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ButtonSheetFragment(supportFragmentManager: FragmentManager, s: String) : BottomSheetDialogFragment() {
    lateinit var binding:ButtonsheetlayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.buttonsheetlayout,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      //  binding.Image.setOnClickListener {

     //   }

    }
}
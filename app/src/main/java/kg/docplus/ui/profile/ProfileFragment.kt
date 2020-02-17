package kg.docplus.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kg.docplus.App
import kg.docplus.R
import kg.docplus.databinding.FragmentProfileBinding
import kg.docplus.injection.ViewModelFactory
import kg.docplus.ui.main.MainActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        binding = DataBindingUtil.inflate(
            LayoutInflater.from(container!!.context),
            R.layout.fragment_profile,
            container,
            false
        )


        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(ProfileViewModel::class.java)

        //binding.viewModel = viewModel

        binding.viewModel = viewModel
        viewModel.getProfile()
        viewModel.profile.observe(this, Observer {
            Log.e("PROFILE_GET", it.toString())
            binding.profile = it
        })

        (App.activity as MainActivity).pathPhoto.observe(this, Observer {
           viewModel.postImage(it!!)
        })

        return binding.root
    }


}

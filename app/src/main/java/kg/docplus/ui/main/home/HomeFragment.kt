package kg.docplus.ui.main.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kg.docplus.R
import kg.docplus.databinding.FragmentHomeBinding
import kg.docplus.injection.ViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        binding = DataBindingUtil.inflate(LayoutInflater.from(container!!.context), R.layout.fragment_home, container, false)


        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(HomeViewModel::class.java)

        binding.viewModel = viewModel

        //viewModel.filterDocs()

        return  binding.root
    }


}

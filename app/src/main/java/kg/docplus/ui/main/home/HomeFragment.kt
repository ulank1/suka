package kg.docplus.ui.main.home


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kg.docplus.R
import kg.docplus.databinding.FragmentHomeBinding
import kg.docplus.injection.ViewModelFactory
import kg.docplus.model.get.Notification
import kg.docplus.ui.notification.NotificationActivity
import kg.docplus.utils.extension.gone
import kg.docplus.utils.extension.visible

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

        binding.notif.setOnClickListener { startActivity(Intent(context,NotificationActivity::class.java)) }

        //viewModel.filterDocs()
        viewModel.loadingVisibility.observe(this, Observer {
            if (it!=null){

                if (it>0){
                    binding.notif1.visible()
                }else{
                    binding.notif1.gone()
                }

            }
        })

        return  binding.root
    }


    override fun onResume() {
        super.onResume()

        viewModel.getNotCounts()
    }

}

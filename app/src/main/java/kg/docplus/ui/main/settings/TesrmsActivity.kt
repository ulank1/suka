package kg.docplus.ui.main.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kg.docplus.R
import kg.docplus.injection.ViewModelFactory
import kotlinx.android.synthetic.main.activity_tesrms.*

class TesrmsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tesrms)
        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(SettingsViewModel::class.java)
        viewModel.getTerms()
        viewModel.term.observe(this, Observer {

            text.text = it.toString()

        })
    }
}

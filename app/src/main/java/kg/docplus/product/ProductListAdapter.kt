package kg.docplus.product

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kg.docplus.R
import kg.docplus.databinding.ItemProductBinding
import kg.docplus.model.Product

class ProductListAdapter: RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    private lateinit var postList:ArrayList<Product>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemProductBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_product, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postList[position])
    }

    override fun getItemCount(): Int {
        return if(::postList.isInitialized) postList.size else 0
    }

    fun updatePostList(postList:ArrayList<Product>){
        this.postList = postList
        notifyDataSetChanged()
    }

    fun addList(postList:ArrayList<Product>){
        this.postList.addAll(postList)
        notifyDataSetChanged()
    }


    class ViewHolder(private val binding: ItemProductBinding):RecyclerView.ViewHolder(binding.root){
        private val viewModel = ProductViewModel()

        fun bind(post: Product){
            viewModel.bind(post)
            binding.viewModel = viewModel
        }
    }
}
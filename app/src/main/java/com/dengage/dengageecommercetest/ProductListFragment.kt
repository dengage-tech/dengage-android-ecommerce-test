package com.dengage.dengageecommercetest

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.dengage.dengageecommercetest.data.Product
import androidx.recyclerview.widget.LinearLayoutManager
import com.dengage.dengageecommercetest.data.*
import com.dengage.sdk.Dengage

class ProductListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private var categoryId: Int = -1

    companion object {
        private const val ARG_CATEGORY_ID = "category_id"

        fun newInstance(categoryId: Int): ProductListFragment {
            val fragment = ProductListFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_CATEGORY_ID, categoryId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryId = arguments?.getInt(ARG_CATEGORY_ID) ?: -1

        val category = DataProvider.getCategoryById(categoryId)
        if (category != null) {
            adapter = ProductAdapter(category.products) { product ->
                // Navigate to ProductDetailFragment
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProductDetailFragment.newInstance(product.id))
                    .addToBackStack(null) // so user can press back
                    .commit()
            }
            recyclerView.adapter = adapter
            Dengage.setNavigation(
                activity = activity as AppCompatActivity,
                screenName = category.imageName
            )
            val data = hashMapOf<String, Any>(
                "page_type" to "category",
                "category_id" to category.imageName
            )
            Dengage.pageView(data)
        }

    }
}

class ProductAdapter(
    private val products: List<Product>,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.productImage)
        val nameTextView: TextView = itemView.findViewById(R.id.productName)
        val idTextView: TextView = itemView.findViewById(R.id.productId)
        val priceTextView: TextView = itemView.findViewById(R.id.productPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    @SuppressLint("DefaultLocale", "DiscouragedApi")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        val imageResId = holder.itemView.context.resources.getIdentifier(
            product.imageName, "drawable", holder.itemView.context.packageName)
        holder.imageView.setImageResource(imageResId)
        holder.nameTextView.text = product.name
        holder.idTextView.text = product.imageName
        holder.priceTextView.text = String.format("$%.2f", product.price)
        holder.itemView.setOnClickListener { onItemClick(product) }
    }

    override fun getItemCount(): Int = products.size
}


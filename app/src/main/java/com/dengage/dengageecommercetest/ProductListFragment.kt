package com.dengage.dengageecommercetest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.dengage.dengageecommercetest.data.Product
import androidx.recyclerview.widget.LinearLayoutManager
import com.dengage.dengageecommercetest.data.*

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
        }
    }
}

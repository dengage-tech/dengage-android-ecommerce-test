package com.dengage.dengageecommercetest

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dengage.dengageecommercetest.data.*
import com.dengage.sdk.Dengage
import com.google.android.material.button.MaterialButton

class ProductDetailFragment : Fragment() {

    private var productId: Int = -1

    companion object {
        private const val ARG_PRODUCT_ID = "product_id"

        fun newInstance(productId: Int): ProductDetailFragment {
            val fragment = ProductDetailFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_PRODUCT_ID, productId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    @SuppressLint("DefaultLocale")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productId = arguments?.getInt(ARG_PRODUCT_ID) ?: -1

        val product = DataProvider.getProductById(productId) ?: return
        val productImageView = view.findViewById<ImageView>(R.id.detailProductImage)
        val nameTextView = view.findViewById<TextView>(R.id.detailProductName)
        val priceTextView = view.findViewById<TextView>(R.id.detailProductPrice)
        val quantitySpinner = view.findViewById<Spinner>(R.id.spinnerQuantity)
        val addToCartButton = view.findViewById<MaterialButton>(R.id.btnAddToCart)

        // Populate UI
        val imageResId = resources.getIdentifier(product.imageName, "drawable", requireContext().packageName)
        productImageView.setImageResource(imageResId)
        nameTextView.text = product.name
        priceTextView.text = String.format("$%.2f", product.price)

        val quantities = (1..10).toList()
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, quantities)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        quantitySpinner.adapter = spinnerAdapter

        Dengage.setNavigation(
            activity = activity as AppCompatActivity,
            screenName = "product"
        )
        val data = hashMapOf<String, Any>(
            "page_type" to "product",
            "product_id" to productId
        )
        Dengage.pageView(data)

        addToCartButton.setOnClickListener {
            val selectedQty = quantitySpinner.selectedItem as Int
            CartManager.addProduct(product, selectedQty)

            (activity as? MainActivity)?.updateCartBadge(CartManager.getTotalItemCount())

            val cartData = hashMapOf<String, Any>(
                "product_id" to product.id,
                "product_variant_id" to product.id,
                "quantity" to selectedQty,
                "unit_price" to product.price,
                "discounted_price" to product.price
            )
            Dengage.addToCart(cartData)

            Toast.makeText(requireContext(), "Added to cart", Toast.LENGTH_SHORT).show()
        }

    }
}

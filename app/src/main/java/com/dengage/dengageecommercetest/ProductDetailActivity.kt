package com.dengage.dengageecommercetest

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dengage.dengageecommercetest.data.*

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var productImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var quantitySpinner: Spinner
    private lateinit var addToCartButton: Button
    private var selectedQuantity: Int = 1
    private var product: Product? = null

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        productImageView = findViewById(R.id.detailProductImage)
        nameTextView = findViewById(R.id.detailProductName)
        priceTextView = findViewById(R.id.detailProductPrice)
        quantitySpinner = findViewById(R.id.spinnerQuantity)
        addToCartButton = findViewById(R.id.btnAddToCart)

        val productId = intent.getIntExtra("productId", -1)
        product = DataProvider.getProductById(productId)
        if (product == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        title = product?.name
        val imageResId = resources.getIdentifier(product?.imageName, "drawable", packageName)
        productImageView.setImageResource(imageResId)
        nameTextView.text = product?.name
        priceTextView.text = String.format("$%.2f", product?.price)

        // Setup quantity spinner (1 to 10)
        val quantities = (1..10).toList()
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, quantities)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        quantitySpinner.adapter = spinnerAdapter

        quantitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedQuantity = quantities[position]
            }
        }

        addToCartButton.setOnClickListener {
            product?.let {
                CartManager.addProduct(it, selectedQuantity)
                // Optionally, send an event to Dengage here
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

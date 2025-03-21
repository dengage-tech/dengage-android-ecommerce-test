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
import androidx.recyclerview.widget.RecyclerView
import com.dengage.dengageecommercetest.data.Product
import androidx.recyclerview.widget.LinearLayoutManager
import com.dengage.dengageecommercetest.data.*


class ProductListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private var categoryId: Int = -1
    private lateinit var products: List<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        categoryId = intent.getIntExtra("categoryId", -1)
        val category = DataProvider.getCategoryById(categoryId)
        if (category != null) {
            products = category.products
            adapter = ProductAdapter(products) { product ->
                // Open ProductDetailActivity
                val intent = Intent(this, ProductDetailActivity::class.java)
                intent.putExtra("productId", product.id)
                startActivity(intent)
            }
            recyclerView.adapter = adapter
            title = category.name
        } else {
            Toast.makeText(this, "Category not found", Toast.LENGTH_SHORT).show()
            finish()
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
        val priceTextView: TextView = itemView.findViewById(R.id.productPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        val imageResId = holder.itemView.context.resources.getIdentifier(
            product.imageName, "drawable", holder.itemView.context.packageName)
        holder.imageView.setImageResource(imageResId)
        holder.nameTextView.text = product.name
        holder.priceTextView.text = String.format("$%.2f", product.price)
        holder.itemView.setOnClickListener { onItemClick(product) }
    }

    override fun getItemCount(): Int = products.size
}

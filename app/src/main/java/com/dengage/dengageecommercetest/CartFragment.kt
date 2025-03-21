package com.dengage.dengageecommercetest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dengage.dengageecommercetest.data.*

class CartFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var totalTextView: TextView
    private lateinit var checkoutButton: Button
    private lateinit var adapter: CartAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewCart)
        totalTextView = view.findViewById(R.id.textTotal)
        checkoutButton = view.findViewById(R.id.btnCheckout)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CartAdapter(CartManager.getItems()) { product ->
            // Remove item from cart callback
            CartManager.removeProduct(product)
            updateTotal()
            adapter.updateItems(CartManager.getItems())
        }
        recyclerView.adapter = adapter

        checkoutButton.setOnClickListener {
            startActivity(Intent(context, CheckoutActivity::class.java))
        }

        updateTotal()
        return view
    }

    private fun updateTotal() {
        val total = CartManager.getItems().sumByDouble { it.product.price * it.quantity }
        totalTextView.text = String.format("Total: $%.2f", total)
        checkoutButton.isEnabled = CartManager.getItems().isNotEmpty()
    }
}

class CartAdapter(
    private var items: MutableList<CartItem>,
    private val onRemove: (Product) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.cartItemImage)
        val nameTextView: TextView = itemView.findViewById(R.id.cartItemName)
        val quantityTextView: TextView = itemView.findViewById(R.id.cartItemQuantity)
        val priceTextView: TextView = itemView.findViewById(R.id.cartItemPrice)
        val removeButton: ImageButton = itemView.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = items[position]
        val imageResId = holder.itemView.context.resources.getIdentifier(
            cartItem.product.imageName, "drawable", holder.itemView.context.packageName)
        holder.imageView.setImageResource(imageResId)
        holder.nameTextView.text = cartItem.product.name
        holder.quantityTextView.text = "Quantity: ${cartItem.quantity}"
        holder.priceTextView.text = String.format("$%.2f", cartItem.product.price * cartItem.quantity)
        holder.removeButton.setOnClickListener {
            onRemove(cartItem.product)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: MutableList<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}


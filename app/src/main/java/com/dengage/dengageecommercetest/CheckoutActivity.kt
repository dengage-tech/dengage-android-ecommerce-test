package com.dengage.dengageecommercetest

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView
import com.dengage.dengageecommercetest.data.Product
import androidx.recyclerview.widget.LinearLayoutManager
import com.dengage.dengageecommercetest.data.CartManager

class CheckoutActivity : AppCompatActivity() {
    private lateinit var summaryTextView: TextView
    private lateinit var placeOrderButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        summaryTextView = findViewById(R.id.textSummary)
        placeOrderButton = findViewById(R.id.btnPlaceOrder)

        displaySummary()

        placeOrderButton.setOnClickListener {
            CartManager.clearCart()
            AlertDialog.Builder(this)
                .setTitle("Order Placed")
                .setMessage("Your order has been placed successfully!")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }
                .show()
        }
    }

    private fun displaySummary() {
        val items = CartManager.getItems()
        if (items.isEmpty()) {
            summaryTextView.text = "Your cart is empty!"
            placeOrderButton.isEnabled = false
        } else {
            val summaryBuilder = StringBuilder("Your Items:\n")
            var total = 0.0
            for (item in items) {
                summaryBuilder.append("• ${item.product.name} (x${item.quantity})\n")
                total += item.product.price * item.quantity
            }
            summaryBuilder.append("\nTotal: $%.2f".format(total))
            summaryTextView.text = summaryBuilder.toString()
        }
    }
}

package com.dengage.dengageecommercetest

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.dengage.dengageecommercetest.data.CartManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class CheckoutFragment : Fragment() {

    private lateinit var summaryTextView: TextView
    private lateinit var placeOrderButton: Button

    companion object {
        fun newInstance(): CheckoutFragment {
            return CheckoutFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        summaryTextView = view.findViewById(R.id.textSummary)
        placeOrderButton = view.findViewById(R.id.btnPlaceOrder)

        displaySummary()

        placeOrderButton.setOnClickListener {
            CartManager.clearCart()
            AlertDialog.Builder(requireContext())
                .setTitle("Order Placed")
                .setMessage("Your order has been placed successfully!")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()

                    // 1) Pop all fragments from the back stack
                    parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                    // 2) Update cart badge to 0
                    (activity as? MainActivity)?.updateCartBadge(0)

                    // 3) Optionally switch the bottom nav to Categories tab
                    //(activity as? MainActivity)?.selectCategoryTab()
                }
                .show()
        }
    }

    @SuppressLint("SetTextI18n")
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

    override fun onDestroyView() {
        super.onDestroyView()
        // Eğer hala cart sekmesi aktifse, cart fragment'i göster
        val mainActivity = activity as? MainActivity
        if (mainActivity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.selectedItemId == R.id.nav_cart) {
            mainActivity.showFragment(CartFragment())
        }
    }
}

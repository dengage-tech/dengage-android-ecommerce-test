package com.dengage.dengageecommercetest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.view.Gravity
import androidx.appcompat.app.AlertDialog
import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dengage.dengageecommercetest.data.CartManager


class ProfileFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProfileAdapter
    private var userInfo = mutableListOf<Pair<String, String>>() // Pair(field, value)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewProfile)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Load user info from SharedPreferences
        val prefs = activity?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val username = prefs?.getString("username", "Guest") ?: "Guest"
        userInfo = mutableListOf(
            "Username" to username,
            "Email" to "",
            "Phone" to ""
        )

        adapter = ProfileAdapter(userInfo) { field, value ->
            // Allow editing for Email and Phone (not Username)
            if (field != "Username") {
                showEditDialog(field, value)
            }
        }
        recyclerView.adapter = adapter

        return view
    }

    private fun showEditDialog(field: String, currentValue: String) {
        val builder = context?.let { AlertDialog.Builder(it) }
        if (builder != null) {
            builder.setTitle("Edit $field")
        }
        val editText = EditText(context)
        editText.setText(currentValue)
        if (builder != null) {
            builder.setView(editText)
            builder.setPositiveButton("Save") { dialog, _ ->
                val newValue = editText.text.toString()
                val index = userInfo.indexOfFirst { it.first == field }
                if (index != -1) {
                    userInfo[index] = field to newValue
                    adapter.updateData(userInfo)
                }
                dialog.dismiss()
            }
            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            builder.show()
        }
    }
}

class ProfileAdapter(
    private var items: List<Pair<String, String>>,
    private val onItemClick: (String, String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_INFO = 0
    private val TYPE_LOGOUT = 1

    override fun getItemViewType(position: Int): Int {
        return if (position < items.size) TYPE_INFO else TYPE_LOGOUT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_INFO) {
            val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
            InfoViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
            LogoutViewHolder(view)
        }
    }

    override fun getItemCount(): Int = items.size + 1 // Additional row for logout

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is InfoViewHolder && position < items.size) {
            val (field, value) = items[position]
            holder.title.text = field
            holder.subtitle.text = value
            holder.itemView.setOnClickListener {
                onItemClick(field, value)
            }
        } else if (holder is LogoutViewHolder) {
            holder.title.text = "Logout"
            holder.title.setTextColor(Color.RED)
            holder.title.gravity = Gravity.CENTER
            holder.itemView.setOnClickListener {
                // Clear SharedPreferences and navigate back to LoginActivity
                val context = holder.itemView.context
                val prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                CartManager.clearCart()
                prefs.edit().clear().apply()
                val intent = Intent(context, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            }
        }
    }

    inner class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(android.R.id.text1)
        val subtitle: TextView = itemView.findViewById(android.R.id.text2)
    }

    inner class LogoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(android.R.id.text1)
    }

    fun updateData(newItems: List<Pair<String, String>>) {
        items = newItems
        notifyDataSetChanged()
    }
}

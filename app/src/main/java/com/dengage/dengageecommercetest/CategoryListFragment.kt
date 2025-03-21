package com.dengage.dengageecommercetest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.Intent
import android.os.Bundle
import com.dengage.dengageecommercetest.data.*

class CategoryListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter
    private val categories = DataProvider.getCategories()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_category_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewCategories)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CategoryAdapter(categories) { category ->
            // Open ProductListActivity and pass the selected category ID
            //val intent = Intent(context, ProductListFragment::class.java)
            //intent.putExtra("categoryId", category.id)
            //startActivity(intent)
            onCategorySelected(category)
        }
        recyclerView.adapter = adapter
        return view
    }

    private fun onCategorySelected(category: Category) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ProductListFragment.newInstance(category.id))
            .addToBackStack(null)
            .commit()
    }
}


class CategoryAdapter(
    private val categories: List<Category>,
    private val onItemClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.categoryImage)
        val nameTextView: TextView = itemView.findViewById(R.id.categoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        val imageResId = holder.itemView.context.resources.getIdentifier(
            category.imageName, "drawable", holder.itemView.context.packageName)
        holder.imageView.setImageResource(imageResId)
        holder.nameTextView.text = category.name
        holder.itemView.setOnClickListener { onItemClick(category) }
    }

    override fun getItemCount(): Int = categories.size
}


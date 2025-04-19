package com.dengage.dengageecommercetest.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val imageName: String // Name of drawable resource
)

data class Category(
    val id: Int,
    val name: String,
    val imageName: String,
    val products: List<Product>
)

object DataProvider {
    private val categories: List<Category> = listOf(
        Category(
            id = 1,
            name = "Phones",
            imageName = "category_01",
            products = listOf(
                Product(id = 1, name = "iPhone 16 Pro Max", price = 1199.99, imageName = "product_01"),
                Product(id = 2, name = "Samsung Galaxy A35", price = 399.99, imageName = "product_02"),
                Product(id = 3, name = "Samsung Galaxy S24 Ultra", price = 845.99, imageName = "product_03")
            )
        ),
        Category(
            id = 2,
            name = "Sports",
            imageName = "category_02",
            products = listOf(
                Product(id = 4, name = "Spalding TF-1000", price = 79.99, imageName = "product_04"),
                Product(id = 5, name = "Bushnell Velocity Speed Gun", price = 119.99, imageName = "product_05"),
                Product(id = 6, name = "adidas Women's Sneaker", price = 44.99, imageName = "product_06")
            )
        ),
        Category(
            id = 3,
            name = "Games",
            imageName = "category_03",
            products = listOf(
                Product(id = 7, name = "PlayStation 5 console (slim)", price = 469.99, imageName = "product_07"),
                Product(id = 8, name = "Silent Hill 2 (PS5)", price = 59.60, imageName = "product_08"),
                Product(id = 9, name = "PDP Victrix Controller", price = 155.00, imageName = "product_09")
            )
        ),
        Category(
            id = 4,
            name = "Laptops",
            imageName = "category_04",
            products = listOf(
                Product(id = 10, name = "Lenovo E14 Laptop", price = 729.99, imageName = "product_10"),
                Product(id = 11, name = "2025 MacBook AirLaptop", price = 1579.99, imageName = "product_11"),
                Product(id = 12, name = "UtechSmart Wireless Mouse", price = 47.99, imageName = "product_12")
            )
        )
    )


    fun getCategories(): List<Category> = categories

    fun getCategoryById(id: Int): Category? = categories.find { it.id == id }

    fun getProductById(id: Int): Product? =
        categories.flatMap { it.products }.find { it.id == id }
}

data class CartItem(val product: Product, var quantity: Int)

object CartManager {
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    private val cartItems = mutableListOf<CartItem>()
    private var currentUsername: String? = null

    /** Call this *after* a successful login */
    fun init(context: Context, username: String) {
        sharedPreferences = context.getSharedPreferences("CartPrefs", Context.MODE_PRIVATE)
        currentUsername = username
        loadCart()
    }

    /** Call this when the user logs out */
    fun logout() {
        cartItems.clear()
        currentUsername = null
    }


    fun addProduct(product: Product, quantity: Int) {
        val existing = cartItems.find { it.product.id == product.id }
        if (existing != null) {
            existing.quantity = (existing.quantity + quantity).coerceAtMost(10)
        } else {
            cartItems.add(CartItem(product, quantity))
        }
        saveCart()
    }

    fun removeProduct(product: Product) {
        cartItems.removeAll { it.product.id == product.id }
        saveCart()
    }

    /** *Manual* clear (e.g. “Empty cart” button). This also wipes the saved JSON. */
    fun clearCart() {
        cartItems.clear()
        saveCart()
    }

    fun getItems(): List<CartItem> = cartItems

    fun getTotalItemCount(): Int = cartItems.sumOf { it.quantity }

    // ——— Internal persistence ———

    private fun keyForUser(): String =
        "cart_items_${currentUsername ?: "unknown"}"

    private fun saveCart() {
        currentUsername?.let { user ->
            val json = gson.toJson(cartItems)
            sharedPreferences.edit {
                putString(keyForUser(), json)
            }
        }
    }

    private fun loadCart() {
        cartItems.clear()
        currentUsername?.let {
            val json = sharedPreferences.getString(keyForUser(), null)
            if (!json.isNullOrEmpty()) {
                val type = object : TypeToken<MutableList<CartItem>>() {}.type
                val saved: MutableList<CartItem> = gson.fromJson(json, type)
                cartItems.addAll(saved)
            }
        }
    }
}

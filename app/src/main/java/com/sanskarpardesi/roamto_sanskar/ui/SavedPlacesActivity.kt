package com.sanskarpardesi.roamto_sanskar.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sanskarpardesi.roamto_sanskar.data.PlaceDatabase
import com.sanskarpardesi.roamto_sanskar.databinding.ActivitySavedPlacesBinding
import com.sanskarpardesi.roamto_sanskar.ui.adapter.SavedPlaceAdapter
import kotlinx.coroutines.launch

class SavedPlacesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedPlacesBinding
    private val placeDao by lazy {
        PlaceDatabase.getDatabase(this).placeDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.decorView.setOnApplyWindowInsetsListener { view, insets ->
            view.setPadding(0, insets.systemWindowInsetTop, 0, 0)
            insets
        }

        binding = ActivitySavedPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.apply {
            title = "Saved Places"
            setDisplayHomeAsUpEnabled(true)
        }

        // ✅ Use SavedPlaceAdapter with delete & click functionality
        val adapter = SavedPlaceAdapter(
            onDelete = { place ->
                lifecycleScope.launch {
                    placeDao.delete(place)
                }
            },
            onClick = { place ->
                val intent = Intent(this, PlaceDetailActivity::class.java).apply {
                    putExtra("title", place.title)
                    putExtra("description", place.description)
                    putExtra("date", place.date)
                    putExtra("latitude", place.latitude)
                    putExtra("longitude", place.longitude)
                }
                startActivity(intent)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Observe data from DB
        placeDao.getAllPlaces().observe(this) { list ->
            adapter.submitList(list)
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

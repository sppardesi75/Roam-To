package com.sanskarpardesi.roamto_sanskar.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.snackbar.Snackbar
import com.sanskarpardesi.roamto_sanskar.R
import com.sanskarpardesi.roamto_sanskar.data.Place
import com.sanskarpardesi.roamto_sanskar.data.PlaceDatabase
import com.sanskarpardesi.roamto_sanskar.databinding.ActivityMainBinding
import com.sanskarpardesi.roamto_sanskar.utils.LocationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val placeDao by lazy { PlaceDatabase.getDatabase(this).placeDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val samplePlaces = listOf(
            Place(title = "CN Tower", description = "Iconic tower in Toronto", date = "2023-03-01", latitude = 43.6426, longitude = -79.3871),
            Place(title = "Royal Ontario Museum", description = "Art and natural history museum", date = "2023-03-02", latitude = 43.6677, longitude = -79.3948),
            Place(title = "Ripley's Aquarium", description = "Marine life exhibits", date = "2023-03-03", latitude = 43.6424, longitude = -79.3860),
            Place(title = "High Park", description = "Huge park with trails", date = "2023-03-04", latitude = 43.6465, longitude = -79.4637),
            Place(title = "Distillery District", description = "Historic pedestrian-only area", date = "2023-03-05", latitude = 43.6503, longitude = -79.3596),
            Place(title = "Toronto Zoo", description = "Large zoo with global animals", date = "2023-03-06", latitude = 43.8177, longitude = -79.1859),
            Place(title = "Casa Loma", description = "Gothic Revival castle", date = "2023-03-07", latitude = 43.6780, longitude = -79.4094),
            Place(title = "St. Lawrence Market", description = "Historic food market", date = "2023-03-08", latitude = 43.6490, longitude = -79.3716),
            Place(title = "Toronto Islands", description = "Recreational islands on Lake Ontario", date = "2023-03-09", latitude = 43.6205, longitude = -79.3784),
            Place(title = "BMO Field", description = "Outdoor sports stadium", date = "2023-03-10", latitude = 43.6332, longitude = -79.4185),
            Place(title = "Art Gallery of Ontario", description = "Museum with Canadian art", date = "2023-03-11", latitude = 43.6536, longitude = -79.3925),
            Place(title = "Nathan Phillips Square", description = "Public square in front of City Hall", date = "2023-03-12", latitude = 43.6525, longitude = -79.3839),
            Place(title = "Harbourfront Centre", description = "Cultural hub by the lake", date = "2023-03-13", latitude = 43.6387, longitude = -79.3822),
            Place(title = "Rogers Centre", description = "Sports dome for Blue Jays", date = "2023-03-14", latitude = 43.6414, longitude = -79.3894),
            Place(title = "Toronto Eaton Centre", description = "Large indoor mall", date = "2023-03-15", latitude = 43.6544, longitude = -79.3807),
            Place(title = "Yonge-Dundas Square", description = "Popular downtown public square", date = "2023-03-16", latitude = 43.6561, longitude = -79.3802),
            Place(title = "Allan Gardens", description = "Indoor botanical garden", date = "2023-03-17", latitude = 43.6617, longitude = -79.3762),
            Place(title = "Trinity Bellwoods Park", description = "Trendy downtown park", date = "2023-03-18", latitude = 43.6473, longitude = -79.4103),
            Place(title = "Ontario Science Centre", description = "Science museum for kids", date = "2023-03-19", latitude = 43.7169, longitude = -79.3407),
            Place(title = "Humber Bay Park", description = "Lakeside nature park", date = "2023-03-20", latitude = 43.6242, longitude = -79.4774)
        )

        CoroutineScope(Dispatchers.IO).launch {
            placeDao.insertAll(samplePlaces)
        }


        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.decorView.setOnApplyWindowInsetsListener { view, insets ->
            view.setPadding(0, insets.systemWindowInsetTop, 0, 0)
            insets
        }// Enable edge-to-edge layout
 

// Apply correct icon color for light/dark mode in status bar
        val isDarkMode = (resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES

// Light mode → dark icons | Dark mode → light icons
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = !isDarkMode


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.apply {
            title = "RoamTO"
            setDisplayHomeAsUpEnabled(false)
        }

        // 📅 Date picker
        binding.editDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, day ->
                    binding.editDate.setText(String.format("%04d-%02d-%02d", year, month + 1, day))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // 🚫 Prevent future dates
            datePicker.datePicker.maxDate = System.currentTimeMillis()
            datePicker.show()
        }


        binding.btnAddPlace.setOnClickListener {
            val title = binding.editTitle.text?.toString()?.trim().orEmpty()
            val description = binding.editDescription.text?.toString()?.trim().orEmpty()
            val date = binding.editDate.text?.toString()?.trim().orEmpty()
            val locationName = binding.editLocation.text?.toString()?.trim().orEmpty()

            var isValid = true
            binding.inputLayoutTitle.error = null
            binding.inputLayoutDescription.error = null
            binding.inputLayoutDate.error = null

            if (title.isEmpty()) {
                binding.inputLayoutTitle.error = "Title is required"
                isValid = false
            }
            if (description.isEmpty()) {
                binding.inputLayoutDescription.error = "Description is required"
                isValid = false
            }
            if (date.isEmpty()) {
                binding.inputLayoutDate.error = "Date is required"
                isValid = false
            }
            if (locationName.isEmpty()) {
                Snackbar.make(binding.root, "Please enter location", Snackbar.LENGTH_SHORT).show()
                isValid = false
            }

            if (!isValid) return@setOnClickListener

            val coords = LocationUtils.getCoordinatesFromName(this, locationName)
            if (coords != null) {
                val (lat, lng) = coords
                val place = Place(
                    title = title,
                    description = description,
                    date = date,
                    latitude = lat,
                    longitude = lng
                )


                CoroutineScope(Dispatchers.IO).launch {
                    placeDao.insert(place)
                    runOnUiThread {
                        with(binding) {
                            editTitle.text?.clear()
                            editDescription.text?.clear()
                            editDate.text?.clear()
                            editLocation.text?.clear()
                        }
                        Snackbar.make(binding.root, "Place saved", Snackbar.LENGTH_SHORT).show()
                    }
                }
            } else {
                Snackbar.make(binding.root, "Location not found", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_saved_places -> {
                startActivity(Intent(this, SavedPlacesActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

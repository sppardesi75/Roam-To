package com.sanskarpardesi.roamto_sanskar.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.MaterialToolbar
import com.sanskarpardesi.roamto_sanskar.R
import com.sanskarpardesi.roamto_sanskar.databinding.ActivityPlaceDetailBinding

class PlaceDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityPlaceDetailBinding
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.setOnApplyWindowInsetsListener { view, insets ->
            view.setPadding(0, insets.systemWindowInsetTop, 0, 0)
            insets
        }
        binding = ActivityPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.topAppBarDetail))
        supportActionBar?.title = "Place Details"
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // if you want back button


        title = intent.getStringExtra("title") ?: "Unknown"
        val description = intent.getStringExtra("description") ?: "No description"
        val date = intent.getStringExtra("date") ?: ""
        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)

        binding.tvDetailTitle.text = title
        binding.tvDetailDate.text = date
        binding.tvDetailDescription.text = description

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBarDetail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            finish() // go back
        }


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val location = LatLng(latitude, longitude)
        googleMap.addMarker(
            MarkerOptions()
                .position(location)
                .title(title)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }
}

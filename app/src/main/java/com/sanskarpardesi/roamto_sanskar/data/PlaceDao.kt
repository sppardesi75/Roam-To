package com.sanskarpardesi.roamto_sanskar.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlaceDao {
    @Insert
    suspend fun insert(place: Place)

    @Query("SELECT * FROM places ORDER BY id DESC")
    fun getAllPlaces(): LiveData<List<Place>>


    @Delete
    suspend fun delete(place: Place)
    @Insert
    suspend fun insertAll(places: List<Place>)

}

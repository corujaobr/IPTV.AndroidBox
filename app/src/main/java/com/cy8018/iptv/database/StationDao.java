package com.cy8018.iptv.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StationDao {
    @Query("SELECT * FROM stations")
    List<StationData> getAll();

    @Query("SELECT * FROM stations WHERE id IN (:stationIds)")
    List<StationData> loadAllByIds(int[] stationIds);

    @Query("SELECT * FROM stations WHERE is_favorite = 1")
    List<StationData> loadAllFavorites();

    @Query("SELECT * FROM stations WHERE station_name = :name LIMIT 1")
    StationData findByName(String name);

    @Query("UPDATE stations Set is_favorite = 1 WHERE station_name = :name")
    void addToFavorites(String name);

    @Query("UPDATE stations Set is_favorite = 0 WHERE station_name = :name")
    void removeFromFavorites(String name);

    @Query("UPDATE stations Set last_source = :source WHERE station_name = :name")
    void setLastSource(String name, int source);

    @Insert
    void insert(StationData station);

    @Insert
    void insertAll(StationData... stations);

    @Update
    void update(StationData station);

    @Delete
    void delete(StationData station);
}

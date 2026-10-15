package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RollHistoryDao {
  @Query("SELECT * FROM roll_history ORDER BY timestamp DESC")
  fun getAllHistory(): Flow<List<RollHistoryEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRoll(roll: RollHistoryEntity): Long

  @Query("DELETE FROM roll_history WHERE id = :id")
  suspend fun deleteById(id: Long)

  @Query("DELETE FROM roll_history")
  suspend fun clearAll()

  @Query("SELECT COUNT(*) FROM roll_history")
  suspend fun getCount(): Int
}

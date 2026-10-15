package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roll_history")
data class RollHistoryEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val formula: String,
  val rollsCommaSeparated: String,
  val modifier: Int = 0,
  val total: Int,
  val colorHex: Long,
  val timestamp: Long = System.currentTimeMillis()
)

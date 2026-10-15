package com.example.data

import kotlinx.coroutines.flow.Flow

class DiceRepository(private val dao: RollHistoryDao) {
  val allHistory: Flow<List<RollHistoryEntity>> = dao.getAllHistory()

  suspend fun insert(entity: RollHistoryEntity): Long {
    return dao.insertRoll(entity)
  }

  suspend fun delete(id: Long) {
    dao.deleteById(id)
  }

  suspend fun clear() {
    dao.clearAll()
  }

  suspend fun seedInitialDataIfEmpty() {
    val count = dao.getCount()
    if (count == 0) {
      val now = System.currentTimeMillis()
      // Card 1: 2d20 + 5, 2 mins ago, total 32, primary color
      dao.insertRoll(
        RollHistoryEntity(
          formula = "2d20 + 5",
          rollsCommaSeparated = "15, 12",
          modifier = 5,
          total = 32,
          colorHex = 0xFF451EBB,
          timestamp = now - (2 * 60 * 1000)
        )
      )
      // Card 2: 1d6, 15 mins ago, total 4, teal/secondary color
      dao.insertRoll(
        RollHistoryEntity(
          formula = "1d6",
          rollsCommaSeparated = "4",
          modifier = 0,
          total = 4,
          colorHex = 0xFF00677F,
          timestamp = now - (15 * 60 * 1000)
        )
      )
      // Card 3: 4d8 - 2, 1 hour ago, total 17, primary color
      dao.insertRoll(
        RollHistoryEntity(
          formula = "4d8 - 2",
          rollsCommaSeparated = "8, 3, 7, 1",
          modifier = -2,
          total = 17,
          colorHex = 0xFF451EBB,
          timestamp = now - (60 * 60 * 1000)
        )
      )
      // Card 4: 1d100, yesterday, total 88, brown/tertiary color
      dao.insertRoll(
        RollHistoryEntity(
          formula = "1d100",
          rollsCommaSeparated = "88",
          modifier = 0,
          total = 88,
          colorHex = 0xFF713300,
          timestamp = now - (24 * 60 * 60 * 1000)
        )
      )
    }
  }
}

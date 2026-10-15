package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.example.model.DiceType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

class SoundPlayer {
  private val scope = CoroutineScope(Dispatchers.Default)
  private val sampleRate = 22050

  // Pre-generate unique sound profiles for each dice type for zero-latency, realistic playback
  private val soundBuffers = mutableMapOf<DiceType, ShortArray>()
  private var multiDiceBuffer: ShortArray? = null
  private var placementBuffer: ShortArray? = null

  init {
    scope.launch {
      // Synthesize audio buffers in background on initialization
      for (type in DiceType.entries) {
        soundBuffers[type] = generateAcousticProfile(type)
      }
      multiDiceBuffer = generateMultiDiceCascade()
      placementBuffer = generatePlacementClick()
    }
  }

  fun playDieRollSound(type: DiceType, enabled: Boolean) {
    if (!enabled) return
    scope.launch {
      val buffer = soundBuffers[type] ?: generateAcousticProfile(type)
      playBuffer(buffer)
    }
  }

  fun playRollAllSound(diceTypes: List<DiceType>, enabled: Boolean) {
    if (!enabled) return
    scope.launch {
      if (diceTypes.size == 1) {
        playDieRollSound(diceTypes.first(), enabled)
      } else {
        val buffer = multiDiceBuffer ?: generateMultiDiceCascade()
        playBuffer(buffer)
      }
    }
  }

  fun playPlacementSound(type: DiceType, enabled: Boolean) {
    if (!enabled) return
    scope.launch {
      val buffer = placementBuffer ?: generatePlacementClick()
      playBuffer(buffer)
    }
  }

  // Generates unique physical acoustics depending on the dice geometry
  private fun generateAcousticProfile(type: DiceType): ShortArray {
    val durationMs = when (type) {
      DiceType.D4 -> 240   // Lightweight pyramid: quick tumbling
      DiceType.D6 -> 320   // Classic cube: solid wood/resin bounce
      DiceType.D8 -> 360   // Octahedron: crisp snappy triangular impacts
      DiceType.D10 -> 420  // Decahedron: progressive rolling clicks
      DiceType.D12 -> 460  // Dodecahedron: smooth facet transitions
      DiceType.D20 -> 520  // Heavy icosahedron: deep resonant rolling rumble
      DiceType.D100 -> 580 // Zocchihedron sphere: extended rolling clatter
    }

    val baseFreq = when (type) {
      DiceType.D4 -> 920.0   // High pitch, sharp geometric clicks
      DiceType.D6 -> 580.0   // Classic hollow cube resonance
      DiceType.D8 -> 720.0   // Crisp diamond facet frequency
      DiceType.D10 -> 540.0  // Medium rolling facet frequency
      DiceType.D12 -> 480.0  // Warm tumbling tone
      DiceType.D20 -> 360.0  // Deep, weighty RPG die tone
      DiceType.D100 -> 640.0 // Granular spherical rattle
    }

    val numClicks = when (type) {
      DiceType.D4 -> 4
      DiceType.D6 -> 7
      DiceType.D8 -> 9
      DiceType.D10 -> 12
      DiceType.D12 -> 15
      DiceType.D20 -> 19
      DiceType.D100 -> 26
    }

    val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
    val buffer = ShortArray(numSamples)

    // Calculate striking points with realistic physics deceleration
    val clickIndices = mutableListOf<Int>()
    for (i in 0 until numClicks) {
      val progress = i.toDouble() / numClicks
      // Deceleration curve: strikes start rapid and space out as kinetic energy dissipates
      val nonLinearProgress = progress * progress * 0.4 + progress * 0.6
      val index = (nonLinearProgress * (numSamples - 600)).toInt().coerceIn(0, numSamples - 600)
      clickIndices.add(index)
    }

    for ((clickNum, clickIndex) in clickIndices.withIndex()) {
      val clickProgress = clickNum.toDouble() / numClicks
      val clickLength = (220 + (clickProgress * 150)).toInt() // settling impacts last longer
      // Slight pitch variance per facet strike
      val freq = baseFreq * (0.92 + Random.nextDouble(0.18))
      var phase = 0.0

      for (i in 0 until clickLength) {
        val idx = clickIndex + i
        if (idx < numSamples) {
          // Sharp attack, exponential decay for realistic percussive impact
          val decay = exp(-i.toDouble() / (clickLength * 0.28))
          val sampleVal = sin(phase) * 19000.0 * decay
          // Add subtle second harmonic for material richness
          val overtone = sin(phase * 2.14) * 8000.0 * decay
          val combined = (buffer[idx] + (sampleVal + overtone)).toInt()
          buffer[idx] = combined.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
          phase += 2.0 * PI * freq / sampleRate
        }
      }
    }

    // Weighty settling thud when the die comes to rest
    val thudStart = (numSamples * 0.82).toInt()
    val thudLength = numSamples - thudStart
    val thudFreq = baseFreq * 0.45
    var thudPhase = 0.0
    for (i in 0 until thudLength) {
      val idx = thudStart + i
      if (idx < numSamples) {
        val envelope = (1.0 - (i.toDouble() / thudLength))
        val sample = (sin(thudPhase) * 24000.0 * envelope * envelope).toInt()
        val combined = buffer[idx] + sample
        buffer[idx] = combined.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        thudPhase += 2.0 * PI * thudFreq / sampleRate
      }
    }

    return buffer
  }

  // Generates composite multi-dice cascade for "ROLL ALL"
  private fun generateMultiDiceCascade(): ShortArray {
    val durationMs = 550
    val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
    val buffer = ShortArray(numSamples)

    // Layer multiple dice clattering simultaneously
    val frequencies = listOf(380.0, 520.0, 680.0, 850.0)
    for (f in frequencies) {
      val numClicks = 8 + Random.nextInt(6)
      for (c in 0 until numClicks) {
        val clickIndex = Random.nextInt(numSamples - 800)
        val clickLength = 260 + Random.nextInt(180)
        var phase = 0.0
        val freq = f * (0.95 + Random.nextDouble(0.1))

        for (i in 0 until clickLength) {
          val idx = clickIndex + i
          if (idx < numSamples) {
            val decay = exp(-i.toDouble() / (clickLength * 0.25))
            val sample = (sin(phase) * 9500.0 * decay).toInt()
            val combined = buffer[idx] + sample
            buffer[idx] = combined.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            phase += 2.0 * PI * freq / sampleRate
          }
        }
      }
    }

    // Heavy final landing thuds
    val thudStart = (numSamples * 0.80).toInt()
    val thudLength = numSamples - thudStart
    var thudPhase = 0.0
    for (i in 0 until thudLength) {
      val idx = thudStart + i
      if (idx < numSamples) {
        val envelope = (1.0 - (i.toDouble() / thudLength))
        val sample = (sin(thudPhase) * 21000.0 * envelope * envelope).toInt()
        val combined = buffer[idx] + sample
        buffer[idx] = combined.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        thudPhase += 2.0 * PI * 160.0 / sampleRate
      }
    }

    return buffer
  }

  // Generates crisp short placement click when adding a die
  private fun generatePlacementClick(): ShortArray {
    val durationMs = 90
    val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
    val buffer = ShortArray(numSamples)
    var phase = 0.0
    val freq = 750.0

    for (i in 0 until numSamples) {
      val envelope = exp(-i.toDouble() / (numSamples * 0.2))
      val sample = (sin(phase) * 16000.0 * envelope).toInt()
      buffer[i] = sample.toShort()
      phase += 2.0 * PI * freq / sampleRate
    }
    return buffer
  }

  private fun playBuffer(buffer: ShortArray) {
    try {
      val track = AudioTrack.Builder()
        .setAudioAttributes(
          AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        )
        .setAudioFormat(
          AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(sampleRate)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()
        )
        .setBufferSizeInBytes(buffer.size * 2)
        .setTransferMode(AudioTrack.MODE_STATIC)
        .build()

      track.write(buffer, 0, buffer.size)
      track.play()

      val playDurationMs = ((buffer.size.toDouble() / sampleRate) * 1000).toLong() + 40
      Thread.sleep(playDurationMs)
      track.stop()
      track.release()
    } catch (_: Exception) {
      // Gracefully handled if audio device is unavailable
    }
  }
}

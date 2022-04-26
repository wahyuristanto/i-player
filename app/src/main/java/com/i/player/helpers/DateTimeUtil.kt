package com.i.player.helpers

object DateTimeUtil {

    /**
     * Format Duration
     * e.g. 01:30 for 90 seconds
     *
     * @return String formatted duration as XX:XX
     */
    fun getFormattedDuration(duration: Int): String {
        val minutes = (duration / 1000) / 60
        val seconds = ((duration / 1000) % 60)
        return String.format("%02d:%02d", minutes, seconds)
    }
}
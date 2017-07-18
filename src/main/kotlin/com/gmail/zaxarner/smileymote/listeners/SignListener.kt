package com.gmail.zaxarner.smileymote.listeners

import com.gmail.zaxarner.smileymote.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent

/**
 * Created on 7/16/2017.
 */
object SignListener : Listener {

    @EventHandler
    fun onSignPlace(event: SignChangeEvent) {
        val player = event.player
        if(!player.hasPermission("smileymote.user")) return


        val smileySection = plugin.config.getConfigurationSection("smileys")

        for(i in 0..4) {
            val line = event.lines[i]

            for(s in smileySection.getKeys(false)) {
                val input = smileySection.getString(s + ".input") ?: continue
                if(line.contains(input, false)) {
                    val output = smileySection.getString(s + ".output") ?: continue

                    event.setLine(i, line.replace(input, output, false))
                }
            }
        }
    }
}
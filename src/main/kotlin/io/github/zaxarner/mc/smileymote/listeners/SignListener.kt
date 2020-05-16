package io.github.zaxarner.mc.smileymote.listeners

import io.github.zaxarner.mc.smileymote.plugin
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
        if(!player.hasPermission("smileymote.smiley")) return


        val smileySection = plugin.config.getConfigurationSection("smileys") ?: return

        for(i in event.lines.indices) {
            val line = event.lines[i] ?: continue

            for (s in smileySection.getKeys(false)) {
                val input = smileySection.getString("$s.input") ?: continue
                if (line.contains(input, false)) {
                    val output = smileySection.getString("$s.output") ?: continue

                    event.setLine(i, line.replace(input, output, false))
                }
            }
        }
    }
}
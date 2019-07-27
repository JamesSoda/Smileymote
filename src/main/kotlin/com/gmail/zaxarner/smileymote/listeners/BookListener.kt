package com.gmail.zaxarner.smileymote.listeners

import com.gmail.zaxarner.smileymote.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEditBookEvent

/**
 * Created on 7/16/2017.
 */
object BookListener : Listener {


    @EventHandler
    fun onBookWrite(event: PlayerEditBookEvent) {
        val player = event.player
        if(!player.hasPermission("smileymote.user")) return

        val meta = event.newBookMeta


        for(i in 1..meta.pageCount) {
            var page = meta.getPage(i)

            val smileySection = plugin.config.getConfigurationSection("smileys") ?: return

            for(s in smileySection.getKeys(false)) {
                val input = smileySection.getString(s + ".input") ?: continue
                val output = smileySection.getString(s + ".output") ?: continue

                page = page.replace(input, output, false)

            }

            meta.setPage(i, page)
        }
        event.newBookMeta = meta
    }
}
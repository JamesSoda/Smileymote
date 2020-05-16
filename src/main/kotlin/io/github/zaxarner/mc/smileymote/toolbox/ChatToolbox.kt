package io.github.zaxarner.mc.smileymote.toolbox

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import java.util.logging.Level

/**
 * Created on 7/15/2017.
 */

fun consoleLog(msg: String, lvl: Level = Level.INFO) {
    var chatColor = ChatColor.WHITE
    when(lvl) {
        Level.WARNING -> chatColor = ChatColor.RED
        Level.SEVERE -> chatColor = ChatColor.DARK_RED
    }
    Bukkit.getServer().consoleSender.sendMessage("[Smileymote] $chatColor$msg")
}

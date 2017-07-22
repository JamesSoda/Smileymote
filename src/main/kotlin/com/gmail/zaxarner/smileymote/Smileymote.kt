package com.gmail.zaxarner.smileymote

import com.gmail.zaxarner.smileymote.extensions.makePretty
import com.gmail.zaxarner.smileymote.listeners.BookListener
import com.gmail.zaxarner.smileymote.listeners.ChatListener
import com.gmail.zaxarner.smileymote.listeners.MenuListener
import com.gmail.zaxarner.smileymote.listeners.SignListener
import com.gmail.zaxarner.smileymote.menu.Menu
import com.gmail.zaxarner.smileymote.menu.MenuItem
import com.gmail.zaxarner.smileymote.menu.PlayerSelectMenu
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created on 7/15/2017.
 */
class Smileymote : JavaPlugin() {

    val emoteMenu = Menu("${ChatColor.DARK_AQUA}Emotes ${ChatColor.GRAY}- ${ChatColor.BLUE}Emote Type", 27)

    val selfEmoteMenu = Menu("${ChatColor.DARK_AQUA}Self ${ChatColor.GRAY}- ${ChatColor.BLUE}Select Emote", 54)
    val otherEmoteMenu = Menu("${ChatColor.DARK_AQUA}Other-Player ${ChatColor.GRAY}- ${ChatColor.BLUE}Select Emote", 54)

    val playerSelectMenu = PlayerSelectMenu

    val selfEmotes = mutableMapOf<String, String>()
    val otherEmotes = mutableMapOf<String, String>()

    override fun onEnable() {
        this.saveDefaultConfig()

        this.server.pluginManager.registerEvents(BookListener, this)
        this.server.pluginManager.registerEvents(ChatListener, this)
        this.server.pluginManager.registerEvents(MenuListener, this)
        this.server.pluginManager.registerEvents(SignListener, this)

        val emoteCommand = getCommand("emote")
        emoteCommand.executor = EmoteCommand()
        emoteCommand.usage = "${ChatColor.GRAY}[${ChatColor.GOLD}Emote${ChatColor.GRAY}] ${ChatColor.RESET}${emoteCommand.usage}"

        val smileysCommand = getCommand("smileys")
        smileysCommand.executor = SmileysCommand()
        smileysCommand.usage = "${ChatColor.GRAY}[${ChatColor.GOLD}Smileys${ChatColor.GRAY}] ${ChatColor.RESET}${smileysCommand.usage}"

        val selfEmoteSection = plugin.config.getConfigurationSection("emotes.self")
        val otherEmoteSection = plugin.config.getConfigurationSection("emotes.other")

        for(emote in selfEmoteSection.getKeys(false)) {
            selfEmotes.put(emote, selfEmoteSection.getString("$emote.message"))
        }

        for(emote in otherEmoteSection.getKeys(false)) {
            otherEmotes.put(emote, otherEmoteSection.getString("$emote.message"))
        }

        emoteMenu.addMenuItem(12, object : MenuItem("${ChatColor.BLUE}Self Emotes", Material.SKULL_ITEM, 3, "Display your emotions to everyone!") {
            override fun onClick(player: Player) {
                player.openInventory(selfEmoteMenu.inventory)
            }
        })
        emoteMenu.addMenuItem(14, object : MenuItem("${ChatColor.BLUE}Other-Player Emotes", Material.SKULL_ITEM, 3, "Display your emotions about another player to everyone!") {
            override fun onClick(player: Player) {
                player.openInventory(otherEmoteMenu.inventory)
            }
        })

        val selfEmoteNames = selfEmotes.keys.toList()

        for(i in 0..selfEmoteNames.size - 1) {
            val name = selfEmoteNames[i]

            var description = "A simple emote"
            if(selfEmoteSection["$name.description"] != null) {
                description = selfEmoteSection.getString("$name.description")
            }

            var material = Material.STICK
            var data = 0
            if(selfEmoteSection["$name.material"] != null) {
                material = Material.getMaterial(selfEmoteSection.getString("$name.material"))

                if(selfEmoteSection["$name.data"] != null) {
                    data = selfEmoteSection.getInt("$name.data")
                }
            }

            selfEmoteMenu.addMenuItem(i, object : MenuItem("${ChatColor.BLUE}${name.makePretty()}", material, data.toShort(), description) {
                override fun onClick(player: Player) {
                    player.closeInventory()
                    Bukkit.dispatchCommand(player, "emote $name")
                }
            })
        }

        val otherEmoteNames = otherEmotes.keys.toList()

        for(i in 0..otherEmoteNames.size - 1) {
            val name = otherEmoteNames[i]

            var description = "A simple emote"
            if(otherEmoteSection["$name.description"] != null) {
                description = otherEmoteSection.getString("$name.description")
            }

            var material = Material.STICK
            var data = 0
            if(otherEmoteSection["$name.material"] != null) {
                material = Material.getMaterial(otherEmoteSection.getString("$name.material"))

                if(otherEmoteSection["$name.data"] != null) {
                    data = otherEmoteSection.getInt("$name.data")
                }
            }

            otherEmoteMenu.addMenuItem(i, object : MenuItem("${ChatColor.BLUE}${name.makePretty()}", material, data.toShort(), description) {
                override fun onClick(player: Player) {
                    playerSelectForEmote(player, name)
                }
            })
        }

        playerSelectMenu.updatePlayerSelectMenu()
    }

    fun playerSelectForEmote(player: Player, message: String?) {
        if(message == null) {
            player.closeInventory()
            return
        }

        playerSelectMenu.playersSelecting.put(player, message)
        playerSelectMenu.openInventory(player)
    }
}

val plugin : Smileymote by lazy {
    Bukkit.getServer().pluginManager.getPlugin("Smileymote") as Smileymote
}

val menus = mutableListOf<Menu>()
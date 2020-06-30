package io.github.zaxarner.mc.smileymote

import co.aikar.commands.BukkitCommandManager
import io.github.zaxarner.mc.smileymote.extensions.makePretty
import io.github.zaxarner.mc.smileymote.listeners.BookListener
import io.github.zaxarner.mc.smileymote.listeners.ChatListener
import io.github.zaxarner.mc.smileymote.listeners.MenuListener
import io.github.zaxarner.mc.smileymote.listeners.SignListener
import io.github.zaxarner.mc.smileymote.menu.Menu
import io.github.zaxarner.mc.smileymote.menu.MenuItem
import io.github.zaxarner.mc.smileymote.menu.PlayerSelectMenu
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

    private lateinit var commandManager: BukkitCommandManager

    override fun onEnable() {
        this.saveDefaultConfig()

        this.server.pluginManager.registerEvents(BookListener, this)
        this.server.pluginManager.registerEvents(ChatListener, this)
        this.server.pluginManager.registerEvents(MenuListener, this)
        this.server.pluginManager.registerEvents(SignListener, this)

        initialiseEmotes()
        registerCommands()

    }

    override fun onDisable() {
        commandManager.unregisterCommands()
    }

    fun reload() {
        this.reloadConfig()

        selfEmotes.clear()
        otherEmotes.clear()
        initialiseEmotes()

        commandManager.unregisterCommands()
        registerCommands()
    }

    fun initialiseEmotes() {
        val selfEmoteSection = plugin.config.getConfigurationSection("emotes.self") ?: return
        val otherEmoteSection = plugin.config.getConfigurationSection("emotes.other") ?: return

        for(emote in selfEmoteSection.getKeys(false)) {
            selfEmotes[emote] = selfEmoteSection.getString("$emote.message")!!
        }

        for(emote in otherEmoteSection.getKeys(false)) {
            otherEmotes[emote] = otherEmoteSection.getString("$emote.message")!!
        }

        emoteMenu.addMenuItem(12, object : MenuItem("${ChatColor.BLUE}Self Emotes",
                Material.PLAYER_HEAD, 3, "Display your emotions to everyone!") {
            override fun onClick(player: Player) {
                player.openInventory(selfEmoteMenu.inventory)
            }
        })
        emoteMenu.addMenuItem(14, object : MenuItem("${ChatColor.BLUE}Other-Player Emotes",
                Material.PLAYER_HEAD, 3, "Display your emotions about another player to everyone!") {
            override fun onClick(player: Player) {
                player.openInventory(otherEmoteMenu.inventory)
            }
        })

        val selfEmoteNames = selfEmotes.keys.toList()

        for(i in 0..selfEmoteNames.size - 1) {
            val name = selfEmoteNames[i]

            var description = "A simple emote"
            if(selfEmoteSection["$name.description"] != null) {
                description = selfEmoteSection.getString("$name.description")!!
            }

            var material = Material.STICK
            var data = 0
            if(selfEmoteSection["$name.material"] != null) {
                material = Material.getMaterial(selfEmoteSection.getString("$name.material")!!)!!

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
                description = otherEmoteSection.getString("$name.description")!!
            }

            var material = Material.STICK
            var data = 0
            if(otherEmoteSection["$name.material"] != null) {
                material = Material.getMaterial(otherEmoteSection.getString("$name.material")!!)!!

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
    }

    fun registerCommands() {
        commandManager = BukkitCommandManager(this)

        commandManager.registerCommand(SmileymoteCommand(this))
        commandManager.registerCommand(EmoteCommand(this))
        commandManager.registerCommand(SmileysCommand(this))

        val emoteNames = mutableListOf<String>()
        emoteNames.addAll(selfEmotes.keys)
        emoteNames.addAll(otherEmotes.keys)

        commandManager.commandCompletions.registerCompletion("emotes") { emoteNames }
    }

    fun playerSelectForEmote(player: Player, message: String?) {
        if(message == null) {
            player.closeInventory()
            return
        }

        playerSelectMenu.playersSelecting[player] = message
        playerSelectMenu.openInventory(player)
    }

    fun getPrefix() : String {
        if(config["prefix"] != null) {
            return ChatColor.translateAlternateColorCodes('&', config.getString("prefix")!!)
        }

        return ChatColor.translateAlternateColorCodes('&', "&7[&6Smileymote&7] ")
    }
}

val plugin : Smileymote by lazy {
    Bukkit.getServer().pluginManager.getPlugin("Smileymote") as Smileymote
}

val menus = mutableListOf<Menu>()
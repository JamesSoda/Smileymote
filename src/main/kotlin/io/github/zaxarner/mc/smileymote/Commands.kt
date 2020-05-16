package io.github.zaxarner.mc.smileymote

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import co.aikar.commands.bukkit.contexts.OnlinePlayer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Created on 7/15/2017.
 */
@CommandPermission("smileymote.admin")
@CommandAlias("smileymote|smote|smile")
class SmileymoteCommand(private val plugin: Smileymote) : BaseCommand() {

    @Default
    @HelpCommand
    @CatchUnknown
    fun onDefault(sender: CommandSender) {
        sender.sendMessage("${plugin.getPrefix()} ${ChatColor.GREEN}/smileymote version")
        sender.sendMessage("${plugin.getPrefix()} ${ChatColor.GREEN}/smileymote reload")
        sender.sendMessage("${plugin.getPrefix()} ${ChatColor.GREEN}/emote")
        sender.sendMessage("${plugin.getPrefix()} ${ChatColor.GREEN}/smileys")
    }

    @Subcommand("version")
    fun onVersion(sender: CommandSender) {
        sender.sendMessage("${plugin.getPrefix()} ${ChatColor.GREEN}Plugin version: ${plugin.description.version}")
    }

    @Subcommand("reload")
    fun onReload(sender: CommandSender) {
        plugin.reload()
        sender.sendMessage("${plugin.getPrefix()} ${ChatColor.GREEN}Config successfully reloaded.")
    }
}

@CommandPermission("smileymote.emote")
@CommandAlias("emotes|emote|em")
class EmoteCommand(private val plugin: Smileymote) : BaseCommand() {

    @Default
    fun onEmote(sender: CommandSender) {
        if (sender is Player) {
            sender.openInventory(plugin.emoteMenu.inventory)
        } else {
            sender.sendMessage("${plugin.getPrefix()} ${ChatColor.RED}You must be a player to use the Emote Menu.")
        }
    }

    @HelpCommand
    @CatchUnknown
    fun onHelp(sender: CommandSender) {
        sender.sendMessage("${plugin.getPrefix()} ${ChatColor.GREEN}/emote <emote>")
        sender.sendMessage("${plugin.getPrefix()} ${ChatColor.GREEN}/emote <emote> <player>")
    }

    @Default
    @CommandCompletion("@emotes @players")
    fun onEmote(sender: CommandSender, emoteName: String, @Optional player: OnlinePlayer?) {
        if (plugin.otherEmotes.keys.contains(emoteName)) {
            if (player != null) {
                if (sender is Player) {
                    if (sender == player.player && !plugin.selfEmotes.keys.contains(emoteName)) {
                        sender.sendMessage("${plugin.getPrefix()} ${ChatColor.RED}You cannot use that emote on yourself!")
                        return
                    }
                }
                val message = plugin.otherEmotes[emoteName] ?: return

                Bukkit.broadcastMessage("${plugin.getPrefix()} ${message.replace("{player1}", sender.name)
                        .replace("{player}", sender.name)
                        .replace("{player2}", player.player.name)}")
                return
            } else {
                if (!plugin.selfEmotes.keys.contains(emoteName)) {
                    return
                }
            }
        }

        if (plugin.selfEmotes.keys.contains(emoteName)) {
            val message = plugin.selfEmotes[emoteName] ?: return

            Bukkit.broadcastMessage("${plugin.getPrefix()} ${message.replace("{player}", sender.name)}")
            return
        }

        sender.sendMessage("${plugin.getPrefix()} ${ChatColor.RED}That is not a valid emote!")

    }

    @Subcommand("list")
    fun viewList(sender: CommandSender) {
        val selfEmoteIter = plugin.selfEmotes.iterator()
        var selfMessage = ""
        while (selfEmoteIter.hasNext()) {
            val emote = selfEmoteIter.next()
            if (selfEmoteIter.hasNext()) {
                selfMessage += emote.key + ", "
            } else {
                selfMessage += emote.key
            }
        }

        selfMessage.substring(0, selfMessage.length - 1)
        sender.sendMessage("${plugin.getPrefix()} List of self emotes: ${ChatColor.RESET}$selfMessage.")

        val otherEmoteIter = plugin.otherEmotes.iterator()

        var otherMessage = ""
        while (otherEmoteIter.hasNext()) {
            val emote = otherEmoteIter.next()
            if (otherEmoteIter.hasNext()) {
                otherMessage += emote.key + ", "
            } else {
                otherMessage += emote.key
            }
        }

        otherMessage.substring(0, otherMessage.length - 1)
        sender.sendMessage("${plugin.getPrefix()} List of other-player emotes: ${ChatColor.RESET}$otherMessage.")
    }
}


@CommandPermission("smileymote.smiley")
@CommandAlias("smileys|smiley")
class SmileysCommand(private val plugin: Smileymote) : BaseCommand() {

    @Default
    @HelpCommand
    @CatchUnknown
    fun listSmileys(sender: CommandSender) {
        val smileySection = plugin.config.getConfigurationSection("smileys") ?: return

        var listMessage = ""

        val smileysIter = smileySection.getKeys(false).iterator()

        while (smileysIter.hasNext()) {
            val smiley = smileysIter.next()
            val input = smileySection["$smiley.input"]
            val output = smileySection["$smiley.output"]
            if (smileysIter.hasNext()) {
                listMessage += " [ $input = $output ] , "
            } else {
                listMessage += " [ $input = $output ] "
            }
        }

        listMessage.substring(0, listMessage.length - 1)
        sender.sendMessage("${plugin.getPrefix()} List of Smileys: ${ChatColor.RESET}$listMessage.")
    }
}
package com.gmail.zaxarner.smileymote

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Created on 7/15/2017.
 */
class EmoteCommand : CommandExecutor {


    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if(sender !is Player) {
            sender.sendMessage("You must be a player!")
            return true
        }

        if(args.isEmpty()) {
            sender.openInventory(plugin.emoteMenu.inventory)
            return true
        }


        val selfEmoteIter = plugin.selfEmotes.iterator()


        if(args[0].toLowerCase() == "list") {


            var selfMessage = ""
            while(selfEmoteIter.hasNext()) {
                val emote = selfEmoteIter.next()
                if(selfEmoteIter.hasNext()) {
                    selfMessage += emote.key + ", "
                } else {
                    selfMessage += emote.key
                }
            }

            selfMessage.substring(0, selfMessage.length - 1)
            sender.sendMessage("${plugin.getPrefix()} List of self emotes: ${ChatColor.RESET}$selfMessage.")


            val otherEmoteIter = plugin.otherEmotes.iterator()

            var otherMessage = ""
            while(otherEmoteIter.hasNext()) {
                val emote = otherEmoteIter.next()
                if(otherEmoteIter.hasNext()) {
                    otherMessage += emote.key + ", "
                } else {
                    otherMessage += emote.key
                }
            }

            otherMessage.substring(0, otherMessage.length - 1)
            sender.sendMessage("${plugin.getPrefix()} List of other-player emotes: ${ChatColor.RESET}$otherMessage.")

            return true
        }

        if(args.size == 1) {

            if(plugin.selfEmotes.keys.contains(args[0])) {
                val message = plugin.selfEmotes[args[0]] ?: return false

                Bukkit.broadcastMessage("${plugin.getPrefix()} ${message.replace("{player}", sender.name)}")
                return true
            } else if(plugin.otherEmotes.keys.contains(args[0])) {
                sender.sendMessage("${ChatColor.RED}That is not a valid player!")
                return true
            }
        }

        else if(args.size == 2) {

            if(Bukkit.getPlayer(args[1]) == null) {
                sender.sendMessage("${ChatColor.RED}That is not a valid player!")
                return true
            }
            val target = Bukkit.getPlayer(args[1])


            if(plugin.otherEmotes.keys.contains(args[0])) {
                val message = plugin.otherEmotes[args[0]] ?: return false

                Bukkit.broadcastMessage("${plugin.getPrefix()} ${message.replace("{player1}", sender.name).replace("{player}", sender.name)
                        .replace("{player2}", target.name)}")
                return true
            }
        }

        return false
    }
}

class SmileysCommand : CommandExecutor {


    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if(sender !is Player) {
            sender.sendMessage("You must be a player!")
            return true
        }

        val smileySection = plugin.config.getConfigurationSection("smileys")

        var listMessage = ""

        val smileysIter = smileySection.getKeys(false).iterator()

        while(smileysIter.hasNext()) {
            val smiley = smileysIter.next()
            val input = smileySection["$smiley.input"]
            val output = smileySection["$smiley.output"]
            if(smileysIter.hasNext()) {
                listMessage += "($input = $output), "
            } else {
                listMessage += "($input = $output)"
            }
        }

        listMessage.substring(0, listMessage.length - 1)
        sender.sendMessage("${plugin.getPrefix()} List of Smileys: ${ChatColor.RESET}$listMessage.")

        return true
    }
}

class ReloadCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {

        try {
            plugin.reloadConfig()
            sender.sendMessage("${plugin.getPrefix()} ${ChatColor.GREEN}Config successfully reloaded.")
        } catch (e: Exception) {
            sender.sendMessage("${plugin.getPrefix()} ${ChatColor.RED}Config could not be reloaded.")
        }

        return true
    }
}
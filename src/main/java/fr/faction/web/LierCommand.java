package fr.faction.web;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /lier — génère un code à 6 chiffres pour lier son compte Minecraft
 * à un compte web sur le site HeroCraft.
 *
 * Usage :
 *   /lier          → affiche le code + instructions
 *   /lier statut   → indique si le compte est déjà lié
 */
public class LierCommand implements CommandExecutor {

    private final WebLinkManager linkManager;
    private final String siteUrl;

    public LierCommand(WebLinkManager linkManager, String siteUrl) {
        this.linkManager = linkManager;
        this.siteUrl     = siteUrl.replaceAll("/$", "");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Cette commande est réservée aux joueurs.");
            return true;
        }

        if (!linkManager.isEnabled()) {
            player.sendMessage(ChatColor.RED + "❌ Le système de liaison n'est pas configuré "
                    + "(MySQL absent dans config.yml).");
            return true;
        }

        // /lier statut
        if (args.length >= 1 && args[0].equalsIgnoreCase("statut")) {
            String webPseudo = linkManager.getLinkedWebPseudo(player.getUniqueId());
            if (webPseudo != null) {
                player.sendMessage("");
                player.sendMessage(ChatColor.GREEN + "✔ Ton compte Minecraft est lié au compte site "
                        + ChatColor.YELLOW + webPseudo + ChatColor.GREEN + ".");
                player.sendMessage(ChatColor.GRAY + "  Site : §b" + siteUrl + "/faction.html");
                player.sendMessage("");
            } else {
                player.sendMessage(ChatColor.YELLOW + "⚠ Ton compte Minecraft n'est pas encore lié. "
                        + "Tape §e/lier §epour obtenir un code.");
            }
            return true;
        }

        // /lier → générer le code
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "⬡ " + ChatColor.YELLOW + ChatColor.BOLD
                + "Liaison compte site web" + ChatColor.RESET);
        player.sendMessage(ChatColor.GRAY + "Génération du code en cours…");

        // Async pour ne pas bloquer le thread principal
        org.bukkit.Bukkit.getScheduler().runTaskAsynchronously(
            org.bukkit.Bukkit.getPluginManager().getPlugin("FactionPlugin"),
            () -> {
                String code = linkManager.generateCode(player.getUniqueId(), player.getName());
                org.bukkit.Bukkit.getScheduler().runTask(
                    org.bukkit.Bukkit.getPluginManager().getPlugin("FactionPlugin"),
                    () -> {
                        if (code == null) {
                            player.sendMessage(ChatColor.RED + "❌ Impossible de générer un code "
                                    + "(connexion MySQL perdue — contacte un admin).");
                            return;
                        }

                        player.sendMessage("");
                        player.sendMessage(ChatColor.GREEN + "✔ Code généré :");
                        player.sendMessage("");

                        // Afficher le code en grand, cliquable
                        net.md_5.bungee.api.chat.TextComponent msg =
                            new net.md_5.bungee.api.chat.TextComponent(
                                "   " + ChatColor.YELLOW + ChatColor.BOLD + code
                            );
                        msg.setHoverEvent(new net.md_5.bungee.api.chat.HoverEvent(
                            net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                            new net.md_5.bungee.api.chat.ComponentBuilder(
                                "Clic pour copier le code").color(net.md_5.bungee.api.ChatColor.GRAY).create()
                        ));
                        msg.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                            net.md_5.bungee.api.chat.ClickEvent.Action.COPY_TO_CLIPBOARD, code
                        ));
                        player.spigot().sendMessage(msg);

                        player.sendMessage("");
                        player.sendMessage(ChatColor.GRAY + "Instructions :");
                        player.sendMessage(ChatColor.WHITE + "  1. " + ChatColor.GRAY
                                + "Va sur §b" + siteUrl + "/faction.html");
                        player.sendMessage(ChatColor.WHITE + "  2. " + ChatColor.GRAY
                                + "Connecte-toi (ou crée un compte)");
                        player.sendMessage(ChatColor.WHITE + "  3. " + ChatColor.GRAY
                                + "Entre le code §e" + code + " §7dans le champ de liaison");
                        player.sendMessage(ChatColor.WHITE + "  4. " + ChatColor.GRAY
                                + "Clique §b🔗 Lier le compte");
                        player.sendMessage("");
                        player.sendMessage(ChatColor.DARK_GRAY + "⏱ Ce code expire dans §710 minutes§8.");
                        player.sendMessage(ChatColor.DARK_GRAY + "Tape §7/lier statut §8pour vérifier ton lien.");
                        player.sendMessage("");

                        player.playSound(player.getLocation(),
                            org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, 0.8f, 1.5f);
                    }
                );
            }
        );

        return true;
    }
}

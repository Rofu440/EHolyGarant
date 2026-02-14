package org.rofu.eholygarant.core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.rofu.eholygarant.EHolyGarant;
import org.rofu.eholygarant.deal.Deal;
import org.rofu.eholygarant.deal.DealStatus;
import org.rofu.eholygarant.core.model.DealStats;
import org.rofu.eholygarant.core.util.ColorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GarantCommand implements CommandExecutor, TabCompleter {
    private final EHolyGarant plugin;

    public GarantCommand(EHolyGarant plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) return cmdMenu(sender);
        switch (args[0].toLowerCase()) {
            case "list": return cmdList(sender);
            case "reload": return cmdReload(sender);
            case "help": return cmdHelp(sender);
            case "stats": return cmdStats(sender);
            case "cancel": return cmdCancel(sender);
            default: return cmdMenu(sender);
        }
    }

    private boolean cmdMenu(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getConfigManager().getMessages().get("only-player"));
            return true;
        }
        plugin.getMenuManager().openCreateDealMenu((Player) sender);
        return true;
    }

    private boolean cmdList(CommandSender sender) {
        if (!checkPlayerAndPermission(sender, "garant.moderator")) return true;
        plugin.getMenuManager().openDealListMenu((Player) sender);
        return true;
    }

    private boolean cmdReload(CommandSender sender) {
        if (!sender.hasPermission("garant.admin")) {
            sender.sendMessage(plugin.getConfigManager().getMessages().get("no-permission"));
            return true;
        }
        plugin.reload();
        sender.sendMessage(plugin.getConfigManager().getMessages().get("reload"));
        return true;
    }

    private boolean cmdHelp(CommandSender sender) {
        List<String> helpMessages = plugin.getConfigManager().getMessages().getList("help");
        if (helpMessages.isEmpty()) sendHelp(sender);
        else helpMessages.stream().map(ColorUtil::colorize).forEach(sender::sendMessage);
        return true;
    }

    private boolean cmdStats(CommandSender sender) {
        if (!checkPlayerAndPermission(sender, "garant.moderator")) return true;
        Player player = (Player) sender;
        DealStats stats = plugin.getStatsManager().getStats(player.getUniqueId());
        List<String> statsMessages = plugin.getConfigManager().getMessages().getList("stats-info");
        if (statsMessages.isEmpty()) {
            player.sendMessage(ColorUtil.colorize("&6▶ &fВаша статистика:"));
            player.sendMessage(ColorUtil.colorize("&7├ &fУспешных сделок: &a" + stats.getSuccessCount()));
            player.sendMessage(ColorUtil.colorize("&7├ &fОтменённых сделок: &c" + stats.getCancelledCount()));
            player.sendMessage(ColorUtil.colorize("&7└ &fЗаработано: &e" + (int) stats.getTotalEarned() + " монет"));
        } else {
            statsMessages.stream()
                    .map(line -> line
                            .replace("%success%", String.valueOf(stats.getSuccessCount()))
                            .replace("%cancelled%", String.valueOf(stats.getCancelledCount()))
                            .replace("%earned%", String.valueOf((int) stats.getTotalEarned())))
                    .map(ColorUtil::colorize)
                    .forEach(player::sendMessage);
        }
        return true;
    }

    private boolean cmdCancel(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getConfigManager().getMessages().get("only-player"));
            return true;
        }
        Player player = (Player) sender;
        Optional<Deal> dealOpt = plugin.getDealManager().getActiveDealByPlayer(player.getUniqueId());
        if (!dealOpt.isPresent()) {
            player.sendMessage(plugin.getConfigManager().getMessages().get("no-active-deal"));
            return true;
        }
        Deal deal = dealOpt.get();
        if (deal.getStatus() == DealStatus.WAITING) {
            plugin.getDealManager().cancelDeal(deal.getId(), "Отменено игроком");
            player.sendMessage(plugin.getConfigManager().getMessages().get("deal-cancelled-by-player"));
        } else {
            player.sendMessage(plugin.getConfigManager().getMessages().get("cannot-cancel-in-progress"));
        }
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ColorUtil.colorize("&e&lUnigarant &7- &fСистема гарантов\n"));
        sender.sendMessage(ColorUtil.colorize("&e/garant &7- &fОткрыть меню создания заявки"));
        sender.sendMessage(ColorUtil.colorize("&e/garant cancel &7- &fОтменить свою заявку"));
        if (sender.hasPermission("garant.moderator")) {
            sender.sendMessage(ColorUtil.colorize("&e/garant list &7- &fСписок активных заявок"));
            sender.sendMessage(ColorUtil.colorize("&e/garant stats &7- &fВаша статистика"));
        }
        if (sender.hasPermission("garant.admin")) {
            sender.sendMessage(ColorUtil.colorize("&e/garant reload &7- &fПерезагрузить конфиг"));
        }
        sender.sendMessage("");
    }

    private boolean checkPlayerAndPermission(CommandSender sender, String perm) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getConfigManager().getMessages().get("only-player"));
            return false;
        }
        if (!sender.hasPermission(perm)) {
            sender.sendMessage(plugin.getConfigManager().getMessages().get("no-permission"));
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 1) return new ArrayList<>();
        List<String> subCommands = new ArrayList<>();
        subCommands.add("help");
        subCommands.add("cancel");
        if (sender.hasPermission("garant.moderator")) {
            subCommands.add("list");
            subCommands.add("stats");
        }
        if (sender.hasPermission("garant.admin")) {
            subCommands.add("reload");
        }
        String input = args[0].toLowerCase();
        return subCommands.stream().filter(s -> s.startsWith(input)).collect(Collectors.toList());
    }
}

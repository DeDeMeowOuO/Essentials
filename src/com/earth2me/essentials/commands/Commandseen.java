package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import com.earth2me.essentials.UserMap;
import com.earth2me.essentials.utils.StringUtil;
import com.earth2me.essentials.utils.DateUtil;
import com.earth2me.essentials.utils.FormatUtil;

import com.massivecraft.mcore.mixin.Mixin;
import com.massivecraft.mcore.util.Txt;

import java.util.ArrayList;
import java.util.List;

import net.immortalcraft.factions.entity.UPlayer;
import net.immortalcraft.vampire.VPlayer;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandseen extends EssentialsCommand
{
	public Commandseen()
	{
		super("seen");
	}

	@Override
	protected void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		seen(server, sender, args, true, true, true);
                
	}

	@Override
	protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		seen(server, user.getBase(), args, user.isAuthorized("essentials.seen.banreason"), user.isAuthorized("essentials.seen.extra"), user.isAuthorized("essentials.seen.ipsearch"));
	}

	protected void seen(final Server server, final CommandSender sender, final String[] args, final boolean showBan, final boolean extra, final boolean ipLookup) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		try
		{
			User user = getPlayer(server, sender, args, 0);
			seenOnline(server, sender, user, showBan, extra);
		}
		catch (NoSuchFieldException e)
		{
			User player = ess.getOfflineUser(args[0]);
			if (player == null)
			{
				if (ipLookup && FormatUtil.validIP(args[0]))
				{
					seenIP(server, sender, args[0]);
					return;
				}
				else
				{
					throw new Exception(_("playerNotFound"));
				}
			}
			seenOffline(server, sender, player, showBan, extra);
		}
	}

	private void seenOnline(final Server server, final CommandSender sender, final User user, final boolean showBan, final boolean extra) throws Exception
	{
            user.setDisplayNick();
            Mixin.msg(sender , Txt.titleize("Player "+user.getName()));

                sender.sendMessage(_("showDisplayName", user.getDisplayName()));
		sender.sendMessage(_("seenOnline"));
		if (user.isAfk())
		{
			sender.sendMessage(_("whoisAFK"));
		}
                else if ( ! user.isAfk())
		{
                        sender.sendMessage(_("whonotAFK"));
		}
                if (user.isBanned())
		{
			sender.sendMessage(_("whoisBanned", user.getBanReason()));
		}
                else if ( ! user.isBanned())
		{
			sender.sendMessage(_("whonotBanned"));
		}
		if (user.isMuted())
		{
			sender.sendMessage(_("whoisMuted", DateUtil.formatDateDiff(user.getMuteTimeout())));
		}
                else if ( ! user.isMuted())
		{
			sender.sendMessage(_("whonotMuted"));
		}
                final Location loc = user.getLogoutLocation();
                if (loc != null)
                {
			sender.sendMessage(_("whoisLocation", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
                }
                        sender.sendMessage(_("showMoney", user.getMoney()));
                if (UPlayer.get(user.getBase()).hasFaction())
                {
                        sender.sendMessage(_("whoisFaction", UPlayer.get(user.getBase()).getFactionName()));
                }
                else if ( ! UPlayer.get(user.getBase()).hasFaction())
                {
                        sender.sendMessage(_("noFaction"));
                }
                if (VPlayer.get(user.getBase()).isVampire())
                {
                        sender.sendMessage(_("whoisVampire"));
                }
                else if ( ! VPlayer.get(user.getBase()).isVampire())
                {
                        sender.sendMessage(_("whonotVampire"));
                }
		final String location = user.getGeoLocation();
		if (location != null && (!(sender instanceof Player) || ess.getUser(sender).isAuthorized("essentials.geoip.show")))
		{
			sender.sendMessage(_("whoisGeoLocation", location));
		}
		if (extra)
		{
			sender.sendMessage(_("whoisIPAddress", user.getAddress().getAddress().toString()));
		}

	}
	private void seenOffline(final Server server, final CommandSender sender, User user, final boolean showBan, final boolean extra) throws Exception
	{
		user.setDisplayNick();
                Mixin.msg(sender , Txt.titleize("Player "+user.getName()));
                
                sender.sendMessage(_("showDisplayName", user.getDisplayName()));
		if (user.getLastLogout() > 0)
		{
			sender.sendMessage(_("seenOffline", DateUtil.formatDateDiff(user.getLastLogout())));
		}
		else
		{
			sender.sendMessage(_("userUnknown", user.getName()));
		}
		if (user.isBanned())
		{
			sender.sendMessage(_("whoisBanned", user.getBanReason()));
		}
                else if ( ! user.isBanned())
		{
			sender.sendMessage(_("whonotBanned"));
		}
		if (user.isMuted())
		{
			sender.sendMessage(_("whoisMuted", DateUtil.formatDateDiff(user.getMuteTimeout())));
		}
                else if ( ! user.isMuted())
		{
			sender.sendMessage(_("whonotMuted"));
		}
                final Location loc = user.getLogoutLocation();
		if (loc != null)
		{
			sender.sendMessage(_("whoisLocation", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
		}
                        sender.sendMessage(_("showMoney", user.getMoney()));
                if (UPlayer.get(user.getBase()).hasFaction())
                {
                        sender.sendMessage(_("whoisFaction", UPlayer.get(user.getBase()).getFactionName()));
                }
                else if ( ! UPlayer.get(user.getBase()).hasFaction())
                {
                        sender.sendMessage(_("noFaction"));
                }
                if (VPlayer.get(user.getBase()).isVampire())
                {
                        sender.sendMessage(_("whoisVampire"));
                }
                else if ( ! VPlayer.get(user.getBase()).isVampire())
                {
                        sender.sendMessage(_("whonotVampire"));
                }
		final String location = user.getGeoLocation();
		if (location != null && (!(sender instanceof Player) || ess.getUser(sender).isAuthorized("essentials.geoip.show")))
		{
			sender.sendMessage(_("whoisGeoLocation", location));
		}
		if (extra)
		{
			if (!user.getLastLoginAddress().isEmpty())
			{
				sender.sendMessage(_("whoisIPAddress", user.getLastLoginAddress()));
			}
			}
		}

	private void seenIP(final Server server, final CommandSender sender, final String ipAddress) throws Exception
	{
		final UserMap userMap = ess.getUserMap();
		sender.sendMessage(_("runningPlayerMatch", ipAddress));

		ess.runTaskAsynchronously(new Runnable()
		{
			@Override
			public void run()
			{
				final List<String> matches = new ArrayList<String>();
				for (final String u : userMap.getAllUniqueUsers())
				{
					final User user = ess.getUserMap().getUser(u);
					if (user == null)
					{
						continue;
					}

					final String uIPAddress = user.getLastLoginAddress();

					if (!uIPAddress.isEmpty() && uIPAddress.equalsIgnoreCase(ipAddress))
					{
						matches.add(user.getName());
					}
				}

				if (matches.size() > 0)
				{
					sender.sendMessage(_("matchingIPAddress"));
					sender.sendMessage(StringUtil.joinList(matches));
				}
				else
				{
					sender.sendMessage(_("noMatchingPlayers"));
				}

			}
		});

	}
}

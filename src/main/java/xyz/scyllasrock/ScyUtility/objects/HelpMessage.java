package xyz.scyllasrock.ScyUtility.objects;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

/**
 * Helps developer create clickable, hoverable, and pageable chat messages quickly
 * @deprecated Will need to be recreated or abandoned due to the use of new Command API
 * @author kwilk
 *
 */
public class HelpMessage {
	
	/**
	 * 
	 * @param commands
	 * @param player - Return help message for commands that specified player has permission for
	 * @param page
	 * @param commandsPerPage
	 * @param cmdColor - **net.md_5.bungee.api.ChatColor** color to give the command
	 * @param descColor - **net.md_5.bungee.api.ChatColor** color to give the description
	 * @param header
	 * @param footer
	 * @return Clickable/Hoverable help message with appended headers and footers and of the specified page.
	 */
	public static void appendHelpMessageBuilder(ComponentBuilder header, final List<ScyCommand> commands, final Player player,
			int page, int commandsPerPage, ChatColor cmdColor, ChatColor descColor) {
		//Filter out commands that player does not have permission for
		List<ScyCommand> newCommands = commands.stream().filter(command -> command.getPermission() == null || player.hasPermission(command.getPermission())).collect(Collectors.toList());
		appendHelpMessageBuilder(header, newCommands, page, commandsPerPage, cmdColor, descColor);
	}
	
	/**
	 * 
	 * @param commands
	 * @param page
	 * @param commandsPerPage
	 * @param cmdColor - **net.md_5.bungee.api.ChatColor** color to give the command
	 * @param descColor - **net.md_5.bungee.api.ChatColor** color to give the description
	 * @param header
	 * @param footer
	 * @return Clickable/Hoverable help message with appended headers and footers and of the specified page.
	 */
	public static void appendHelpMessageBuilder(ComponentBuilder header, final List<ScyCommand> commands, int page, int commandsPerPage,
			ChatColor cmdColor, ChatColor descColor) {
		if(page <= 0) page = 1;
		
		
		//Calculate how many pages are in the help messages
		int maxPages = getMaxPages(commands, commandsPerPage);
		if(page > maxPages) page = maxPages;		
		
		//Loop through sub commands and add to menu
		for(ScyCommand cmd : getHelpCommandsForPage(commands, page, commandsPerPage, maxPages)) {
			String usage;
			if(cmd instanceof ScyBaseCommand) {
				ScyBaseCommand cmdBase = (ScyBaseCommand) cmd;
				usage = cmdBase.getUsage();
			}
			else {
				ScySubCommand cmdSub = (ScySubCommand) cmd;
				usage = cmdSub.getUsage();
			}
			
			//Usage
			header.append("\n" + usage).color(cmdColor)
			.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new Text(ChatColor.translateAlternateColorCodes('&', "&8Click to copy"))))
			.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, usage));
			
			//Description
			header.append(" - " + cmd.getDescription()).color(descColor)
			.event((HoverEvent) null).event((ClickEvent) null);
		}
		header.append("\n");
				
	}
	
	/**
	 * 
	 * @param commands - list of all commands to attempt to put into help menu (if player has permission)
	 * @param player
	 * @param commandsPerPage - max commands to display in a single page
	 * @return
	 */
	public static int getMaxPages(List<ScyCommand> commands, Player player, int commandsPerPage) {
		List<ScyCommand> newCommands = commands.stream().filter(command -> command.getPermission() == null || player.hasPermission(command.getPermission())).collect(Collectors.toList());
		return (int) Math.ceil((float) newCommands.size() / (float) commandsPerPage);
	}
	
	
//	private static void addHeaderFooter(ComponentBuilder builder, ComponentBuilder headerFooter) {
//		builder.append(headerFooter.create());		
//	}

	
	private static int getMaxPages(List<ScyCommand> commands, int commandsPerPage) {
		return (int) Math.ceil((float) commands.size() / (float) commandsPerPage);
	}
	

	
	
	/**
	 * Requires page is a valid page between 1 - maxPages
	 * @param page
	 * @return
	 */
	private static List<ScyCommand> getHelpCommandsForPage(List<ScyCommand> commands, int page, int commandsPerPage, int maxPages) {
		int lastInd = page == maxPages ? commands.size() : (int) (commandsPerPage * page);
		return commands.subList((int) (commandsPerPage * (page - 1)), lastInd);
	}
	
}

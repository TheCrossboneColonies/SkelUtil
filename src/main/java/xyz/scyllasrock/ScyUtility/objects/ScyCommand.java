package xyz.scyllasrock.ScyUtility.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * @deprecated - will use CommandAPI instead
 * @author kwilk
 *
 */
public abstract class ScyCommand {
	

	private String description;
	private String permission;
	private boolean playerOnly;
	private boolean consoleOnly;
	private List<ScySubCommand> subCommands = new ArrayList<ScySubCommand>();
	
	public ScyCommand() {
		this.description = null;
		this.permission = null;
		this.playerOnly = false;
		this.consoleOnly = false;
	}
	
	public ScyCommand(@Nullable String description, @Nullable String permission) {
		this.description = description;
		this.permission = permission;
		this.playerOnly = false;
		this.consoleOnly = false;
	}
	
	public ScyCommand(@Nullable String description, @Nullable String permission, boolean playerOnly, boolean consoleOnly) {
		this.description = description;
		this.permission = permission;
		this.playerOnly = playerOnly;
		this.consoleOnly = consoleOnly;
	}
	

	
	public final List<ScySubCommand> getSubCommands(){ return subCommands;}
	public final void addSubCommand(ScySubCommand subCommand) { 
		subCommands.add(subCommand);
		
		//Check that number of subcommands = 1 if there is a variable argument. Otherwise, send warning to console
		boolean variableArgument = false;
		for(ScySubCommand cmd : subCommands) {
			if(cmd.getArgumentType() == ArgumentType.OPTIONAL_VARIES || cmd.getArgumentType() == ArgumentType.REQUIRED_VARIES) {
				variableArgument = true;
				break;
			}
		}
		if(variableArgument && subCommands.size() > 1) Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Warning: Command with description "
		+ description + " has more than one subcommand when a variable argument is present. This could cause issues if there is commonality between arguments in the subcommands.\n"
				+ "Ex: /kingdom delete <kingdom> and /kingdom delete confirm - what if there is a kingdom named 'confirm'?");
	}
	
	public final String getDescription() { return description;}
	public final void setDescription(String description) {this.description = description;} 
	
	public final String getPermission() { return permission;}
	public final void setPermission(String permission) {this.permission = permission;} 
	
	public final boolean isConsoleOnly() { return consoleOnly;}
	public final boolean isPlayerOnly() { return playerOnly;}
	
	/**
	 * 
	 * @param sender
	 * @param args
	 * @return
	 */
	public abstract void execute(CommandSender sender, String[] args);
	
	public void executeFinalCommand(CommandSender sender, String[] args, boolean permissionOverride, int commandLength) {
		
		//Check playerOnly or consoleOnly
		if(sender instanceof Player && isConsoleOnly()) {
			Player player = (Player) sender;
			if(player.hasPermission(getPermission()))
				player.sendMessage(ChatColor.RED + "Sorry, this command is only for console!");
			else
				player.sendMessage("Unknown command. Type \"/help\" for help."); //Default message when no command exists
			return;
		}
		
		if(sender instanceof ConsoleCommandSender && isPlayerOnly()) {
			sender.sendMessage(ChatColor.RED + "Sorry, this command is only for players!");
			return;
		}
		
		//Check permission - does continue until command is found though
		boolean hasPermission = permissionOverride;
		if(sender instanceof Player && !permissionOverride) {
			Player player = (Player) sender;
			if(getPermission() == null) hasPermission = true;
			else if(player.hasPermission(getPermission())) hasPermission = true;
		}
		
		//execute command or sub command
		if(args.length == commandLength || this.subCommands.isEmpty()) { //If no more args or no more sub commands
			//Check perms to execute this command
			if(!hasPermission) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cSorry, you do not have permission to run this command :("));
				return;
			}
			//Run base command
			execute(sender, args);
			return;
		}
		else {
			//Find subcommand to run...
			
			//Check first argument
			for(ScySubCommand subCmd : this.subCommands) {
				if(subCmd.getArgument().equalsIgnoreCase(args[commandLength]) ||
						subCmd.getArgumentType() == ArgumentType.OPTIONAL_VARIES ||
						subCmd.getArgumentType() == ArgumentType.REQUIRED_VARIES) {
					subCmd.executeFinalCommand(sender, args, hasPermission, commandLength + 1);
					return;
				}
			}
			
			//Check aliases
			for(ScySubCommand subCmd : this.subCommands) {
				if(subCmd.getAliases().contains(args[commandLength].toLowerCase()) ||
						subCmd.getArgumentType() == ArgumentType.OPTIONAL_VARIES ||
						subCmd.getArgumentType() == ArgumentType.REQUIRED_VARIES) {
					subCmd.executeFinalCommand(sender, args, hasPermission, commandLength + 1);
					return;
				}
			}
		}
	}
	

	
	/**
	 * @param player
	 * @param args
	 * @return
	 */
	public abstract List<String> getTabCompletions(Player player, String[] args);
	

	public final List<String> getFinalTabCompletions(Player player, String[] args, int commandLength){		
		List<String> completions = new ArrayList<String>();
		
		//Get scycommand that tab completions should be done for...
		//Command has been found
		if(args.length == commandLength + 1) {
			final String searching = args[args.length - 1].toLowerCase();
			
			for(ScySubCommand subCmd : subCommands) {
				if(hasAnySubCmdPerms(player, subCmd)) {
					subCmd.getTabCompletions(player, args).stream()
					.filter(s -> s.startsWith(searching))
					.forEach(s -> completions.add(s));
				}
			}

			//Sort in alphabetical order
			Collections.sort(completions);
			
			return completions;
		}
		//Search recursively for sub command
		else {
			//Check first argument
			for(ScySubCommand subCmd : this.subCommands) {
				if(subCmd.getArgument().equalsIgnoreCase(args[commandLength]) ||
						subCmd.getArgumentType() == ArgumentType.OPTIONAL_VARIES ||
						subCmd.getArgumentType() == ArgumentType.REQUIRED_VARIES) {
					return subCmd.getFinalTabCompletions(player, args, commandLength + 1);
				}
			}
			
			//Check aliases
			for(ScySubCommand subCmd : this.subCommands) {
				if(subCmd.getAliases().contains(args[commandLength].toLowerCase()) ||
						subCmd.getArgumentType() == ArgumentType.OPTIONAL_VARIES ||
						subCmd.getArgumentType() == ArgumentType.REQUIRED_VARIES) {
					return subCmd.getFinalTabCompletions(player, args, commandLength + 1);
				}
			}
			
			//Return empty list
			return new ArrayList<String>();
		}		
		
	}
	
	
	private boolean hasAnySubCmdPerms(Player player, ScySubCommand rootSubCmd) {
		//Check perms for root sub command
		if(rootSubCmd.getPermission() == null) return true;
		if(player.hasPermission(rootSubCmd.getPermission())) return true;
		
		//Checks perms for each sub command and their sub commands
		for(ScySubCommand subCmd : rootSubCmd.getSubCommands()) {
			if(hasAnySubCmdPerms(player, subCmd)) return true;
		}
		return false;
		
	}
	


}

package xyz.scyllasrock.ScyUtility.objects;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.scyllasrock.ScyUtility.Main;

/**
 * @deprecated - will use CommandAPI instead
 * @author kwilk
 *
 */
public abstract class SubCommand {

	private String baseCommand;
	private String firstArgument;
	private List<String> aliases;
	private String permission;
	private String usage;
	private String description;
	
	/**
	 * 
	 * @param baseCommand - command label. ex: /"give"
	 * @param firstArgument - first arg in the command. ex: /give "money", /give "xp"
	 * @param aliases - list of other first arguments that are accepted to run this subcommand. 
	 * Note: first arguments of other subcommands are prioritized before aliases are read and all aliases are lowercase.
	 * @param permission - permission required to use the command. ex: "give.xp"
	 * @param usage - How to use the command. ex: "/give money <player> <material> [amount]."
	 * Please use <> for required arguments and [] for optional arguments. Please do NOT use color codes!
	 * This is the message that shows up in the help message
	 * @param description - Description of what the command does. Please do NOT use color codes!
	 * ex: "Gives specified player some materials."
	 */
	public SubCommand(String baseCommand, String firstArgument, List<String> aliases, String permission,
			String usage, String description) {
		this.baseCommand = baseCommand;
		this.firstArgument = firstArgument;
		this.aliases = aliases.stream().map(s -> s.toLowerCase()).collect(Collectors.toList());
		this.permission = permission;
		this.usage = usage;
		this.description = description;
	}
	
	public final String getBaseCommand() { return baseCommand;}
	public final String getFirstArgument() { return firstArgument; }
	public final List<String> getAliases() { return aliases; }
	public final String getPermission() {	return permission;}
	public final String getUsage() { return usage;}
	public final String getDescription() { return description;}
	public abstract boolean execute(CommandSender sender, String[] args);
	
	/**
	 * Only tab completions for second+ arguments need to be handled. If your subcommand is only one argument, return null.
	 * @param player
	 * @param args
	 * @return
	 */
	public abstract List<String> getTabCompletions(Player player, String[] args);
	
}

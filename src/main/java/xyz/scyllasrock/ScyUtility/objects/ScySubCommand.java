package xyz.scyllasrock.ScyUtility.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * @deprecated - will use CommandAPI instead
 * @author kwilk
 *
 */
public abstract class ScySubCommand extends ScyCommand {

	private String argumentLabel;
	private ArgumentType type;
	private List<String> aliases = new ArrayList<String>();
	private ScyCommand previousCommand;
	

	/**
	 * 
	 * @param argument
	 * @param type - whether argument is a variable and is required/optional
	 * @param aliases - use empty list for no aliases
	 * @param permission - permission can be null if no perms are needed
	 * @param previousCommand - command representing previous argument. Can be null if this command represents the first argument (base command).
	 * 
	 * example #1: /team
	 * argument = "team"
	 * type = REQUIRED_STATIC
	 * aliases = empty string list
	 * permission = null
	 * previousCommand = null
	 * 
	 * example #2: /team invite
	 * argument = "invite"
	 * type = REQUIRED_STATIC
	 * aliases = empty string list
	 * permission = null
	 * previousCommand = *pointer to example #1
	 * 
	 * example #3: /team invite <player>
	 * argument = "player"
	 * type = REQUIRED_VARIES
	 * aliases = empty string list
	 * permission = "team.invite.player"
	 * previousCommand = *pointer to example #2
	 * 
	 * example #4: /team invite accept
	 * argument = "accept"
	 * type = REQUIRED_STATIC
	 * aliases = Arrays.asList("acc", "yes")
	 * permission = "team.invite.accept"
	 * previousCommand = *pointer to example #2
	 * 
	 */
	
	public ScySubCommand(String argumentLabel, ArgumentType type, boolean playerOnly, boolean consoleOnly, ScyCommand previousCommand) {
		super(null, null, playerOnly, consoleOnly);
		this.argumentLabel = argumentLabel.toLowerCase();
		this.type = type;
		this.previousCommand = previousCommand;
		
		//Confirm aliases is null or empty if argument type varies
		if(type == ArgumentType.OPTIONAL_VARIES || type == ArgumentType.REQUIRED_VARIES) {
			if(aliases != null && !aliases.isEmpty()) throw new 
			IllegalArgumentException("Cannot have argument type " + type.toString() + " with non-empty aliases list.");
		}

	}
	
	public ScySubCommand(String argumentLabel, ArgumentType type, boolean playerOnly, boolean consoleOnly,
			@Nullable String permission, @Nullable String description, ScyCommand previousCommand) {
		super(description, permission, playerOnly, consoleOnly);
		this.argumentLabel = argumentLabel.toLowerCase();
		this.type = type;
		this.previousCommand = previousCommand;
		
		//Confirm aliases is null or empty if argument type varies
		if(type == ArgumentType.OPTIONAL_VARIES || type == ArgumentType.REQUIRED_VARIES) {
			if(aliases != null && !aliases.isEmpty()) throw new 
			IllegalArgumentException("Cannot have argument type " + type.toString() + " with non-empty aliases list.");
		}

	}
	
	public ScySubCommand(String argumentLabel, ArgumentType type, boolean playerOnly, boolean consoleOnly,
			@Nullable String permission, @Nullable String description,
			List<String> aliases,  ScyCommand previousCommand) {
		super(description, permission, playerOnly, consoleOnly);
		this.argumentLabel = argumentLabel.toLowerCase();
		this.type = type;
		this.aliases = aliases.stream().map(s -> s.toLowerCase()).collect(Collectors.toList()); //Make aliases lowercase
		this.previousCommand = previousCommand;
		
		//Confirm aliases is null or empty if argument type varies
		if(type == ArgumentType.OPTIONAL_VARIES || type == ArgumentType.REQUIRED_VARIES) {
			if(aliases != null && !aliases.isEmpty()) throw new 
			IllegalArgumentException("Cannot have argument type " + type.toString() + " with non-empty aliases list.");
		}

	}
	
	
	public final String getArgument() { return argumentLabel; }
	public final ArgumentType getArgumentType() { return type; }
	public final List<String> getAliases() { return aliases; }
	
	public final String getUsage() {
		String usage = getArgumentUsage();
		ScyCommand previous = previousCommand;
		while(previous instanceof ScySubCommand) {
			ScySubCommand prevSub = (ScySubCommand) previous;
			usage = prevSub.getArgumentUsage() + " " + usage;
			previous = prevSub.getPreviousCommand();
		}
		ScyBaseCommand base = (ScyBaseCommand) previous;
		usage = "/" + base.getCommandLabel() + " " + usage;
		return usage;
	}
	
	public ScyCommand getPreviousCommand() { return previousCommand; }
	
	public final String getArgumentUsage() {
		switch(type) {
		case REQUIRED_VARIES:
			return "<" + argumentLabel + ">";
		case OPTIONAL_VARIES:
			return "[" + argumentLabel + "]";
		case REQUIRED_STATIC:
			return argumentLabel;
		case OPTIONAL_STATIC:
			return "['" + argumentLabel + "']";
		default:
			return "";
		}
		
	}
	
	

	
}

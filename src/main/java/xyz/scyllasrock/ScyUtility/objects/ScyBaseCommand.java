package xyz.scyllasrock.ScyUtility.objects;

import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

/**
 * @deprecated - will use CommandAPI instead
 * @author kwilk
 *
 */
public abstract class ScyBaseCommand extends ScyCommand implements TabExecutor {
	
	private String commandLabel;

	public ScyBaseCommand(String commandLabel, String description, @Nullable String permission, boolean playerOnly, boolean consoleOnly) {
		super(description, permission, playerOnly, consoleOnly);
		this.commandLabel = commandLabel;
	}
	
	/**
	 * Execute the command. Checks recursively for the subcommand to run
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		int commandLength = 0;
		boolean hasPerms;
		if(sender instanceof ConsoleCommandSender) hasPerms = true;
		else hasPerms = false;
		this.executeFinalCommand(sender, args, hasPerms, commandLength);		
		
		return true;
	}
	

	/**
	 * Perform the tab completions. Checks recursively for subcommand tab completions
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return null;
		
		return this.getFinalTabCompletions((Player) sender, args, 0);
	}
	

	//Base commands do not need tab completions changed
	public List<String> getTabCompletions(Player player, String[] args){
		return null;
	}
	
	public String getCommandLabel() { return commandLabel; }
	
	public final String getUsage() {
		return "/" + getCommandLabel();
	}
	

}

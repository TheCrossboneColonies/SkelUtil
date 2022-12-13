package xyz.scyllasrock.ScyUtility.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated will use CommandAPI instead
 * @author kwilk
 *
 */
public abstract class BaseCommand {
	
	private List<SubCommand> subCommands = new ArrayList<SubCommand>();
	
	public final List<SubCommand> getSubCommands(){ return subCommands;}
	public final void addSubCommand(SubCommand subCommand) { subCommands.add(subCommand);}

}

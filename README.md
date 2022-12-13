# ScyUtility Wiki

# ActionBarAPI
ActionBarAPI static methods. Should be self explanatory. <br/> The MutableBoolean method allows for action bar messages to be sent while the MutableBoolean passed in remains true.

# CommandAPI (New System)
## Adding API to dev environment
https://commandapi.jorel.dev/8.5.1/quickstart.html

You must also install the jar into your plugins folder

## Basic use of the api
```Java
public class testCommand {

	public testCommand() {
		// Add all new commands with their methods here
		// Then call the constructor from the onEnable()
		makeTestCommand();
	}

	// The command API uses a builder system making it very easy to read and understand what is going on.
	public void makeTestCommand() {
		new CommandAPICommand("CommandName")
		.withArguments(new ArgumentType("nodeName")) // List of arguments here: https://commandapi.jorel.dev/8.5.1/arguments.html
		.executesPlayer((player, args) -> { // Types of objects that the command executes by here: https://commandapi.jorel.dev/8.5.1/commandexecutors.html
		// Logic
		}).register; // Adds the command. No need for plugin.yml just call the constructor in the on enable
	}
}
```
## How to make arguments with tooltips
```Java
public class testCommand {

	public testCommand() {
		makeTestCommand();
	}


	public void makeTestCommand() {
		// Make list of argument object
		List<Argument<?>> arguments = new ArrayList<>(); // it must be <Argument<?>>
		// Add arguments IN THE ORDER YOU WANT THEM TO BE IN
		arguments.add(new ArgumentType("nodeName")); // args[0]
		arguments.add(new ArgumentType("nodeName") // args[1]
			.replaceSuggestions(ArgumentSuggestions.stringsWithTooltips(info -> new IStringToolTip[] {
				// You can add as many suggestions as you want here
				// The nodename is just the name of the argument
				// But the StringTooltip.of(); are the suggestions given
				StringTooltip.of("Suggestion", "Tooltip")
			}
			))
			);
		new CommandAPICommand("CommandName")
		.withArguments(arguments) // Note the args[i] will be in order from which they were added
		.executesPlayer((player, args) -> { 
		// Logic
		}).register; // Adds the command. No need for plugin.yml just call the constructor in the on enable
	}
}
```
# ScyCommands (TO BE REMOVED)
## ScyBaseCommand
```Java
public class ScyUtilTestCmd extends ScyBaseCommand {
	
	/**
	 * Constructor:
	 * "scytest" - command label (/scytest)
	 * "Base command description" - Description that goes along with this command if used in a help menu
	 * Permission (can be null) - A player CAN run subcommands if they do NOT have perms for the base command
	 * false - is this command run only by players?
	 * false - is this command run only by console?
	 *  - only one of these should be true at any given time!
	 */
	public ScyUtilTestCmd() {
		super("scytest", "Base command description", "scytest.*", false, false);
		
		//**Add subcommands here
		super.addSubCommand(new ScyUtilTestGUI(this));
		super.addSubCommand(new ScyUtilTestBroadcast(this));
		super.addSubCommand(new ScyUtilTestHelp(this));
	}


	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage("Use /scytest help for help on how to use this command.");
	}

}
```

## ScySubCommand
```Java
public class ScyUtilTestGUI extends ScySubCommand {
	

	/**
	 * Constructor:
	 * "gui" - sub command argument (/scytest gui)
	 * REQUIRED_STATIC - states that this argument is required and does not change
	 * true - only players can run this command
	 * false - console cannot run it
	 * null - no permission needed
	 * "opens test gui" - description if subcommand shown in help menu
	 * previousCommand - pointer to the previous command (used in creating usage messages)
	 * @param previousCommand
	 */
	public ScyUtilTestGUI(ScyCommand previousCommand) {
		super("gui", ArgumentType.REQUIRED_STATIC, true, false, null, "opens test gui", previousCommand);
		
		//Add sub sub commands <3
		super.addSubCommand(new ScyUtilTestGUIPlayer(this));
	}

	
	/*
	 * Permission checks, player vs. console checks (if enabled above), and STATIC argument checks already done.
	 * VARIES argument types must still be checked!
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		//Since only players can run this, no "sender instanceof Player" check necessary
		Player player = (Player) sender;
		
		//Open a gui
		new MessageMenu().open(player);
	}

	@Override
	public List<String> getTabCompletions(Player player, String[] args) {
		return Arrays.asList("gui");
	}
	
	

}
```

# HelpMessage builder (TO BE REMOVED)<br/>
```Java
public class ScyUtilTestHelp extends ScySubCommand {
	
	public ScyUtilTestHelp(ScyCommand previousCommand) {
		
		super("help", ArgumentType.REQUIRED_STATIC, true, false, null,
				"opens help menu", previousCommand);
	}
	
	private final int COMMANDS_PER_PAGE = 2;

	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		//I already know it's a player because I specified true above
		Player player = (Player) sender;
		
		//Default page number		
		int page = 1;

		//If player specifies a page number
		if(args.length > 1) {
			try {
				page = Integer.parseInt(args[1]);
			} catch(NumberFormatException e) {
				player.sendMessage("Error: Not a number");
				return;
			}
		}
		
		//Cast list of ScySubCommand to ScyCommand
		List<ScyCommand> helpCmdList = new ArrayList<ScyCommand>();
		for(ScySubCommand subCmd : super.getPreviousCommand().getSubCommands()) {
			helpCmdList.add((ScyCommand) subCmd);
		}
		
		//Method to calculate max pages
		int maxPages = HelpMessage.getMaxPages(helpCmdList, player, COMMANDS_PER_PAGE); 
		
		//Update page to be between min/max (1 - max pages)
		if(page <= 0) page = 1;
		else if(page > maxPages) page = maxPages;
		
		//NOTE: Header must be passed by reference to appendHelpMessageBuilder
		//Create header first!
		ComponentBuilder header = 
				getHeader(page, maxPages);

		
		//APPEND help message to header
		HelpMessage.appendHelpMessageBuilder(header, helpCmdList, player, page, COMMANDS_PER_PAGE, ChatColor.YELLOW, ChatColor.GREEN);
		//Add footer
		addFooter(header, page, maxPages);
		player.spigot().sendMessage(header.create());
	}

	@Override
	public List<String> getTabCompletions(Player arg0, String[] arg1) {
		return Arrays.asList(super.getArgumentUsage());
	}
	
	private ComponentBuilder getHeader(int page, int maxPages) {
		ComponentBuilder builder = new ComponentBuilder();
		//Prev page clickable
		builder.append(ChatColor.translateAlternateColorCodes('&', "&7&m&l            &e&l<<<<&7&m&l &3 "))
		.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/scytest help " + (page - 1)))
		.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new Text(ChatColor.translateAlternateColorCodes('&', "&eShow previous"))));
		
		//Title
		builder.append(ChatColor.translateAlternateColorCodes('&', "&aScyTest Help Menu &2" + page + " &8/ &2" + maxPages))
		.event((HoverEvent) null)
		.event((ClickEvent) null);
		
		//Next page clickable
		builder.append(ChatColor.translateAlternateColorCodes('&', " &7&m&l &e&l>>>>&7&m&l            "))
		.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/scytest help " + (page + 1)))
		.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new Text(ChatColor.translateAlternateColorCodes('&', "&eShow next"))));
		return builder;
	}
	
	private ComponentBuilder addFooter(ComponentBuilder current, int page, int maxPages) {

		//Prev page clickable
		current.append(ChatColor.translateAlternateColorCodes('&', "&7&m&l            &e&l<<<<&7&m&l &3 "))
		.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/scytest help " + (page - 1)))
		.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new Text(ChatColor.translateAlternateColorCodes('&', "&eShow previous"))));
		
		//Title
		current.append(ChatColor.translateAlternateColorCodes('&', "&aScyTest Help Menu &2" + page + " &8/ &2" + maxPages))
		.event((HoverEvent) null)
		.event((ClickEvent) null);
		
		//Next page clickable
		current.append(ChatColor.translateAlternateColorCodes('&', " &7&m&l &e&l>>>>&7&m&l            "))
		.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/scytest help " + (page + 1)))
		.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new Text(ChatColor.translateAlternateColorCodes('&', "&eShow next"))));
		return current;
	}


```
# GUI (Coming soon - if requested)
Can create an inventory and set items/slots using lamba expressions. Makes development of inventories much faster

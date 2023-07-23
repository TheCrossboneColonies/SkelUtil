# SkelUtil Wiki

# ActionBarAPI
ActionBarAPI static methods. Should be self explanatory. <br/> The MutableBoolean method allows for action bar messages to be sent while the MutableBoolean passed in remains true.

# ConfigUpdater
TO BE REMOVED. Not useful anymore

# Cloud Command Framework
https://github.com/Incendo/cloud

Examples coming soon...

# CommandAPI (OLD System)
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

# GUI (Coming soon - if requested)
Can create an inventory and set items/slots using lamba expressions. Makes development of inventories much faster

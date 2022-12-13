package xyz.scyllasrock.ScyUtility.api;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.scyllasrock.ScyUtility.Main;
import xyz.scyllasrock.ScyUtility.objects.MutableBoolean;

public class ActionBarAPI {
	
	private static ActionBarAPI instance;
	
	private ActionBarAPI() { }
	
	public static ActionBarAPI getInstance() {
		if(instance == null) instance = new ActionBarAPI();
		return instance;
	}
	
	
	
	//MAKE COMPARATOR THAT SORTS PRIORITY BASED ON TASKPRIORITY FIRST AND CURRENT MESSAGE SECOND
	private static Map<UUID, ActionBarPlayer> actionBarPlayers = new HashMap<UUID, ActionBarPlayer>();
	
	/**
	 * Sends action bar message to specified player for time specified with priority specified.
	 * If another action bar message with higher priority exists, it will override ones with lower priority.
	 * Duration seconds still continue if message has been overridden.
	 * @param player
	 * @param message
	 * @param seconds
	 */
	public void sendActionBarMessage(Player player, String message, int seconds, TaskPriority priority) {

		sendActionBarMessage(player, message, (float) seconds, priority);
		
	}
	
	
	/**
	 * Sends action bar message to specified player for time specified with priority specified.
	 * If another action bar message with higher priority exists, it will override ones with lower priority.
	 * Duration seconds still continue if message has been overridden.
	 * @param player
	 * @param message
	 * @param seconds
	 */
	public void sendActionBarMessage(Player player, String message, float seconds, TaskPriority priority) {
		
		//Create new player if necessary
		ActionBarPlayer actionPlayer;
		if(!actionBarPlayers.containsKey(player.getUniqueId())) {
			actionPlayer = new ActionBarPlayer(player);
			actionBarPlayers.put(player.getUniqueId(), actionPlayer);
		}
		else actionPlayer = actionBarPlayers.get(player.getUniqueId());
		
		//Add ActionBarMessage to player's queue and send
		actionPlayer.addActionBarMessage(
				new ActionBarMessage(message, System.currentTimeMillis() + (int) (1000 * seconds), new MutableBoolean(true), priority));
	
		
	}
	
	/**
	 * Sends action bar message to specified player as long as the Boolean object inserted into the method
	 * remains true. If the object becomes false, the message is no longer sent and it is removed. A new call
	 * to sendActionBarMessage() must be made if the Boolean object ever becomes false.
	 * If another action bar message with higher priority exists, it will override ones with lower priority.
	 * @param player
	 * @param message
	 * @param condition
	 * @param priority
	 */
	public void sendActionBarMessage(Player player, String message, MutableBoolean condition, TaskPriority priority) {
		
		//Create new player if necessary
		ActionBarPlayer actionPlayer;
		if(!actionBarPlayers.containsKey(player.getUniqueId())) {
			actionPlayer = new ActionBarPlayer(player);
			actionBarPlayers.put(player.getUniqueId(), actionPlayer);
		}
		else actionPlayer = actionBarPlayers.get(player.getUniqueId());
		
		//Add ActionBarMessage to player's queue and send
		actionPlayer.addActionBarMessage(
				new ActionBarMessage(message, Long.MAX_VALUE, condition, priority));
		
	}

	
    //Sort PriorityQueue from highest UpdatePriority to lowest
    private Comparator<ActionBarMessage> comparator = new Comparator<ActionBarMessage>() {
        @Override
        public int compare(ActionBarMessage m1, ActionBarMessage m2) {        	
        	
        	int priorityInt = m2.priority.ordinal() - m1.priority.ordinal();
        	if(priorityInt != 0) return priorityInt;
        	
        	//If same priority, first one added to queue takes priority
        	return 1;
        	       	
        }
    };
	
	private class ActionBarPlayer {
		
		Player player;
		PriorityQueue<ActionBarMessage> messageQueue = new PriorityQueue<ActionBarMessage>(comparator);
		BukkitTask actionBarTask;
		
		private ActionBarPlayer(Player player) {
			this.player = player;
		}
		
		/**
		 * Adds a message to be sent and sends it
		 * @param message
		 */
		private void addActionBarMessage(ActionBarMessage message) {
			messageQueue.add(message);
			
			//If actionBarTask is null or previously cancelled, start a new task to send the message			
			if(actionBarTask == null || actionBarTask.isCancelled()) {
				
				actionBarTask = new BukkitRunnable() {

					@Override
					public void run() {
						
						//Poll first message that is still "valid", time not expired
						while(!messageQueue.isEmpty() && (messageQueue.peek().endTime < System.currentTimeMillis() || !messageQueue.peek().condition.getBooleanValue())) {
							messageQueue.remove();
						}
						
						//If queue is empty, cancel action bar task
						if(messageQueue.isEmpty()) {
							cancel();
							actionBarTask = null;
							actionBarPlayers.remove(player.getUniqueId()); //Fixes bug where player does not receive msg after a while
							return;
						}
							
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
								new TextComponent(messageQueue.peek().text));
						
					}
					
				}.runTaskTimer(Main.getInstance(), 0L, 2L);

			}
		}
		
		
		
	}
	
	private class ActionBarMessage {
		String text;
		long endTime;
		MutableBoolean condition;
		TaskPriority priority;
		
		ActionBarMessage(String text, long endTime, MutableBoolean condition, TaskPriority priority){
			this.text = text;
			this.endTime = endTime;
			this.condition = condition;
			this.priority = priority;
		}
		
	}
	
}

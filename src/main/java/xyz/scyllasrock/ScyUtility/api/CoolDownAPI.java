package xyz.scyllasrock.ScyUtility.api;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class makes tracking of cooldowns easy. Only supports non-persistent cooldowns (cooldowns will disappear
 * after a server restart).
 *
 * This API will never support persistent cooldowns. It should be handled by plugins separately.
 */
public class CoolDownAPI {
    private static Map<String, Long> cooldowns = new HashMap<>();

    /**
     * @param key : key of object to give cool down
     * @param milliseconds : time of cool down
     */
    public static void addCooldown(String key, long milliseconds) {
        cooldowns.put(key, System.currentTimeMillis() + milliseconds);
    }
    /**
     * @param key : key of object to give cool down
     * Checks if the object has a cool down
     */
    public static boolean hasCooldown(String key) {
        Long cooldown = cooldowns.get(key);
        if(cooldown == null || cooldown - System.currentTimeMillis() < 0) {
            cooldowns.remove(key);
            return false;
        }
        return true;
    }


    /**
     *
     * @param key
     * @return - previous ending time associated with this cooldown. Null if none.
     */
    @Nullable
    public static Long removeCooldown(String key) {
        return cooldowns.remove(key);
    }


    /**
     *
     * @param key
     * @return timestamp at which cooldown ends.
     */
    @Nullable
    public static Long getCooldown(String key) {
        return cooldowns.get(key);
    }
}

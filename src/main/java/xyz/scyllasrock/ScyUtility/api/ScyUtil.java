package xyz.scyllasrock.ScyUtility.api;

import java.nio.ByteBuffer;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Class for miscellaneous utilities
 * @author kylew
 *
 */
public class ScyUtil {


	/**
	 * SQL data type BINARY(16)
	 * @param uuid
	 * @return
	 */
	   public static byte[] uuidToBytes(final UUID uuid) {
	        return ByteBuffer.allocate(16).putLong(uuid.getMostSignificantBits()).putLong(uuid.getLeastSignificantBits()).array();
	    }

	    public static UUID uuidFromBytes(final byte[] bytes) {
	        if (bytes.length < 2) { throw new IllegalArgumentException("Byte array too small."); }
	        final ByteBuffer bb = ByteBuffer.wrap(bytes);
	        return new UUID(bb.getLong(), bb.getLong());
	    }


	    public static Location locationFromLong(final long l, @Nullable String worldName) {
	    	if(worldName != null)
	        return new Location(Bukkit.getWorld(worldName), (int)(l >> 38), (int)(l << 26 >> 52), (int)(l << 38 >> 38));

	    	//else
	    	return new Location(null, (int)(l >> 38), (int)(l << 26 >> 52), (int)(l << 38 >> 38));
	    }

	    public static Location locationFromLong(final long l, UUID worldUID) {

	    	return new Location(Bukkit.getWorld(worldUID), (int)(l >> 38), (int)(l << 26 >> 52), (int)(l << 38 >> 38));
	    }

	    public static long locationToLong(final Location l) {
	        return ((long) l.getBlockX() & 67108863) << 38 | ((long) l.getBlockY() & 4095) << 26 | ((long) l.getBlockZ() & 67108863);
	    }

}
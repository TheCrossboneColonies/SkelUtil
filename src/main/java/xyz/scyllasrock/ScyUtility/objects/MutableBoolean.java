package xyz.scyllasrock.ScyUtility.objects;

public class MutableBoolean {

	private byte value;

	public MutableBoolean(boolean value) {
		this.value = (byte) (value ? 1 : 0);
	}

	public boolean getBooleanValue() {
		return value == 1;
	}

	public void setBooleanValue(boolean value) {
		this.value = (byte) (value ? 1 : 0);
	}

}

package main;

public interface Peripheral {
	public void setMem(short address, short data);
	public short getMem(short address);
	public boolean inRange(short address);
}

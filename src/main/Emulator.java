package main;
import java.util.Arrays;

import java.util.ArrayList;
import java.util.List;

public class Emulator {
	private short a;
	private short d;
	private short pc;
	private short[] ram;
	private short[] instruction;
	private List<Peripheral> mapping;
	
	public Emulator(short[] instruction) {
		this.ram = new short[0x10000];
		this.pc = 0;
		this.a = 0;
		this.d = 0;
		this.instruction = Arrays.copyOf(instruction, 0x10000);
		this.mapping = new ArrayList<Peripheral>();
	}
	
	public void step() {
		short instr = this.getInstruction();
		if (instr >= 0) {
			this.a = instr;
			this.pc++;
		}
		else {
			
			boolean star = (((instr >> 12) & 1) != 0);
			boolean u = (((instr >> 10) & 1) != 0);
			boolean op1 = (((instr >> 9) & 1) != 0);
			boolean op0 = (((instr >> 8) & 1) != 0);
			boolean zx = (((instr >> 7) & 1) != 0);
			boolean sw = (((instr >> 6) & 1) != 0);
			
			short a = this.a;
			if (star) {
				a = this.getRam();
			}
			
			short result = alu(this.d, a, u, op0, op1, zx, sw);
			
			boolean setA = (((instr >> 5) & 1) != 0);
			boolean setD = (((instr >> 4) & 1) != 0);
			boolean setStarA = (((instr >> 3) & 1) != 0);
			
			boolean lt = (((instr >> 2) & 1) != 0);
			boolean eq = (((instr >> 1) & 1) != 0);
			boolean gt = (((instr >> 0) & 1) != 0);
			
			if (lt && (result < 0)) {
				this.pc = this.a;
			}
			else if (eq && (result == 0)) {
				this.pc = this.a;
			}
			else if (gt && (result > 0)) {
				this.pc = this.a;
			}
			else {
				this.pc++;
			}
			
			
			if (setStarA) {
				this.setRam(result);
			}
			if (setA) {
				this.a = result;
			}
			if (setD) {
				this.d = result;
			}
			
			
		}
	}
	
	private static short alu(short d, short a, boolean u, boolean op0, boolean op1, boolean zx, boolean sw) {
		short r;
		short x;
		short y;
		if (sw) {
			x = a;
			y = d;
		}
		else {
			x = d;
			y = a;
		}
		if (zx) {
			x = 0;
		}
		
		
		if (u) {
			if (op0) {
				y = 1;
			}
			if (op1) {
				r = (short) (x - y);
			}
			else {
				r = (short) (x + y);
			}
		}
		else {
			if (op1) {
				if (op0) {
					r = (short) (x | y);
				}
				else {
					r = (short) (x & y);
				}
			}
			else {
				if (op0) {
					r = (short) ~x;
				}
				else {
					r = (short) (x ^ y);
				}
			}
		}
		
		return r;
	}
	public String coreDump() {
		return ("A: " + this.a + "; D: " + this.d + "; PC: " + this.pc);
	}
	
	public short getRam(short a) {
		for (Peripheral block : mapping) {
			if (block.inRange(a)) {
				return block.getMem(a);
			}
		}
		return this.ram[a & 0xFFFF];
	}
	
	public short getRam() {
		return this.getRam(this.a);
	}
	
	public void setRam(short a, short r) {
		boolean hitPeripheral = false;
		for (Peripheral block : mapping) {
			if (block.inRange(a)) {
				block.setMem(a, r);
				hitPeripheral = true;
				break;
			}
		}
		if (!hitPeripheral) {
			this.ram[a & 0xFFFF] = r;
		}
	}
	
	public void setRam(short r) {
		this.setRam(this.a,r);
	}
	
	public short getInstruction(short pc) {
		return this.instruction[pc & 0xFFFF];
	}
	
	public short getInstruction() {
		return this.getInstruction(this.pc);
	}

	public short getA() {
		return a;
	}

	public short getD() {
		return d;
	}
	
	public void addMapping(int index, Peripheral block) {
		this.mapping.add(index, block);
	}
	
	public void addMapping(Peripheral block) {
		this.mapping.add(block);
	}
	public void removeMapping(int index) {
		this.mapping.remove(index);
	}
	

}

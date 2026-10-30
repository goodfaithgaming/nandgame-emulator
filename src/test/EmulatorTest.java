package test;
import main.Emulator;

public class EmulatorTest {

	public static void main(String[] args) {
		short[] program = {(short) 0x8090, 2, (short) 0x8510, (short) 0x8007};
		Emulator nand = new Emulator(program);
		nand.step();
		assert (nand.getD() == 0);
		assert (nand.getA() == 0);
		nand.step();
		assert (nand.getD() == 0);
		assert (nand.getA() == 2);
		nand.step();
		assert (nand.getD() == 1) : "D did not increment, D was " + nand.getD() + "; expected 1";
		assert (nand.getA() == 2);
		nand.step();
		nand.step();
		assert (nand.getD() == 2);
		System.out.println("Tests passed");
	}

}

package main;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ExecuteFile {

	public static void main(String[] args) {
		Path path = Path.of(args[0]); 
		
		
        try {
        	byte[] fileBytes = Files.readAllBytes(path);
            if (fileBytes.length % 2 != 0) {
            	
            }
            else {
            	short[] program = new short[fileBytes.length/2];
            	ByteBuffer.wrap(fileBytes)
                		  .order(ByteOrder.LITTLE_ENDIAN)
                		  .asShortBuffer()
                		  .get(program);
            	Emulator nand = new Emulator(program);
            	int runtime = Integer.parseInt(args[1]);
            	for (int i = 0; i < runtime; i++) {
            		nand.step();
            	}
            }
            
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        

		
	}

}

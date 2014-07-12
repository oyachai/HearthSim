package com.hearthsim;


import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.hearthsim.HearthSimMain;

public class HearthSimMain {

	public static void main(String[] args) {
		
		if (args.length < 1) {
			System.out.println("Usage: java -jar hearthsim.jar setupFilePath");
			System.exit(0);
		}
		
		Path setupFilePath = FileSystems.getDefault().getPath(args[0]);
		
		HearthSim sim = new HearthSimRandom();
		try {
			sim.setup(setupFilePath);
			sim.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
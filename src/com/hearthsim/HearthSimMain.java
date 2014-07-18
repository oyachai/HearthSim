package com.hearthsim;


import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.hearthsim.HearthSimMain;
import com.hearthsim.exception.HSException;

public class HearthSimMain {

	public static void main(String[] args) {
		
		if (args.length < 1) {
			System.out.println("Usage: java -jar hearthsim.jar setupFilePath");
			System.exit(0);
		}
		
		Path simParamFilePath = FileSystems.getDefault().getPath(args[0]);
		
		try {
			HearthSim sim = new HearthSimConstructed(simParamFilePath);
			sim.run();
		} catch (IOException e) {
			System.err.println("Can't find some files!");
			e.printStackTrace();
		} catch (HSException e) {
			System.err.println("Something went wrong with the simulation: " + e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.err.println("Interrupted");
			e.printStackTrace();
		}
	}

}
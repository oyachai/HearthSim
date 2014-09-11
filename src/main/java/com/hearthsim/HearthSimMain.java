package com.hearthsim;


import com.hearthsim.exception.HSException;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class HearthSimMain {

	public static void main(String[] args) {
        final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HearthSimMain.class);
		
		if (args.length < 1) {
			log.info("Usage: java -jar hearthsim.jar setupFilePath");
			System.exit(0);
		}
		
		Path simParamFilePath = FileSystems.getDefault().getPath(args[0]);
		
		try {
			HearthSimBase sim = new HearthSimConstructed(simParamFilePath);
			sim.run();
		} catch (IOException e) {
            log.error("Can't find some files! {}", e);
		} catch (HSException e) {
            log.error("Something went wrong with the simulation: {}" + e);
		} catch (InterruptedException e) {
            log.error("Interrupted: {}" + e);
		}
	}

}
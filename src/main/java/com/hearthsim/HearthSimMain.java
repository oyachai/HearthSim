package com.hearthsim;


import com.hearthsim.exception.HSException;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class HearthSimMain {

    public static void main(String[] args) {
        final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HearthSimMain.class);

        if (args.length < 1) {
            log.error("Usage: java -jar hearthsim.jar setupFilePath");
            System.exit(0);
        }

        String simDefinitionPath = args[0];
        log.info("starting sim from file: {}", simDefinitionPath);
        Path simParamFilePath = FileSystems.getDefault().getPath(simDefinitionPath);

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

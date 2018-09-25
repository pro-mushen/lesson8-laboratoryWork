package ru.innopolis.laboratoryWork.finishVariant;


import org.apache.log4j.Logger;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;


public class ProcessingFiles implements Runnable {

    private String pathSourcesFile;
    private ConcurrentLinkedQueue<String> finishList;
    private Pattern pattern;
    private static final Logger LOGGER = Logger.getLogger(ProcessingFiles.class);

    public ProcessingFiles(String pathSourcesFile, ConcurrentLinkedQueue finishList, Pattern pattern) {
        this.pathSourcesFile = pathSourcesFile;
        this.finishList = finishList;
        this.pattern = pattern;
    }

    public ProcessingFiles(String pathSourcesFile) {
    }

    private void addFoundOffers() {
        String offer;
        try {
            URLConnection connection = new URL(pathSourcesFile).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream()).useDelimiter("[.!?]");
            while (scanner.hasNext()) {
                offer = scanner.next();
                if (pattern.matcher(offer).find()){
                    finishList.add(offer);
                }
            }
        } catch (Exception e) {
            LOGGER.debug("could not read file " + pathSourcesFile);
        }
    }



    @Override
    public void run() {
        addFoundOffers();
    }

}


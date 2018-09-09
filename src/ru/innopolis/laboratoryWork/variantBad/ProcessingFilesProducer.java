package ru.innopolis.laboratoryWork.variantBad;


import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;


public class ProcessingFilesProducer implements Runnable {

    private String pathSourcesFile;
    private ConcurrentLinkedQueue resultCollection;


    public ProcessingFilesProducer(String pathSourcesFile, ConcurrentLinkedQueue resultCollection) {
        this.pathSourcesFile = pathSourcesFile;
        this.resultCollection = resultCollection;
    }

    public ProcessingFilesProducer(String pathSourcesFile) {
    }


    private void addFoundOffers() {

            try {
                URLConnection connection = new URL(pathSourcesFile).openConnection();
                Scanner scanner = new Scanner(connection.getInputStream()).useDelimiter("[.!?]");

                while (scanner.hasNext()) {
                    resultCollection.add(scanner.next());
                }
            } catch (Exception e) {
                System.out.println("Возникла проблема, при считывании исходных данных: " + pathSourcesFile);
            }
        System.out.println(resultCollection.size());
    }

    @Override
    public void run() {
        addFoundOffers();
    }

}


package ru.innopolis.laboratoryWork.variantBad;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;


public class FinderRealization implements Finder {

    private ConcurrentLinkedQueue<String> finishList = new ConcurrentLinkedQueue<>();
    ConcurrentLinkedQueue<String> offersCollection = new ConcurrentLinkedQueue();
    static final int COUNT_THREAD_PRODUCER = 1;
    static final int COUNT_THREAD_CONSUMER = 100;

    @Override
    public void getOccurencies(String[] sources, String[] words, String res) throws IOException {

        ExecutorService executorProcessingFiles = getAndSubmitExecutorProcessingFiles(sources);
        ExecutorService executorSearchOffers = getAndSubmitExecutorSearchOffers(words);

        waitAndInterrupt(executorProcessingFiles, executorSearchOffers);
        waitAndInterrupt(executorSearchOffers,null);

        System.out.println(finishList);
        write(finishList,res);

    }


    private ExecutorService getAndSubmitExecutorProcessingFiles(String[] sources){

        ExecutorService executorProcessingFiles = Executors.newFixedThreadPool(COUNT_THREAD_PRODUCER);
        for (String pathFile:sources) {
            executorProcessingFiles.submit(new ProcessingFilesProducer(pathFile,offersCollection));
        }
        executorProcessingFiles.shutdown();
        return executorProcessingFiles;
    }


    private ExecutorService getAndSubmitExecutorSearchOffers(String[] words){
        ExecutorService executorSearchOffers = Executors.newFixedThreadPool(COUNT_THREAD_CONSUMER);
        String textSearch = "";

        for (String str:words) {
            textSearch = textSearch + "(.*)\\b" + str + "\\b(.*)|";
        }
        textSearch = textSearch.substring(0,textSearch.length()-1);
        Pattern pattern = Pattern.compile(textSearch, Pattern.CASE_INSENSITIVE);


        for (int i = 0; i < COUNT_THREAD_CONSUMER; i++) {
            executorSearchOffers.submit(new SearchOffersConsumer(offersCollection,pattern,finishList));
        }

        return executorSearchOffers;
    }


    private void waitAndInterrupt(ExecutorService waitExecutor,ExecutorService interruptExecutor){
        if (waitExecutor!=null){
            while (!waitExecutor.isTerminated()){}
        }

        if (interruptExecutor!=null){
            interruptExecutor.shutdownNow();
        }
    }

    private void write(ConcurrentLinkedQueue<String> finishList, String pathFile) throws IOException {

        File file = new File(pathFile);

        if (file.exists() && finishList != null) {
            try (Writer writer = new FileWriter(file)){
                writer.write("");
                for (String offer : finishList) {
                    writer.write(offer);
                }
            } catch (IOException e) {
                IOException myException = new IOException("Возникла проблема, при записи результата. Проверьте корректность указанного пути.");
                myException.setStackTrace(e.getStackTrace());
                throw myException;
            }
        }

    }
}

package ru.innopolis.laboratoryWork.finishVariant;

import org.apache.log4j.Logger;

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
    static final int COUNT_THREAD = 10;
    private static final Logger LOGGER = Logger.getLogger(ProcessingFiles.class);


    @Override
    public void getOccurencies(String[] sources, String[] words, String res) throws IOException {

        ExecutorService executorProcessingFiles = getAndSubmitExecutorProcessingFiles(sources,words);
        waitExecutor(executorProcessingFiles);
        write(finishList,res);
    }


    private ExecutorService getAndSubmitExecutorProcessingFiles(String[] sources, String[] words){
        String textSearch = "";

        for (String str:words) {
            textSearch = textSearch + "(.*)\\b(?i)" + str + "\\b(.*)|";
        }

        textSearch = textSearch.substring(0,textSearch.length()-1);
        Pattern pattern = Pattern.compile(textSearch, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        ExecutorService executorProcessingFiles = Executors.newFixedThreadPool(COUNT_THREAD);
        for (String pathFile:sources) {
            executorProcessingFiles.submit(new ProcessingFiles(pathFile,finishList,pattern));
        }
        executorProcessingFiles.shutdown();
        return executorProcessingFiles;
    }


    private void waitExecutor(ExecutorService waitExecutor){
        if (waitExecutor!=null){
            while (!waitExecutor.isTerminated()){}
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
                LOGGER.error("Could not write result");
                IOException myException = new IOException("Возникла проблема, при записи результата. Проверьте корректность указанного пути.");
                myException.setStackTrace(e.getStackTrace());
                throw myException;
            }
        }

    }
}

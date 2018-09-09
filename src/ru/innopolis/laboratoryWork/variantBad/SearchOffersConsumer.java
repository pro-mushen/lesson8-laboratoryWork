package ru.innopolis.laboratoryWork.variantBad;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchOffersConsumer implements Runnable {

    private ConcurrentLinkedQueue<String> resultCollection;
    private Pattern pattern;
    ConcurrentLinkedQueue<String> finishList;


    public SearchOffersConsumer(ConcurrentLinkedQueue<String> resultCollection, Pattern pattern, ConcurrentLinkedQueue<String> finishList ) {
        this.resultCollection = resultCollection;
        this.pattern = pattern;
        this.finishList = finishList;
    }

    public SearchOffersConsumer() {
    }

    private void SearchOffers(){
        String str;
        while (resultCollection.size() > 0 || !Thread.currentThread().isInterrupted()) {
            str = resultCollection.poll();
            if (str != null){
                Matcher matcher = pattern.matcher(str);
                if (matcher.find()){
                    finishList.add(str + ";" + "\r\n");
                }
            }
        }

    }


    @Override
    public void run() {
        SearchOffers();
    }
}

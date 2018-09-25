package ru.innopolis.laboratoryWork.finishVariant;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Finder finder = new FinderRealization();
        long start = System.currentTimeMillis();

        String[] s = new String[201];

        for (int i = 1; i <= 100; i++) {
            s[i*2] = "file:///C:\\Test\\TestFile.txt";
            s[i * 2 - 1] = "file:///C:\\\\Test\\\\Vojna234324.txt";
        }

        try {
            finder.getOccurencies( s, new String[]{"война"}, "C:\\Test\\Res.txt");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(System.currentTimeMillis() - start);
    }
}

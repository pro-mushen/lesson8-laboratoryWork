package ru.innopolis.laboratoryWork.variantBad;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Finder finder = new FinderRealization();

        String[] s = new String[51];

        for (int i = 1; i <= 25; i++) {
            s[i*2] = "file:///C:\\Test\\TestFile.txt";
            s[i*2-1] =  "file:///C:\\Test\\Vojna.txt";
        }

        try {
            finder.getOccurencies(s, new String[]{"Nichego", "3tut"}, "C:\\Test\\Res.txt");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

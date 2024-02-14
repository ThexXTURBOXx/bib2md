package de.femtopedia.bib2md;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(Converter.convert(Files.readString(Path.of("coauth.bib"))));
    }

}

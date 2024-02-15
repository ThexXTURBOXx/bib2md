package de.femtopedia.bib2md;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Files.writeString(Path.of("theses.md"),
                "- " + String.join("\n- ", Converter.convert(
                        Files.readString(Path.of("theses.bib")))));
        Files.writeString(Path.of("own.md"),
                "- " + String.join("\n- ", Converter.convert(
                        Files.readString(Path.of("own.bib")))));
        Files.writeString(Path.of("coauth.md"),
                "- " + String.join("\n- ", Converter.convert(
                        Files.readString(Path.of("coauth.bib")))));
        Files.writeString(Path.of("others.md"),
                "- " + String.join("\n- ", Converter.convert(
                        Files.readString(Path.of("others.bib")))));
    }

}

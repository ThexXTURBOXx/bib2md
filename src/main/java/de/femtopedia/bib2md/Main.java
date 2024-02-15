package de.femtopedia.bib2md;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        Files.writeString(Path.of("theses.md"),
                "- " + String.join("\n- ", Converter.convert(
                        download(new URI("https://femtopedia.de/research/theses.bib")))));
        Files.writeString(Path.of("own.md"),
                "- " + String.join("\n- ", Converter.convert(
                        download(new URI("https://femtopedia.de/research/first_author.bib")))));
        Files.writeString(Path.of("coauth.md"),
                "- " + String.join("\n- ", Converter.convert(
                        download(new URI("https://femtopedia.de/research/co_author.bib")))));
        Files.writeString(Path.of("others.md"),
                "- " + String.join("\n- ", Converter.convert(
                        download(new URI("https://femtopedia.de/research/others.bib")))));
    }

    private static String download(URI uri) throws IOException {
        try (InputStream in = uri.toURL().openStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

}

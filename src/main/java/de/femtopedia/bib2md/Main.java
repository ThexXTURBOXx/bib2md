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
        String theses = "- " + String.join("\n- ", Converter.convert(
                download(new URI("https://femtopedia.de/research/theses.bib")),
                "unsrtnatOWN", "N\\. Mexis"));
        String own = "- " + String.join("\n- ", Converter.convert(
                download(new URI("https://femtopedia.de/research/first_author.bib")),
                "unsrtnatOWN", "N\\. Mexis"));
        String coauth = "- " + String.join("\n- ", Converter.convert(
                download(new URI("https://femtopedia.de/research/co_author.bib")),
                "unsrtnatOWN", "N\\. Mexis"));
        String others = "- " + String.join("\n- ", Converter.convert(
                download(new URI("https://femtopedia.de/research/others.bib")),
                "unsrtnatOWN", "N\\. Mexis"));

        Files.writeString(Path.of("out.md"),
                """
                        ## Theses
                        %s
                                                
                        ## First author
                        %s
                                                
                        ## Co-author
                        %s
                                                
                        ## Others
                        %s
                        """
                        .formatted(theses, own, coauth, others));
    }

    private static String download(URI uri) throws IOException {
        try (InputStream in = uri.toURL().openStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

}

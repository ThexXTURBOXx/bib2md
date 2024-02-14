package de.femtopedia.bib2md;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

public class Converter {

    private static final String TEX_FILE = "main.tex";
    private static final String BBL_FILE = "main.bbl";
    private static final String BST_FILE = "unsrtnatOWN.bst";
    private static final String BIB_FILE = "bibliography.bib";

    private static final String TEX_CONTENT = """
            \\documentclass{article}
            \\usepackage{hyperref}
            \\begin{document}
            \\nocite{*}
            \\bibliographystyle{unsrtnatOWN}
            \\bibliography{%s}
            \\end{document}
            """;

    public static String convert(String bib) throws IOException, InterruptedException {
        Path temp = randomTempDir();
        Files.createDirectories(temp);

        Path bibPath = temp.resolve(BIB_FILE);
        Files.writeString(bibPath, bib);

        Path bstPath = Path.of(BST_FILE);
        Files.copy(bstPath, temp.resolve(bstPath));

        Files.writeString(temp.resolve(TEX_FILE),
                TEX_CONTENT.formatted(BIB_FILE));

        var pb = new ProcessBuilder("latexmk", TEX_FILE);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        pb.directory(temp.toFile());
        Process p = pb.start();
        p.waitFor();
        String ret = Files.readString(temp.resolve(BBL_FILE));

        try (Stream<Path> walk = Files.walk(temp)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }

        return ret;
    }

    private static Path randomTempDir() {
        return Path.of("temp" + UUID.randomUUID());
    }

}

package de.femtopedia.bib2md;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Converter {

    private static final String PROJECT_NAME = "main";
    private static final String TEX_FILE = PROJECT_NAME + ".tex";
    private static final String BBL_FILE = PROJECT_NAME + ".bbl";
    private static final String BIB_FILE = "bibliography.bib";

    private static final String TEX_CONTENT = """
            \\documentclass{article}
            \\usepackage{hyperref}
            \\begin{document}
            \\nocite{*}
            \\bibliographystyle{%s}
            \\bibliography{%s}
            \\end{document}
            """;

    public static List<String> convert(String bib, String bibStyle, String... namesToEmph)
            throws IOException, InterruptedException {
        Set<String> emphNames = Arrays.stream(namesToEmph)
                .map(n -> n.replace(" ", "[\\s\u00a0]*"))
                .collect(Collectors.toSet());

        Path temp = randomTempDir();
        Files.createDirectories(temp);

        Path bibPath = temp.resolve(BIB_FILE);
        Files.writeString(bibPath, bib);

        Path bstPath = Path.of(bibStyle + ".bst");
        Files.copy(bstPath, temp.resolve(bstPath));

        Files.writeString(temp.resolve(TEX_FILE),
                TEX_CONTENT.formatted(bibStyle, BIB_FILE));

        //startProcessAndWait(temp.toFile(), "latexmk", TEX_FILE);
        startProcessAndWait(temp.toFile(), "pdflatex", TEX_FILE);
        startProcessAndWait(temp.toFile(), "bibtex", PROJECT_NAME);
        String rawBbl = Files.readString(temp.resolve(BBL_FILE));

        try (Stream<Path> walk = Files.walk(temp)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }

        rawBbl = rawBbl.substring(rawBbl.indexOf("\\bibitem"),
                rawBbl.indexOf("\\end{thebibliography}")).trim();

        List<String> ret = new ArrayList<>();
        for (String bibItem : rawBbl.split("(?=\\\\bibitem)")) {
            bibItem = bibItem.trim();

            // Remove \bibitem[...]{...}
            bibItem = LatexProcessor.consumeOptionalArg(bibItem, 0);
            bibItem = LatexProcessor.consumeRequiredArg(bibItem, 0);

            bibItem = LatexProcessor.latexToMd(bibItem);

            for (String name : emphNames)
                bibItem = bibItem.replaceAll("(" + name + ")", "**$1**");

            ret.add(bibItem);
        }

        return ret;
    }

    private static Path randomTempDir() {
        return Path.of("temp-" + UUID.randomUUID());
    }

    private static void startProcessAndWait(File dir, String... cmd) throws IOException, InterruptedException {
        var pb = new ProcessBuilder(cmd);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        pb.directory(dir);
        Process p = pb.start();
        p.waitFor();
    }

}

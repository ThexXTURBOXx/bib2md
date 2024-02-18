package de.femtopedia.bib2md;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LatexProcessor {

    public static String latexToMd(String latex) {
        latex = latex.replace("\r", "");
        latex = latex.replace("\n", " ");
        latex = latex.replace("\t", " ");
        latex = latex.replace("\\newblock", "");
        latex = latex.replace("--", "-");
        latex = latex.replace("~", "\u00a0");
        latex = latex.replace("\\&", "&");
        latex = latex.replace("\\\"{a}", "ä");
        latex = latex.replace("\\\"{A}", "Ä");
        latex = latex.replace("\\\"{e}", "ë");
        latex = latex.replace("\\\"{E}", "Ë");
        latex = latex.replace("\\\"{i}", "ï");
        latex = latex.replace("\\\"{I}", "Ï");
        latex = latex.replace("\\\"{o}", "ö");
        latex = latex.replace("\\\"{O}", "Ö");
        latex = latex.replace("\\\"{u}", "ü");
        latex = latex.replace("\\\"{U}", "Ü");
        latex = latex.replace("\\'{a}", "á");
        latex = latex.replace("\\'{A}", "Á");
        latex = latex.replace("\\'{e}", "é");
        latex = latex.replace("\\'{E}", "É");
        latex = latex.replace("\\'{i}", "í");
        latex = latex.replace("\\'{I}", "Í");
        latex = latex.replace("\\'{o}", "ó");
        latex = latex.replace("\\'{O}", "Ó");
        latex = latex.replace("\\'{u}", "ú");
        latex = latex.replace("\\'{U}", "Ú");
        latex = latex.replace("\\^{a}", "â");
        latex = latex.replace("\\^{A}", "Â");
        latex = latex.replace("\\^{e}", "ê");
        latex = latex.replace("\\^{E}", "Ê");
        latex = latex.replace("\\^{i}", "î");
        latex = latex.replace("\\^{I}", "Î");
        latex = latex.replace("\\^{o}", "ô");
        latex = latex.replace("\\^{O}", "Ô");
        latex = latex.replace("\\^{u}", "û");
        latex = latex.replace("\\^{U}", "Û");
        latex = latex.replace("\\`{a}", "à");
        latex = latex.replace("\\`{A}", "À");
        latex = latex.replace("\\`{e}", "è");
        latex = latex.replace("\\`{E}", "È");
        latex = latex.replace("\\`{i}", "ì");
        latex = latex.replace("\\`{I}", "Ì");
        latex = latex.replace("\\`{o}", "ò");
        latex = latex.replace("\\`{O}", "Ò");
        latex = latex.replace("\\`{u}", "ù");
        latex = latex.replace("\\`{U}", "Ù");
        latex = latex.replace("``", "\\\"");
        latex = latex.replace("''", "\\\"");
        latex = latex.replace("`", "\\'");
        latex = latex.replace("'", "\\'");
        latex = latex.replaceAll("\\\\penalty\\d+\\s*", "");
        latex = transformLatexCommand(latex, "\\emph", 0, 1, "*$1*");
        latex = transformLatexCommand(latex, "\\textit", 0, 1, "*$1*");
        latex = transformLatexCommand(latex, "\\textbf", 0, 1, "**$1**");
        latex = transformLatexCommand(latex, "\\texttt", 0, 1, "`$1`");
        latex = transformLatexCommand(latex, "\\url", 0, 1, "[`$1`]($1)");
        latex = transformLatexCommand(latex, "\\href", 0, 2, "[$2]($1)");
        latex = latex.replace("{", "");
        latex = latex.replace("}", "");
        latex = latex.replaceAll(" +", " ");
        return latex;
    }

    public static String transformLatexCommand(String latex, String cmd,
                                               int optArgs, int reqArgs,
                                               String to) {
        while (true) {
            int startIndex = latex.indexOf(cmd);
            if (startIndex == -1) return latex;
            List<Map.Entry<Integer, Integer>> args = new ArrayList<>();
            int lastEnd = startIndex;
            for (int i = 0; i < optArgs; ++i) {
                int start = latex.indexOf('[', lastEnd);
                int end = endOfOptionalArg(latex, start);
                args.add(new AbstractMap.SimpleEntry<>(start + 1, end));
                lastEnd = end;
            }
            for (int i = 0; i < reqArgs; ++i) {
                int start = latex.indexOf('{', lastEnd);
                int end = endOfRequiredArg(latex, start);
                args.add(new AbstractMap.SimpleEntry<>(start + 1, end));
                lastEnd = end;
            }

            String localReplacer = to;
            for (int i = 0; i < args.size(); ++i) {
                var e = args.get(i);
                localReplacer = localReplacer.replace("$" + (i + 1),
                        latex.substring(e.getKey(), e.getValue()));
            }

            StringBuilder buf = new StringBuilder(latex);
            buf.replace(startIndex, lastEnd, localReplacer);
            latex = buf.toString();
        }
    }

    public static int endOfOptionalArg(String latex, int startIndex) {
        boolean wasOptOpen = false;
        int optsOpen = 0;
        int i;
        for (i = startIndex; i < latex.length(); ++i) {
            char c = latex.charAt(i);
            if (c == '[') {
                wasOptOpen = true;
                ++optsOpen;
            }
            if (c == ']')
                --optsOpen;
            if (wasOptOpen && optsOpen <= 0)
                break;
        }
        return i;
    }

    public static String consumeOptionalArg(String latex, int startIndex) {
        return latex.substring(endOfOptionalArg(latex, startIndex) + 1).trim();
    }

    public static int endOfRequiredArg(String latex, int startIndex) {
        boolean wasOptOpen = false;
        int optsOpen = 0;
        int i;
        for (i = startIndex; i < latex.length(); ++i) {
            char c = latex.charAt(i);
            if (c == '{') {
                wasOptOpen = true;
                ++optsOpen;
            }
            if (c == '}')
                --optsOpen;
            if (wasOptOpen && optsOpen <= 0)
                break;
        }
        return i;
    }

    public static String consumeRequiredArg(String latex, int startIndex) {
        return latex.substring(endOfRequiredArg(latex, startIndex) + 1).trim();
    }

}

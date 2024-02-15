package de.femtopedia.bib2md;

public class LatexProcessor {

    public static String latexToMd(String latex) {
        latex = latex.replace("\r", "");
        latex = latex.replace("\n", " ");
        latex = latex.replace("\\newblock", "");
        latex = latex.replace("--", "-");
        latex = latex.replace("~", " ");
        latex = latex.replace("\\&", "&");
        latex = latex.replace("``", "\"");
        latex = latex.replace("''", "\"");
        latex = latex.replace("\\\"{a}", "ä");
        latex = latex.replace("\\\"{A}", "Ä");
        latex = latex.replace("\\\"{o}", "ö");
        latex = latex.replace("\\\"{O}", "Ö");
        latex = latex.replace("\\\"{u}", "ü");
        latex = latex.replace("\\\"{U}", "Ü");
        latex = latex.replace("\\'{a}", "á");
        latex = latex.replace("\\'{A}", "Á");
        latex = latex.replace("\\'{o}", "ó");
        latex = latex.replace("\\'{O}", "Ó");
        latex = latex.replace("\\'{u}", "ú");
        latex = latex.replace("\\'{U}", "Ú");
        latex = latex.replaceAll("\\\\penalty\\d+", "");
        latex = latex.replaceAll("\\\\emph\\{(.+?)}", "*$1*");
        latex = latex.replaceAll("\\\\texttt\\{(.+?)}", "`$1`");
        latex = latex.replaceAll("\\\\href\\{(.+?)}\\{(.+?)}", "[$2]($1)");
        latex = latex.replaceAll("\\\\url\\{(.+?)}", "$1");
        latex = latex.replace("{", "");
        latex = latex.replace("}", "");
        latex = latex.replaceAll(" +", " ");
        return latex;
    }

}

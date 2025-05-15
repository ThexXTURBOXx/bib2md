# bib2md

Easily transform BibTeX files into Markdown files!

Example: <https://nmexis.me/#research-and-publications>

> [!NOTE]
> I have mainly written this tool to format my CV-ish website "automagically."
> If I encounter bugs, I fix them - however, there may be bugs that I have not experienced yet with my BibTeX files.
> If you encounter such a bug, feel free to either open an issue or fork the project and create a PR with your fix!

## Prerequisites

- Java >=17 installation
- Working LaTeX installation (also make sure to properly set up your PATH environment variable!)

## Setup

1. Clone this repository
2. Edit [`src/main/java/de/femtopedia/bib2md/Main.java`](src/main/java/de/femtopedia/bib2md/Main.java) to your liking
   (right now, it contains example code that I use for my website)
3. Run `mvn package` to compile the project
4. Run the jar file that should be within `target/bib2md-1.0-SNAPSHOT.jar`

## I get a crash like `NoSuchFileException: unsrtnatOWN.bst`

You need to either provide a custom bib style in the `Main` class or use my unsrtnat-derivative:

1. Download the [original `unsrtnat.bib`](https://satztexnik.com/tex-archive/macros/latex/contrib/natbib/unsrtnat.bst)
   to the project root
2. Apply the patch from [`cv-unsrtnat.patch`](cv-unsrtnat.patch) to it

## How does this work?

The entire implementation consists of two classes and uses *no external libraries*. Here is a short explanation:

`Converter`:

- Creates a temporary LaTeX project folder with a tiny document template
- Compiles that project using `pdflatex` and `bibtex`, which yields a `bbl` file
- Reads the content of that `bbl` file
- Removes the temporary project folder
- Hands over the content of the `bbl` file to the `LatexProcessor`

`LatexProcessor`:

- Parses each `\bibitem` entry within the `bbl` file
- Many common symbols and command sequences are easily transformed to Markdown through `replace[All]`
- Some others require more sophisticated conversions (through `transformLatexCommand`)
- The transformed string is then passed back to the `Converter`, which forwards it to `Main`

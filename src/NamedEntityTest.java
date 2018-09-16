import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamedEntityTest {
    final static private String INPUT_FILE = "input.txt";
    final static private String MODEL_FILE = "models/en-ner-location.bin";

    public static void main(final String[] args) throws Exception {
        final String document = readInput(INPUT_FILE);
        final String[] tokens = tokenize(document);

        try (final InputStream modelIn = new FileInputStream(MODEL_FILE)) {
            final NameFinderME nameFinder = new NameFinderME(new TokenNameFinderModel(modelIn));

            final Span[] spans = nameFinder.find(tokens);
            for (final Span span : spans) {
                System.out.printf("Span(%d, %d, %s, %f)=\"%s\"\n", span.getStart(), span.getEnd(), span.getType(), span.getProb(), str(tokens, span));
            }
        }
    }

    private static String readInput(final String filename) {
        final StringBuilder sb = new StringBuilder();

        try (final BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                sb.append(sCurrentLine);
            }

            return sb.toString();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static String[] tokenize(final String document) {
        return document.split("\\s+");
    }

    private static String str(final String[] tokens, final Span span) {
        final List<String> location = new ArrayList<>(Arrays.asList(tokens).subList(span.getStart(), span.getEnd()));
        return String.join(" ", location);
    }
}

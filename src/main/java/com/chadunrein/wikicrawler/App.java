package com.chadunrein.wikicrawler;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.chadunrein.wikicrawler.crawler.WikiCrawler;
import com.chadunrein.wikicrawler.processor.HtmlDocumentProcessor;

/**
 * Main application class.
 */
public final class App {
    private static final String ERROR_USAGE = "Usage: com.chadunrein.wikicrawler.App <string: wikipedia page title> <int: link traversal depth>";
    private static final String WIKIPEDIA_BASE_PATH = "https://en.wikipedia.org/wiki/";

    private App() {
    }

    /**
     * Application entry point.
     *
     * @param args The arguments of the program
     */
    public static void main(String[] args) {
        int k = 0;

        if (args.length != 2) {
            System.err.println(ERROR_USAGE);
            System.exit(1);
        } else {
            try {
                k = Integer.parseInt(args[1]);
            } catch(NumberFormatException e) {
                System.err.println(ERROR_USAGE);
                System.exit(1);
            }
        }

        HtmlDocumentProcessor documentProcessor = new HtmlDocumentProcessor();
        WikiCrawler wikiCrawler = new WikiCrawler(documentProcessor);
        wikiCrawler.crawl(WIKIPEDIA_BASE_PATH + args[0], k);

        // Sort and show results
        Map<Integer, Long> counts = documentProcessor.getCounts();
        Map<Integer,Long> top4Counts =
            counts.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                                      .limit(4)
                                      .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        int i = 1;
        System.out.println("Rank, Letter, Count");
        for (Map.Entry<Integer, Long> entry : top4Counts.entrySet()) {
            System.out.printf("%d, %c, %d\n", i++, entry.getKey(), entry.getValue());
        }
        System.out.println("\nall counts");
        for (Map.Entry<Integer, Long> entry : counts.entrySet()) {
            System.out.printf("%d, %c, %d\n", i++, entry.getKey(), entry.getValue());
        }
    }
}

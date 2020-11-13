package com.chadunrein.wikicrawler.crawler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Set;

import com.chadunrein.wikicrawler.processor.DocumentProcessor;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WikiCrawler {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:82.0) Gecko/20100101 Firefox/82.0";
    private static final String CONTENT_TYPE_TEXT = "text/html";
    private static final String SELECTOR_QUERY = "a[href]";

    private final Set<String> visitedPages = new HashSet<>();
    private final DocumentProcessor<Document> documentProcessor;

    public WikiCrawler(DocumentProcessor<Document> documentProcessor) {
        this.documentProcessor = documentProcessor;
    }

    /**
     * Crawl the web starting at the given URL and to the provided depth. The first provided URL will be considered
     * to be at a depth of 0.
     *
     * @param url The URL of the web page to retrieve the contents of
     * @param depth The desired depth of link traversal
     */
    public void crawl(String url, int depth) {
        if (depth < 0 || url.isEmpty() || !visitedPages.add(url)) {
            return;
        }

        Document htmlDocument = fetchDocument(url);
        if (htmlDocument == null) {
            return;
        }

        documentProcessor.process(htmlDocument);

        Elements links = htmlDocument.select(SELECTOR_QUERY);

        for (Element link : links) {
            // ignore anchors since they don't point to new content
            if (link.attr("href").startsWith("#")) {
                continue;
            }
            crawl(link.absUrl("href"), depth - 1);
        }
    }

    /**
     * Fetch the HTML document at the provided URL.
     *
     * @param url The URL of the web page to fetch
     * @return The HTML document at the provided URL or null if anything prevents it from being fetched
     */
    private Document fetchDocument(String url) {
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            if (connection.response().statusCode() == HttpURLConnection.HTTP_OK) {
                // Only process URLs that point to text documents
                if (connection.response().contentType() != null &&
                        connection.response().contentType().contains(CONTENT_TYPE_TEXT)) {
                    return htmlDocument;
                }
            } else {
                // TODO add logging
                return null;
            }
        } catch (IOException e) {
            // TODO add logging
        }
        return null;
    }
}

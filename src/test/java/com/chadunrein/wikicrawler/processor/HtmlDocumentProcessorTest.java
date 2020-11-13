package com.chadunrein.wikicrawler.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

class HtmlDocumentProcessorTest {
    @Test
    void testProcess() {
        // Document.createShell will create a document that looks like this:
        // <html><head></head><body></body></html>
        HtmlDocumentProcessor documentProcessor = new HtmlDocumentProcessor();
        Document document = Document.createShell("");
        document.text("2hh 3aaa 4bbbbbb 5CCCCC 8eeeeeeee"); // sets body text
        documentProcessor.process(document);
        Map<Integer, Long> counts = documentProcessor.getCounts();
        // expecting 10 e's (ASCII 101), 8 b's (ASCII 98), 5 C's (ASCII 67), 7 h's (ASCII 104), 2 t's (ASCII 116)
        assertEquals(counts.get(101), 10L);
        assertEquals(counts.get(98), 8L);
        assertEquals(counts.get(104), 6L);
        assertEquals(counts.get(67), 5L);
        assertEquals(counts.get(116), 2L);

        // process same doc again and verify doubled counts
        documentProcessor.process(document);
        counts = documentProcessor.getCounts();
        assertEquals(counts.get(101), 20L);
        assertEquals(counts.get(98), 16L);
        assertEquals(counts.get(104), 12L);
        assertEquals(counts.get(67), 10L);
        assertEquals(counts.get(116), 4L);
    }
}

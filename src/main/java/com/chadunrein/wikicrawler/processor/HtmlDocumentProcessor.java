package com.chadunrein.wikicrawler.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;

public class HtmlDocumentProcessor implements DocumentProcessor<Document> {
    Map<Integer, Long> totalCounts = new HashMap<>();

    @Override
    public void process(Document document) {
        String text = document.outerHtml();

        Map<Integer, Long> letterCount =
            text.chars()
                .filter(c -> ((c >= 65 && c <= 90) || (c >= 97 && c <= 122)))
                .boxed()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for (Map.Entry<Integer, Long> entry : letterCount.entrySet()) {
            totalCounts.merge(entry.getKey(), entry.getValue(), Long::sum);
        }
    }

    @Override
    public Map<Integer, Long> getCounts() {
        return totalCounts;
    }
}

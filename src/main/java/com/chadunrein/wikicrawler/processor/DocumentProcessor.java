package com.chadunrein.wikicrawler.processor;

import java.util.Map;

public interface DocumentProcessor<T> {
    void process(T document);

    Map<Integer, Long> getCounts();
}

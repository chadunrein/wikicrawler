The program can be written in the language of your choice. It needs to perform the following functions:
1. The program takes a wikipedia article as its only argument,
2. It recursively explores all follows all hyperlinks in that wikipedia article to a depth of k
3. It reads the contents of each link it encounters
4. It outputs the four most common letters a-zA-Z that appear in all the webpages it reads and the count of each of these letters,

The program should have a few simple unit tests which verifies it meets these requirements. Note cycles are possible, so the program should detect if it has already visited a webpage and only count that webpage once

For example:
$ python countlettersinwikipedia.py TypeScript 3
it would then crawl all text links from the wikipedia article https://en.wikipedia.org/wiki/TypeScript to a depth of 3 and output something like the following

Rank, Letter, Count

1, e, 23423  
2, E, 1999  
3, a, 1543  
4, i,  213  
package com.lbraka.sasebackend.services;

import com.lbraka.sasebackend.model.Page;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Lauris on 22/05/2017.
 */
@Service
public class ChapterService {

    private final int MAX_LINES_PER_PAGE = 35;

    private final int MAX_CHAR_PER_LINES = 60;

    public List<Page> buildChapterPages(String content) {
        List<Page> pages = new ArrayList<>();
        Elements paragraphs = Jsoup.parse(content).getElementsByTag("p");
        Elements lines = new Elements();
        int remainingLines = MAX_LINES_PER_PAGE;
        for(Element paragraph : paragraphs) {
            String textOfParagraph = paragraph.text();
            int nbLinesInParagraph = (textOfParagraph.length() / MAX_CHAR_PER_LINES) + 1;
            if(nbLinesInParagraph <= remainingLines) {
                lines.add(paragraph);
                remainingLines -= nbLinesInParagraph;
            } else {
                int maxCharactersAllowed = remainingLines * MAX_CHAR_PER_LINES;
                String includedParagraph = "";
                boolean isHtml = false;
                boolean hasOpenTag = false;
                char[] signs = paragraph.toString().toCharArray();
                String currentTag = "";
                int nbSignsIncluded = 0;
                for(int i = 0; i < signs.length; ++i) {
                    char currentSign = paragraph.toString().charAt(i);
                    if(currentSign == '<' && String.valueOf(signs[i + 1]).matches("[a-z]")) {
                        isHtml = true;
                        hasOpenTag = true;
                    }
                    if(isHtml && hasOpenTag) {
                        currentTag = currentTag.concat(Character.toString(currentSign));
                    }
                    if(currentSign == '<' && String.valueOf(signs[i + 1]).matches("/")) {
                        hasOpenTag = false;
                    }
                    includedParagraph = includedParagraph.concat(Character.toString(currentSign));
                    if(!isHtml) {
                        nbSignsIncluded++;
                    }
                    if(currentSign == '>') {
                        isHtml = false;
                    }
                    if(nbSignsIncluded == maxCharactersAllowed) {
                        break;
                    }
                }

                String remainingText = paragraph.toString().substring(includedParagraph.length());
                if(!includedParagraph.endsWith(" ")) {
                    remainingText = includedParagraph.substring(includedParagraph.lastIndexOf(" ") + 1).concat(remainingText);
                    includedParagraph = includedParagraph.substring(0, includedParagraph.lastIndexOf(" "));
                }
                if(hasOpenTag) {
                    remainingText = currentTag.concat(remainingText);
                }
                if(!remainingText.startsWith("<p")) {
                    remainingText = includedParagraph.substring(includedParagraph.indexOf("<"), includedParagraph.indexOf(">") + 1).concat(remainingText);
                }

                lines.add(Jsoup.parse(includedParagraph).getElementsByTag("p").first());
                lines = addNewPage(pages, lines, Jsoup.parse(remainingText).getElementsByTag("p").first());
                remainingLines = lines.isEmpty() ? MAX_LINES_PER_PAGE : MAX_LINES_PER_PAGE - (lines.get(0).text().length() / MAX_CHAR_PER_LINES) + 1;
            }
            if(remainingLines == 0) {
                lines = addNewPage(pages, lines, null);
                remainingLines = MAX_LINES_PER_PAGE;
            }
        }
        if(remainingLines != 0) {
            addNewPage(pages, lines, null);
        }
        return pages;
    }

    private Elements addNewPage(List<Page> pages, Elements lines, Element firstElement) {
        pages.add(new Page(null, pages.size() + 1, lines.toString(), null));
        lines = new Elements();
        if(firstElement != null) {
            lines.add(firstElement);
        }
        return lines;
    }

    private int countLengthOfHtml(String content) {
        return Jsoup.parse(content).children().stream()
                .map(child -> "<" + child.tagName() + " " + child.attributes().asList().stream().map(attr -> attr.toString()).collect(Collectors.joining(" ")) + ">")
                .collect(Collectors.joining())
                .length();
    }
}

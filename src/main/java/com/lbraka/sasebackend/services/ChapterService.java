package com.lbraka.sasebackend.services;

import com.lbraka.sasebackend.model.Chapter;
import com.lbraka.sasebackend.model.Page;
import com.lbraka.sasebackend.repositories.ChapterRepository;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by Lauris on 22/05/2017.
 */
@Service
public class ChapterService {

    private final int MAX_LINES_PER_PAGE = 30;

    private final int ABSOLUTE_MAX_CHAR_PER_LINES = 68;

    private final int MAX_CHAR_PER_LINES = 60;

    private final int MAX_EXTRA_WORD_LENGTH = 4;

    @Autowired
    private ChapterRepository chapterRepo;

    public Chapter createNewChapter(Chapter newChapter, String chapterContent, Chapter previousChapter) {
        Chapter savedChapter = chapterRepo.save(newChapter);
        if(previousChapter != null) {
            savedChapter.setPreviousChapterId(previousChapter.getId());
            previousChapter.setNextChapterId(savedChapter.getId());

        }
        savedChapter.setPages(buildChapterPages(chapterContent, savedChapter.getId()));
        return savedChapter;
    }

    public List<Page> buildChapterPages(String content, Long chapterId) {
        List<Page> pages = new ArrayList<>();
        Integer currentNbLines = 0;
        Integer currentLineSize = 0;
        boolean isHtml;
        boolean openedTag = false;
        String currentTag = "";
        StringJoiner currentLine = new StringJoiner(" ");
        StringJoiner currentPageContent = new StringJoiner("<br />");
        content = formatInputText(content);
        String[] paragraphes = content.split("</p>");
        for(String paragraph : paragraphes) {
            String[] words = paragraph.replace("<p>", "")
                    .replace("<p ", "<span ")
                    .replace("</p>", "</span>")
                    .concat("\n")
                    .split(" ");
            for(int i = 0; i < words.length; ++i) {
                String currentWord = words[i];
                if(currentWord.isEmpty()) continue;
                isHtml = currentWord.startsWith("<");
                if(isHtml) {
                    while(!currentWord.endsWith(">")) {
                        if(i + 1 < words.length) {
                            currentWord = currentWord.concat(" ").concat(words[++i]);
                        }
                    }
                    if(!currentWord.startsWith("</") && !currentWord.endsWith("/>")) {
                        openedTag = true;
                        currentTag = currentWord;
                    } else {
                        openedTag = false;
                        currentTag = "";
                    }
                }
                if(currentWord.contains("<img")) {
                    // TODO correct img size
                    String pageContent = currentPageContent.toString();
                    if(openedTag) {
                        int tagSize = currentTag.contains(" ") ? currentTag.indexOf(" ") : currentTag.lastIndexOf(">");
                        pageContent = pageContent.concat("</").concat(currentTag.substring(1, tagSize));
                        currentLine.add(currentTag);
                    }
                    if(!pageContent.isEmpty()) addNewPage(pageContent, pages, chapterId);
                    currentPageContent = addNewPage(currentWord, pages, chapterId);
                    currentNbLines = 0;
                    continue;
                }
                if(currentLineSize + 1 <= MAX_CHAR_PER_LINES) {
                    if(currentLineSize + getCurrentWordLength(currentWord) > ABSOLUTE_MAX_CHAR_PER_LINES) {
                        currentLine = addNewLine(currentLine, currentPageContent);
                        currentLineSize = 0;
                        currentNbLines++;
                    }
                    addWordToLine(currentLine, currentWord);
                    if(!isHtml) currentLineSize += getCurrentWordLength(currentWord);
                    if (currentWord.endsWith("\n")) {
                        currentLine = addNewLine(currentLine, currentPageContent);
                        currentLineSize = 0;
                        currentNbLines++;
                    }
                } else {
                    if(wordHasPunctuation(currentWord)  && newWordFit(currentWord, currentLineSize)) {
                        currentLine = addWordAndNewLine(currentLine, currentWord, currentPageContent);
                        currentLineSize = 0;
                        currentNbLines++;
                    } else if(canAddNextWord(words, i+1, currentLineSize + getCurrentWordLength(currentWord))) {
                        addWordToLine(currentLine, currentWord);
                        currentLine = addWordAndNewLine(currentLine, words[++i], currentPageContent);
                        currentLineSize = 0;
                        currentNbLines++;
                    } else if(i > 0 && wordHasPunctuation(words[i - 1])) {
                        currentLine = addNewLine(currentLine, currentPageContent);
                        currentLineSize = 0;
                        currentNbLines++;
                        i--;
                    } else {
                        currentLine = addNewLine(currentLine, currentPageContent);
                        currentLineSize = 0;
                        currentNbLines++;
                        addWordToLine(currentLine, currentWord);
                        if(!isHtml) currentLineSize += getCurrentWordLength(currentWord);
                    }
                }
                if(currentNbLines == MAX_LINES_PER_PAGE) {
                    String pageContent = currentPageContent.toString();
                    if(openedTag) {
                        int tagSize = currentTag.contains(" ") ? currentTag.indexOf(" ") : currentTag.lastIndexOf(">");
                        pageContent = pageContent.concat("</").concat(currentTag.substring(1, tagSize));
                        currentLine.add(currentTag);
                    }
                    currentPageContent = addNewPage(pageContent, pages, chapterId);
                    currentNbLines = 0;
                }
            }
        }
        if(!currentLine.toString().isEmpty() || !currentPageContent.toString().isEmpty()) {
            currentPageContent.add(currentLine.toString());
            String pageContent = currentPageContent.toString();
            if(openedTag) {
                int tagSize = currentTag.contains(" ") ? currentTag.indexOf(" ") : currentTag.lastIndexOf(">");
                pageContent = pageContent.concat("</").concat(currentTag.substring(1, tagSize)).concat(">");
                currentLine.add(currentTag);
            }
            addNewPage(pageContent, pages, chapterId);
        }
        return pages;
    }

    private boolean newWordFit(String word, int lineLength) {
        return lineLength + word.length() + 1 <= ABSOLUTE_MAX_CHAR_PER_LINES;
    }

    private void addWordToLine(StringJoiner line, String word) {
        line.add(word);
    }

    private boolean wordHasPunctuation(String word) {
        return word.endsWith("(.,;!?");
    }
    
    private StringJoiner addWordAndNewLine(StringJoiner line, String word, StringJoiner pageContent) {
        addWordToLine(line, word);
        return addNewLine(line, pageContent);
    }

    private StringJoiner addNewLine(StringJoiner line, StringJoiner pageContent) {
        String lineContent = line.toString();
        if(lineContent.equals("\n")) {
            lineContent = "";
        }
        pageContent.add(lineContent);
        return new StringJoiner(" ");
    }

    private boolean canAddNextWord(String[] words, int cpt, int lineLenght) {
        return cpt < words.length
                && newWordFit(words[cpt], lineLenght)
                && !words[cpt].equals("\n")
                && (wordHasPunctuation(words[cpt])
                    || words[cpt].length() <= MAX_EXTRA_WORD_LENGTH);
    }

    private StringJoiner addNewPage(String pageContent, List<Page> pages, Long chapterId) {
        if(!pageContent.toString().replace(" ", "").replace("\n", "").replace("<br />", "").replace("<br/>", "").isEmpty()) {
            pages.add(new Page(null, pages.size() + 1, formatPageContent(pageContent), chapterId));
        }
        return new StringJoiner("<br />");
    }

    private String formatPageContent(String pageContent) {
        pageContent = pageContent.replace("?", " ?")
                .replace("!", " !")
                .replace("--", "-- ");
        if(pageContent.startsWith("<br />")) {
            pageContent.replaceFirst("<br />", "");
        }
        return pageContent;
    }

    private String formatInputText(String input) {
        return StringEscapeUtils.unescapeHtml4(input
                .replace("<br />", "")
                .replace("<pre>", "<p>")
                .replace("</pre>", "</p>")
                .replaceAll("\n(?=\\S)", " \n ")
                .replaceAll(">(?=\\S)", "> ")
                .replaceAll("<(?<=\\S)", " <")
                .replaceAll("(\\s\\?)", "?")
                .replaceAll("(\\s!)", "!")
                .replace("-- ", "--"));
    }

    private int getCurrentWordLength(String currentWord) {
        return !currentWord.equals("\n") ? currentWord.length() + 1 : 0;
    }
}

package com.example.solocarry.model;

/**
 * This is Text Message class which extends the Message class. It represents
 * the type of message that users can use to communicate text information.
 * Each LocationMessage contains contains a string to represent the message.
 */
public class TextMessage extends Message{
    private String content;

    /**
     * The constructor of a TextMessage object,it accepts a content string
     * as parameter.
     * @param content the content we want to set
     */
    public TextMessage(String content) {
        this.content = content;
    }
}

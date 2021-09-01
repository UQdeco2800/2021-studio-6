package com.deco2800.game.components.dialoguebox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DialogueTest {

    Dialogue dialogue;
    ArrayList<String> list;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>();
        list.add("Hello World1");
        list.add("World Hello2");
        list.add("Crying World3");
        list.add("Box Boy4");
        list.add("Boy Box5");

        dialogue = new Dialogue(list);
    }

    @Test
    void next() {
        assertEquals(list.get(1), dialogue.next());
        assertEquals(list.get(2), dialogue.next());
        assertEquals(list.get(3), dialogue.next());
        assertEquals(list.get(4), dialogue.next());
        assertEquals(list.get(4), dialogue.next());
    }

    @Test
    void hasNext() {
        assertTrue(dialogue.hasNext());
        dialogue.next();
        dialogue.next();
        dialogue.next();
        dialogue.next();
        assertFalse(dialogue.hasNext());
    }

    @Test
    void getCurrentDialogue() {
        assertEquals(list.get(0), dialogue.getCurrentDialogue());
        dialogue.next();
        assertEquals(list.get(1), dialogue.getCurrentDialogue());
    }
}
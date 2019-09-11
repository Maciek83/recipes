package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.Note;
import com.mgosciminski.recipe.model.NoteDto;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NoteToNoteDtoTest {

    private NoteToNoteDto converter;

    @Before
    public void setUp() throws Exception {

        converter = new NoteToNoteDto();
    }

    @Test
    public void convertNull() throws Exception{
        NoteDto noteDto = converter.convert(null);

        assertNull(noteDto);
    }

    @Test
    public void convert() throws Exception {

        //given
        Note note = new Note();
        note.setNotes("notes");

        //when
        NoteDto noteDto = converter.convert(note);

        //then
        assertNotNull(noteDto);
        assertEquals(noteDto.getNotes(),"notes");

    }
}
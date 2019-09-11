package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.Note;
import com.mgosciminski.recipe.model.NoteDto;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NoteDtoToNoteTest {

    NoteDtoToNote converter;

    @Before
    public void setUp() throws Exception {

        converter = new NoteDtoToNote();
    }

    @Test
    public void convertNull() throws Exception
    {
        Note noteNull = converter.convert(null);

        assertNull(noteNull);
    }

    @Test
    public void convert() throws Exception
    {
        //given
        NoteDto noteDto = new NoteDto();
        noteDto.setNotes("my notes");

        Note noteConverted = converter.convert(noteDto);

        assertNotNull(noteConverted);
        assertEquals(noteDto.getNotes(),noteConverted.getNotes());
    }
}
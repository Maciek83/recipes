package com.mgosciminski.recipe.service;


import com.mgosciminski.recipe.converter.NoteDtoToNote;
import com.mgosciminski.recipe.domain.Note;
import com.mgosciminski.recipe.model.NoteDto;
import com.mgosciminski.recipe.repository.NoteRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class NoteServiceImplTest {

    @Mock
    NoteRepository noteRepository;

    @Mock
    NoteDtoToNote converter;

    @InjectMocks
    NoteServiceImpl noteService;

    private NoteServiceImpl noteServiceSpy;

    private Set<Note> notes = new HashSet<>();

    private final String BAD = "bad";

    @Before
    public void setup()
    {
        Note note = new Note();
        note.setNotes("n");
        notes.add(note);
        Note note1 = new Note();
        note1.setNotes("n2");
        notes.add(note1);

        noteServiceSpy = Mockito.spy(noteService);
    }

    @Test
    public void findAll() throws Exception {

        //when
        when(noteRepository.findAll()).thenReturn(notes);
        Set<Note> result = (Set<Note>) noteService.findAll();

        //then
        assertNotNull(result);
        assertEquals(result.size(),2);

        verify(noteRepository).findAll();
    }

    @Test
    public void findById() throws Exception {

        //given
        Optional<Note> optionalNote = Optional.of(new Note());

        //when
        when(noteRepository.findById(anyLong())).thenReturn(optionalNote);
        Note result = noteService.findById(1L);

        //then
        assertNotNull(result);
        assertEquals(optionalNote.get(),result);

        verify(noteRepository).findById(anyLong());
    }

    @Test
    public void testFindByIdNull() throws Exception
    {
        //given
        Optional<Note> emptyNote = Optional.empty();

        //when
        when(noteRepository.findById(anyLong())).thenReturn(emptyNote);
        Note result = noteService.findById(1L);

        //then
        assertNotNull(result);
        assertEquals(result.getNotes(),BAD);

        verify(noteRepository).findById(anyLong());
    }

    @Test
    public void saveNoteDto() throws Exception {
        //given
        Note note = new Note();

        //when
        when(noteRepository.save(any())).thenReturn(note);
        Note result = noteService.save(new NoteDto());

        //then
        assertNotNull(result);
        assertEquals(note,result);

        verify(noteRepository).save(any());
    }

    @Test
    public void saveNote() throws Exception
    {
        //given
        Note note = new Note();

        //when
        when(noteRepository.save(any())).thenReturn(note);
        Note result = noteService.save(new Note());

        //then
        assertNotNull(result);
        assertEquals(note,result);

        verify(noteRepository).save(any());
    }

    @Test
    public void editNote() throws Exception {
        //given
        Note note = new Note();
        note.setNotes("notes");

        //when
        doReturn(note).when(noteServiceSpy).findById(anyLong());
        Note result = noteServiceSpy.findById(1L);
        result.setNotes("notesUpdate");
        doReturn(result).when(noteServiceSpy).save(any(Note.class));
        Note savedNote = noteServiceSpy.save(result);


        //then
        assertEquals(note,result);
        assertEquals(result,savedNote);
        assertEquals(note.getNotes(),"notesUpdate");
        assertEquals(savedNote.getNotes(),"notesUpdate");

        verify(noteServiceSpy).findById(anyLong());
        verify(noteServiceSpy).save(any(Note.class));
    }
}
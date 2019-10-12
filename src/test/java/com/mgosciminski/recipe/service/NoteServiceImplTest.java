package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Note;
import com.mgosciminski.recipe.repository.NoteRepository;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    @InjectMocks
    NoteServiceImpl noteService;

    NoteServiceImpl noteServiceSpy;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    Set<Note> noteSet = new HashSet<>();
    private final String NOT_FOUND = "can't find this id";

    @Before
    public void setup() {
        noteSet.add(new Note());

        noteServiceSpy = Mockito.spy(noteService);
    }

    @Test
    public void findAll() {

        //given
        when(noteRepository.findAll()).thenReturn(noteSet);

        //when
        Set<Note> result = (Set<Note>) noteService.findAll();

        //then
        assertNotNull(result);
        assertEquals(result, noteSet);
        assertEquals(result.size(), 1);
        verify(noteRepository).findAll();
    }

    @Test
    public void findByIdExist() throws NotFoundException {

        //given
        Note note = new Note();
        when(noteRepository.findById(anyLong())).thenReturn(Optional.of(note));

        //when
        Note result = noteService.findById(anyLong());

        //then
        assertNotNull(result);
        assertEquals(note, result);
        verify(noteRepository).findById(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void findBiIdEmpty() throws NotFoundException {
        //given
        when(noteRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        noteService.findById(anyLong());

        //then
        verify(noteRepository).findById(anyLong());
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(NOT_FOUND);

    }

    @Test
    public void saveNoIdNote() throws NotFoundException {
        //given
        Note note = new Note();
        when(noteRepository.save(note)).thenReturn(note);

        //when
        Note result = noteServiceSpy.save(note);

        //then
        assertNotNull(result);
        assertEquals(result,note);
        verify(noteRepository).save(any());
    }

    @Test(expected = NotFoundException.class)
    public void saveNoteWithIdNotPresent() throws NotFoundException {
        //given
        Note note = new Note();
        note.setId(1L);
        doThrow(new NotFoundException(NOT_FOUND)).when(noteServiceSpy).findById(anyLong());

        //when
        Note result = noteServiceSpy.save(note);

        //then
        verify(noteServiceSpy).findById(anyLong());
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(NOT_FOUND);
    }

    @Test
    public void saveNoteWithIdPresent() throws NotFoundException {
        //given
        Note note = new Note();
        note.setId(1L);
        doReturn(note).when(noteServiceSpy).findById(anyLong());
        when(noteRepository.save(any())).thenReturn(note);

        //when
        Note result = noteServiceSpy.save(note);

        //then
        assertNotNull(result);
        assertEquals(result,note);
        verify(noteServiceSpy).findById(anyLong());
        verify(noteRepository).save(any());
    }

    @Test
    public void delete() {

        //when
        noteService.deleteById(anyLong());

        //then
        verify(noteRepository).deleteById(anyLong());
    }
}
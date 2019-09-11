package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Note;
import com.mgosciminski.recipe.model.NoteDto;

public interface NoteService {
    Iterable<Note> findAll();
    Note findById(Long id);
    Note save(NoteDto noteDto);
    Note save(Note note);
    Note editNote(NoteDto note);
}

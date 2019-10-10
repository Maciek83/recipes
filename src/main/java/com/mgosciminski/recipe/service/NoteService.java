package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Note;

public interface NoteService {
    Iterable<Note> findAll();
    Note findById(Long id);
    Note save(Note note);
}

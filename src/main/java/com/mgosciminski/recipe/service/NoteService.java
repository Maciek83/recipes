package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Note;
import javassist.NotFoundException;


public interface NoteService {
    Iterable<Note> findAll();
    Note findById(Long id) throws NotFoundException;
    Note save(Note note) throws NotFoundException;
    void delete(Long id);
}

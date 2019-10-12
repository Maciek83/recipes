package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Note;
import com.mgosciminski.recipe.repository.NoteRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final String NOT_FOUND = "can't find this id";


    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Iterable<Note> findAll() { return noteRepository.findAll(); }

    @Override
    public Note findById(Long id) throws NotFoundException {

        return noteRepository.findById(id).orElseThrow(()-> new NotFoundException(NOT_FOUND));
    }

    @Override
    public Note save(Note note) throws NotFoundException {

        if (note.getId() != null)
        {
            Note fromDb = findById(note.getId());
            fromDb.setDescription(note.getDescription());
            return noteRepository.save(fromDb);
        }

        return noteRepository.save(note);
    }

    @Override
    public void deleteById(Long id) {

        noteRepository.deleteById(id);
    }

}

package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Note;
import com.mgosciminski.recipe.repository.NoteRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;


    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Iterable<Note> findAll() { return noteRepository.findAll(); }

    @Override
    public Note findById(Long id) throws NotFoundException {

        return noteRepository.findById(id).orElseThrow(()->new NotFoundException("not found"));
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
    public void delete(Long id) {
        noteRepository.deleteById(id);
    }

}

package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Note;
import com.mgosciminski.recipe.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    private Note nullObject = new Note();
    private final String BAD = "bad";

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Iterable<Note> findAll() { return noteRepository.findAll(); }

    @Override
    public Note findById(Long id) {

        Optional<Note> optionalNote = noteRepository.findById(id);

        if (optionalNote.isPresent())
        {
            return optionalNote.get();
        }
        else
        {
            nullObject.setNotes(BAD);
            return nullObject;
        }
    }

    @Override
    public Note save(Note note) {
        return noteRepository.save(note);
    }

}

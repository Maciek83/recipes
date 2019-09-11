package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.Note;
import com.mgosciminski.recipe.model.NoteDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NoteToNoteDto implements Converter<Note, NoteDto> {

    @Nullable
    @Override
    public synchronized NoteDto convert(Note note) {

        if(note == null)
        {
            return null;
        }

        NoteDto noteDto = new NoteDto();
        noteDto.setNotes(note.getNotes());

        return noteDto;
    }
}

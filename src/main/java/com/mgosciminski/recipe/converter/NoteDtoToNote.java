package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.Note;
import com.mgosciminski.recipe.model.NoteDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NoteDtoToNote implements Converter<NoteDto, Note> {

    @Nullable
    @Override
    public synchronized Note convert(NoteDto noteDto) {

        if(noteDto == null) {
            return null;
        }

        Note note = new Note();
        note.setNotes(noteDto.getNotes());
        return note;
    }
}

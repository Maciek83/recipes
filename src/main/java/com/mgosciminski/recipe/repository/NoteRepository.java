package com.mgosciminski.recipe.repository;

import com.mgosciminski.recipe.domain.Note;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends CrudRepository<Note,Long> {
}

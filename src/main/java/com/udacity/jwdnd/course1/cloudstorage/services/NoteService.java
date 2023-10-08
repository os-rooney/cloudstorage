package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getNotes(Integer userId) {
        return noteMapper.getNotes(userId);
    }

    public void saveNote(Note note) {
        noteMapper.saveNote(note);
    }

    public void updateNote(Note note) {
        noteMapper.updateNote(note);
    }
    public void deleteNoteById(Integer noteId, Integer userId) {
        noteMapper.deleteFileById(noteId, userId);
    }
}

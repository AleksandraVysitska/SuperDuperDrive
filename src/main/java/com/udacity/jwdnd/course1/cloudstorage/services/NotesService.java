package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class NotesService {

    private NotesMapper notesMapper;

    public NotesService(NotesMapper notesMapper) {
        this.notesMapper = notesMapper;
    }

    public List<Note> getAllNotes(int userid) throws Exception {
        List<Note> notes = notesMapper.findByUserId(userid);
        if (notes == null) {
            throw new Exception();
        }
        return notes;
    }

    public boolean addNote(Note note, int userid) {
        note.setUserid(userid);
        Integer result = notesMapper.insertNote(note);

        return result > 0;
    }

    public boolean updateNote(Note note) {
        Integer result = notesMapper.updateNote(note);

        return result > 0;
    }

    public void deleteNote(int noteid) {
        notesMapper.deleteNote(noteid);
    }

    public boolean checkNote(Note note){
        String title = notesMapper.findTitle(note.getNotetitle());
        String description = notesMapper.findDescription(note.getNotedescription());

        if (title!=null|| description!=null) return true;

        return false;
    }


}

package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractNotes;
import org.ctdworld.propath.interactor.InteractorNotes;
import org.ctdworld.propath.model.Notes;

import java.util.List;


public abstract class PresenterNotes implements ContractNotes.Presenter, ContractNotes.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterNotes.class.getSimpleName();
    ContractNotes.Interactor interactor;
    ContractNotes.View listener;

    public PresenterNotes(Context context, ContractNotes.View view)
    {
        this.interactor = new InteractorNotes(this, context);
        this.listener = view;
    }


    @Override
    public void onCategoryCreated(Notes notes) {
        listener.onCategoryCreated(notes);
    }

    @Override
    public void onCategoryDeleted(Notes notes) {
        listener.onCategoryDeleted(notes);
    }

    @Override
    public void onReceivedAllCategory(Notes notes) {
        listener.onReceivedAllCategory(notes);
    }

    @Override
    public void onNoteCreated(Notes notes) {
        listener.onNoteCreated(notes);
    }

    @Override
    public void onNoteDeleted(Notes notes) {
        listener.onNoteDeleted(notes);
    }

    @Override
    public void onNoteShared(Notes notes) {
        listener.onNoteShared(notes);
    }

    @Override
    public void onNoteCopy(String notes_id, String title) {
        listener.onNoteCopy(notes_id,title);
    }

    @Override
    public void onReceivedAllNotesOfCategory(Notes notes) {
        listener.onReceivedAllNotesOfCategory(notes);
    }

    @Override
    public void onReceivedNoteData(Notes notes) {
        listener.onReceivedNoteData(notes);
    }

    @Override
    public void onReceivedAllNotes(Notes notes) {
        listener.onReceivedAllNotes(notes);
    }

    @Override
    public void onNoteEdited(Notes notes) {
        listener.onNoteEdited(notes);
    }

    @Override
    public void onFailed(String message) {
        listener.onFailed(message);
    }





    @Override
    public void createCategory(Notes notes) {
        interactor.createCategory(notes);
    }

    @Override
    public void deleteCategory(Notes notes) {
        interactor.deleteCategory(notes);
    }



    @Override
    public void getAllCategory(Notes notes) {
        interactor.getAllCategory(notes);
    }

    @Override
    public void createNote(Notes notes) {
        interactor.createNote(notes);
    }



    @Override
    public void deleteNote(Notes notes) {
        interactor.deleteNote(notes);
    }


    @Override
    public void shareNote(Notes notes, String[] groupIdArray, String[] userIdArray) {
        interactor.shareNote(notes, groupIdArray, userIdArray);
    }

    @Override
    public void copyNote(String note_id, String title) {
        interactor.copyNote(note_id,title);
    }


    @Override
    public void getAllNotesOfCategory(Notes notes) {
        interactor.getAllNotesOfCategory(notes);
    }

    @Override
    public void getNoteData(Notes notes) {
        interactor.getNoteData(notes);
    }

    @Override
    public void getAllNotes(Notes notes) {
        interactor.getAllNotes(notes);
    }

    @Override
    public void editNote(Notes notes, Notes addNotes, Notes deleteNotes, List<Notes.Link> editLinkList) {
        interactor.editNote(notes, addNotes, deleteNotes, editLinkList);
    }
}

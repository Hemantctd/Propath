package org.ctdworld.propath.contract;


import org.ctdworld.propath.model.Notes;

import java.util.List;


/*# this contract class will be used in view, create, edit note pages*/
public interface ContractNotes
{
    interface Interactor
    {


        // # onFinishedListener
        interface OnFinishedListener
        {
            void onCategoryCreated(Notes notes);  // to create new category
            void onCategoryDeleted(Notes notes);
            void onReceivedAllCategory(Notes notes);   //all categories, create by user
            void onNoteCreated(Notes notes);
            void onNoteDeleted(Notes notes);
            void onNoteShared(Notes notes);
            void onNoteCopy(String notes_id, String title);
            void onReceivedAllNotesOfCategory(Notes notes);
            void onReceivedNoteData(Notes notes);  // single note data
            void onReceivedAllNotes(Notes notes);  // to get all notes, self created and shared by someone else
            void onNoteEdited(Notes notes);  // to edit note, only self created note can be edited, not shared by other
            void onFailed(String message);
            void onSuccess(String msg);// this method will be used if there is any issue.
            void onHideProgress();
            void onSetViewsEnabledOnProgressBarGone();
            void onShowMessage(String connection_error);
        }


        // # interactor
        void createCategory(Notes notes);  // to create new category
        void deleteCategory(Notes notes);
        void getAllCategory(Notes notes);
        void createNote(Notes notes);
        void deleteNote(Notes notes);
        void copyNote(String notes_id, String title);
        void shareNote(Notes notes, String[] groupIdArray, String[] userIdArray);
        void getAllNotesOfCategory(Notes notes);
        void getNoteData(Notes notes);
        void getAllNotes(Notes notes);  // to get all notes, self created and shared by someone else
        void editNote(Notes notes, Notes addNotes, Notes deleteNotes, List<Notes.Link> editLinkList);  // to edit note, only self created note can be edited, not shared by other

    }


    // # view
    interface View
    {
        void onCategoryCreated(Notes notes);  // to create new category
        void onCategoryDeleted(Notes notes);
        void onReceivedAllCategory(Notes notes);   //all categories, create by user
        void onNoteCreated(Notes notes);
        void onNoteDeleted(Notes notes);
        void onNoteShared(Notes notes);
        void onNoteCopy(String notes_id, String title);
        void onReceivedAllNotesOfCategory(Notes notes);
        void onReceivedNoteData(Notes notes);  // single note data
        void onReceivedAllNotes(Notes notes);  // to get all notes, self created and shared by someone else
        void onNoteEdited(Notes notes);  // to edit note, only self created note can be edited, not shared by other
        void onFailed(String message);    // this method will be used if there is any issue.
    }

    // # presenter
    interface Presenter
    {
        void createCategory(Notes notes);  // to create new category
        void deleteCategory(Notes notes);
        void getAllCategory(Notes notes);
        void createNote(Notes notes);
        void copyNote(String notes_id, String title);
        void deleteNote(Notes notes);
        void shareNote(Notes notes, String[] groupIdArray, String[] userIdArray);
        void getAllNotesOfCategory(Notes notes);
        void getNoteData(Notes notes);
        void getAllNotes(Notes notes);  // to get all notes, self created and shared by someone else
        void editNote(Notes notes, Notes addNotes, Notes deleteNotes, List<Notes.Link> editLinkList);  // to edit note, only self created note can be edited, not shared by other
    }
}

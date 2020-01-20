package org.ctdworld.propath.interactor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.ctdworld.propath.contract.ContractNotes;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.model.Notes;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


// this interactor will be used for view, create and edit page
public class InteractorNotes implements ContractNotes.Interactor {
    private static final String TAG = InteractorNotes.class.getSimpleName();
    private OnFinishedListener mListener;
    private Context mContext;
    private RemoteServer mRemoteServer;


    public InteractorNotes(OnFinishedListener listener, Context context) {
        this.mListener = listener;
        this.mContext = context;
        this.mRemoteServer = new RemoteServer(context);
    }


    // # creating new category
    @Override
    public void createCategory(final Notes notes) {
        Map<String, String> params = new HashMap<>();
        params.put("add_notes_category", "1");
        params.put("user_id", notes.getCreatedByUserId());
        params.put("title", notes.getCategoryName());

        Log.i(TAG, "params to create category = " + params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(final String message) {
                Log.i(TAG, "server message to create Note , " + message);
                new HandleCreateCategoryResponse().execute(message);   // parsing json
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                Log.e(TAG, "error while creating category , " + error.getMessage());
                mListener.onFailed("Creating category failed");
                error.printStackTrace();
            }
        });
    }


    // to delete category, all notes of deleted category will be uncategorized.
    @Override
    public void deleteCategory(final Notes notes) {
        Log.i(TAG, "deleting category");
        if (notes == null) {
            Log.e(TAG, "notes is null in deleteCategory");
            return;
        }
        Log.i(TAG, "deleteCategory() method called");
        Map<String, String> params = new HashMap<>();
        params.put("delete_notes_category", "1");
        params.put("cat_id", notes.getCategoryId());


        Log.i(TAG, "params to delete Category = " + params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(final String message) {
                Log.i(TAG, "server message to delete Category , " + message);
                if (message == null)
                    return;

                try {
                    JSONObject jsonObject = new JSONObject(message);

                    if ("1".equals(jsonObject.getString("success"))) {
                        mListener.onCategoryDeleted(notes);
                    } else
                        mListener.onFailed(jsonObject.getString("message"));

                } catch (JSONException e) {
                    Log.e(TAG, "error while deleting Category , " + e.getMessage());
                    e.printStackTrace();
                    mListener.onFailed(null);
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                Log.e(TAG, "error while deleting Category , " + error.getMessage());
                mListener.onFailed("Deleting category failed");
                error.printStackTrace();
            }
        });
    }


    // # getting all categories created by user
    @Override
    public void getAllCategory(Notes notes) {

        Map<String, String> params = new HashMap<>();
        params.put("get_notes_category", "1");
        params.put("user_id", notes.getCreatedByUserId());

        Log.i(TAG, "params to get all categories created by user = " + params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onVolleySuccessResponse(final String message) {
                Log.i(TAG, "remote message to get all categories  = " + message);
                new HandleGetAllCategoryResponse().execute(message);   // parsing json
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                mListener.onFailed("Creating category failed");
                error.printStackTrace();
                Log.e(TAG, "error while getting list of employment tools");
            }
        });

    }

    // creating new Note
    @Override
    public void createNote(Notes notes) {
        Log.i(TAG, "creating notes");
        String uuid = UUID.randomUUID().toString();
        try {
            Log.i(TAG, "getUserId() = " + notes.getCreatedByUserId());
            Log.i(TAG, "getCategoryId() = " + notes.getCategoryId());
            Log.i(TAG, "getListLinks().size() = " + notes.getListLinks().size());
            Log.i(TAG, "getTitle() = " + notes.getTitle());
            Log.i(TAG, "getDescription() = " + notes.getDescription());
            Log.i(TAG, "getListMedia().size() = " + notes.getListMedia().size());
            Log.i(TAG, "getListDocument().size() = " + notes.getListDocument().size());

            MultipartUploadRequest requestUpload = new MultipartUploadRequest(mContext, uuid, RemoteServer.URL);

            //  for (Notes.Media media : notes.getMediaList())

            requestUpload.addParameter("create_note", "1");
            requestUpload.addParameter("category_id", notes.getCategoryId());
            requestUpload.addParameter("title", notes.getTitle());
            requestUpload.addParameter("description", notes.getDescription());
            requestUpload.addParameter("user_id", notes.getCreatedByUserId());

            // # adding images
            if (notes.getListMedia() != null) {
                for (Notes.Media media : notes.getListMedia())
                    requestUpload.addFileToUpload(media.getMediaUrl(), "img_files[]");
            }

            // requestUpload.addFileToUpload("", "videos[]"); // adding videos

            // # adding documents
            if (notes.getListDocument() != null) {
                for (Notes.Document document : notes.getListDocument()) {
                    Log.i(TAG, "document path = " + document.getDocumentUrl());
                    requestUpload.addFileToUpload(document.getDocumentUrl(), "documents[]");
                }
            }

            // # adding link
            if (notes.getListLinks() != null) {
                for (Notes.Link link : notes.getListLinks())
                    requestUpload.addParameter("links[]", link.getLinkUrl());
            }


            requestUpload.setNotificationConfig(new UploadNotificationConfig());
            requestUpload.setMaxRetries(3);
            requestUpload.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {
                    Log.i(TAG, "creating note , progress = " + uploadInfo.getProgressPercent());
                }

                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                    mListener.onFailed(null);
                    exception.printStackTrace();
                }

                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                    String response = serverResponse.getBodyAsString().toString();
                    Log.i(TAG, "is response null = " + (response == null));
                    Log.i(TAG, "server response to create Note = " + response);
                    Log.i(TAG, "is response empty = " + response.isEmpty());

                    ArrayList<String> uploadedFiles = uploadInfo.getSuccessfullyUploadedFiles();
                    for (String string : uploadedFiles)
                        Log.i(TAG, "successfully uploaded file = " + string);

                    Log.i(TAG, "uploaded file size = " + uploadedFiles.size());

                    Notes notes1 = new Notes();
                    // notes1.setMedia(new Notes.Media());
                    mListener.onNoteCreated(notes1);
                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {
                    Log.i(TAG, "creating note cancelled , progress = " + uploadInfo.getProgressPercent());
                    // DialogHelper.showSimpleCustomDialog(mContext,"Cancelled...");
                }
            });

            // # uploading data
            requestUpload.startUpload();


        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException in createNote() method = " + e.getMessage());
            e.printStackTrace();
            mListener.onFailed(null);
        } catch (MalformedURLException e) {
            mListener.onFailed(null);
            Log.e(TAG, "MalformedURLException in createNote() method = " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void deleteNote(final Notes notes) {
        if (notes == null) {
            Log.e(TAG, "notes is null in deleteNote");
            return;
        }
        Log.i(TAG, "deleteNote() method called");
        Map<String, String> params = new HashMap<>();
        params.put("delete_note", "1");
        params.put("id", notes.getNoteId());


        Log.i(TAG, "params to delete note = " + params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(final String message) {
                Log.i(TAG, "server message to delete note , " + message);
                if (message == null)
                    return;

                try {
                    JSONObject jsonObject = new JSONObject(message);

                    if ("1".equals(jsonObject.getString("success"))) {
                        mListener.onNoteDeleted(notes);
                    } else
                        mListener.onFailed(jsonObject.getString("message"));

                } catch (JSONException e) {
                    Log.e(TAG, "error while deleting note , " + e.getMessage());
                    e.printStackTrace();
                    mListener.onFailed(null);
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                Log.e(TAG, "error while deleting note , " + error.getMessage());
                mListener.onFailed("Deleting note failed");
                error.printStackTrace();
            }
        });
    }

    @Override
    public void copyNote(String notes_id, String title) {
        Log.i(TAG,"copySurvey() method called ");

        Map<String,String> params = new HashMap<>();

        params.put("copy_note", "1");
        params.put("note_id",notes_id);
        params.put("note_title",title);
        params.put("user_id",SessionHelper.getInstance(mContext).getUser().getUserId());

        Log.i(TAG, "params to copy note = " + params);

        mRemoteServer.sendData(RemoteServer.URL,params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
                Log.i(TAG,"server message to copy note = "+message);

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message.trim());

                    if ("1".equals(jsonObject.getString("res")))
                        mListener.onSuccess("Note Copied Successfully");
                    else
                        mListener.onFailed("Failed");
                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in copyNote() method , "+e.getMessage());
                    e.printStackTrace();
                    mListener.onFailed("Failed");
                }
                finally {
                    mListener.onHideProgress();
                    mListener.onSetViewsEnabledOnProgressBarGone();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {

                Log.d(TAG,"copyNote() method volley error = "+error.getMessage());
                error.printStackTrace();
                mListener.onHideProgress();
                mListener.onShowMessage("Connection Error");
                mListener.onSetViewsEnabledOnProgressBarGone();

            }
        });
    }

    // # sharing note
    @Override
    public void shareNote(final Notes notes, String[] groupIdArray, String[] userIdArray) {
        if (notes == null) {
            Log.e(TAG, "notes is null in shareNote() method");
            return;
        }

        if (notes.getNoteId() == null) {
            Log.e(TAG, "notes id is null in shareNote() method");
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("share_notes", "1");
        params.put("notes_id", notes.getNoteId());
        params.put("group_id[]", Arrays.toString(groupIdArray));
        params.put("user_id[]", Arrays.toString(userIdArray));

        Log.i(TAG, "params to share note = " + params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onVolleySuccessResponse(final String message) {
                Log.i(TAG, "remote message to share note  = " + message);
                try {
                    if (message == null)
                        return;
                    JSONObject jsonObject = new JSONObject(message);
                    if ("1".equalsIgnoreCase(jsonObject.getString("success")))
                        mListener.onNoteShared(notes);
                    else
                        mListener.onFailed("Note not shared");
                } catch (JSONException e) {
                    e.printStackTrace();
                    mListener.onFailed("Note not shared");
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                mListener.onFailed("Sharing note failed");
                error.printStackTrace();
                Log.e(TAG, "error while sharing note");
            }
        });
    }

    // to get all notes of a particular category
    @Override
    public void getAllNotesOfCategory(Notes notes) {
        Map<String, String> params = new HashMap<>();
        params.put("get_all_notes_incategory", "1");
        params.put("cat_id", notes.getCategoryId());
        params.put("user_id", notes.getCreatedByUserId());

        Log.i(TAG, "params to get all notes of a category = " + params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onVolleySuccessResponse(final String message) {
                Log.i(TAG, "remote message to get all notes of a category  = " + message);
                new HandleGetAllNotesOfCategoryResponse().execute(message);   // parsing json
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                mListener.onFailed("Getting all notes failed.");
                error.printStackTrace();
                Log.e(TAG, "error while getting list of notes of a category");
            }
        });
    }


    @Override
    public void getNoteData(Notes notes) {
        Map<String, String> params = new HashMap<>();
        params.put("get_notes_details", "1");
        params.put("notes_id", notes.getNoteId());

        Log.i(TAG, "params to get note data = " + params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onVolleySuccessResponse(final String message) {
                Log.i(TAG, "remote message to get note data  = " + message);
                new HandleGetNoteDataResponse().execute(message);   // parsing json and updating views
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                mListener.onFailed("getting note data");
                error.printStackTrace();
                Log.e(TAG, "error while getting note data");
            }
        });
    }

    // # to get all notes
    @Override
    public void getAllNotes(Notes notes) {
        Map<String, String> params = new HashMap<>();
        params.put("get_notes_list", "1");
        params.put("user_id", notes.getCreatedByUserId());

        Log.i(TAG, "params to get all notes = " + params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onVolleySuccessResponse(final String message) {
                Log.i(TAG, "remote message to get all notes  = " + message);
                new HandleGetAllNotesResponse().execute(message);   // parsing json
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                mListener.onFailed("getting all notes failed");
                error.printStackTrace();
                Log.e(TAG, "error while getting all notes");
            }
        });
    }


    // editing note, addNotes parameter contains notes to be added, deleteNote parameter contains notes to be deleted
    @Override
    public void editNote(Notes notes, Notes addNotes, Notes deleteNotes, List<Notes.Link> editLinkList) {
        Log.i(TAG, "editing notes");
        String uuid = UUID.randomUUID().toString();
        try {
            Log.i(TAG, "notes_id = " + notes.getNoteId());
            Log.i(TAG, "edit_category_id = " + notes.getCategoryId());
            Log.i(TAG, "edit_title = " + notes.getTitle());
            Log.i(TAG, "edit_description = " + notes.getDescription());

            // converting editLinkList to array to pass as string

            String editLinkKeyArr = "";
            String editLinkValueArr = "";
            if (editLinkList != null) {
                String[] keyArr = new String[editLinkList.size()];
                String[] valueArr = new String[editLinkList.size()];
                for (int i = 0; i < editLinkList.size(); i++) {
                    Notes.Link link = editLinkList.get(i);

                    keyArr[i] = link.getLinkId();
                    valueArr[i] = link.getLinkUrl();

                    //  Map<String,String> params = new HashMap<>();
                    //  params.put("id", link.getLinkId());
                    //  params.put("link", link.getLinkUrl());

                }
                editLinkKeyArr = Arrays.toString(keyArr);
                editLinkValueArr = TextUtils.join(",", Arrays.asList(valueArr));  // creating comma separated value as string
            } else
                Log.e(TAG, "editLinkList is null in editNote() method");
            Log.i(TAG, "edit_links" + editLinkKeyArr + " = " + editLinkValueArr);

            MultipartUploadRequest requestUpload = new MultipartUploadRequest(mContext, uuid, RemoteServer.URL);

            //  for (Notes.Media media : notes.getMediaList())

            requestUpload.addParameter("edit_notes", "1");
            requestUpload.addParameter("edit_category_id", notes.getCategoryId());
            requestUpload.addParameter("edit_title", notes.getTitle());
            requestUpload.addParameter("edit_description", notes.getDescription());
            requestUpload.addParameter("notes_id", notes.getNoteId());
            requestUpload.addParameter("edit_links" + editLinkKeyArr, editLinkValueArr);


            // # adding note files
            if (addNotes != null) {
                // # adding images
                if (addNotes.getListMedia() != null) {
                    for (Notes.Media media : addNotes.getListMedia())
                        requestUpload.addFileToUpload(media.getMediaUrl(), "add_images[]");
                }

                // requestUpload.addFileToUpload("", "videos[]"); // adding videos

                // # adding documents
                if (addNotes.getListDocument() != null) {
                    for (Notes.Document document : addNotes.getListDocument()) {
                        Log.i(TAG, "document path = " + document.getDocumentUrl());
                        requestUpload.addFileToUpload(document.getDocumentUrl(), "add_documents[]");
                    }
                }

                // # adding links
                if (addNotes.getListDocument() != null) {
                    for (Notes.Link link : addNotes.getListLinks())
                        requestUpload.addParameter("add_links[]", link.getLinkUrl());
                }
            }


            // # deleting note files
            if (deleteNotes != null) {
                // # deleting images
                if (deleteNotes.getListMedia() != null) {
                    for (Notes.Media media : deleteNotes.getListMedia()) {
                        Log.i(TAG, "deleting media id = " + media.getMediaId());
                        requestUpload.addParameter("delete_images[]", media.getMediaId());
                    }
                }

                // requestUpload.addFileToUpload("", "videos[]"); // adding videos

                // # deleting documents
                if (deleteNotes.getListDocument() != null) {
                    for (Notes.Document document : deleteNotes.getListDocument()) {
                        Log.i(TAG, "document path = " + document.getDocumentUrl());
                        requestUpload.addParameter("delete_documents[]", document.getDocumentId());
                    }
                }

                // # deleting links
                if (deleteNotes.getListDocument() != null) {
                    for (Notes.Link link : deleteNotes.getListLinks())
                        requestUpload.addParameter("delete_links[]", link.getLinkId());
                }
            }

          /*  requestUpload.setNotificationConfig(new UploadNotificationConfig());
            requestUpload.setMaxRetries(3);*/
            UploadNotificationConfig config = new UploadNotificationConfig();
            requestUpload.setNotificationConfig(config);
            config.getCompleted().autoClear = true;
            requestUpload.setDelegate(new UploadStatusDelegate() {


                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {
                    Log.i(TAG, "editing note , progress = " + uploadInfo.getProgressPercent());
                    uploadInfo.describeContents();
                }

                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                    mListener.onFailed(null);
                    exception.printStackTrace();
                }

                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                    String response = serverResponse.getBodyAsString().toString();
                    Log.i(TAG, "editing note completed");
                 /*   Log.i(TAG,"editing note , is response null = "+(response == null));
                    Log.i(TAG,"editing note , server response to create Note = "+response);
                    Log.i(TAG,"editing note , is response empty = "+response.isEmpty());

                    ArrayList<String> uploadedFiles = uploadInfo.getSuccessfullyUploadedFiles();
                    for (String string : uploadedFiles)
                        Log.i(TAG,"editing note , successfully uploaded file = "+string);

                    Log.i(TAG,"editing note , uploaded file size = "+uploadedFiles.size());*/

                    if (response != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if ("1".equalsIgnoreCase(jsonObject.getString("success")))
                                mListener.onNoteEdited(new Notes());
                            else
                                mListener.onFailed("Editing note failed");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            mListener.onFailed("Editing note failed");
                        }

                    }

                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {
                    Log.i(TAG, "editing note cancelled , progress = " + uploadInfo.getProgressPercent());
                    // DialogHelper.showSimpleCustomDialog(mContext,"Cancelled...");
                }
            });


            // # uploading data
            requestUpload.startUpload();


        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException in createNote() method = " + e.getMessage());
            e.printStackTrace();
            mListener.onFailed(null);
        } catch (MalformedURLException e) {
            mListener.onFailed(null);
            Log.e(TAG, "MalformedURLException in createNote() method = " + e.getMessage());
            e.printStackTrace();
        }


    }


    // # this class parses Json data received from server and updates note view page, to show note data
    @SuppressLint("StaticFieldLeak")
    class HandleGetNoteDataResponse extends AsyncTask<String, Notes, Wrapper> {
        @Override
        protected Wrapper doInBackground(String... strings) {
            Wrapper wrapper = new Wrapper();  // # creating Wrapper object
            wrapper.message = null;

            try {
                final JSONObject json = new JSONObject(strings[0]);
                if ("1".equals(json.getString("res"))) {
                    JSONObject jsonObject = json.getJSONObject("result");   // getting JsonObject which contains data

                    wrapper.notes = new Notes();
                    wrapper.notes.setCreatedByUserName(jsonObject.getString("user_name"));
                    wrapper.notes.setCreatorRoleName(jsonObject.getString("role_name"));
                    wrapper.notes.setCreatedDateTime(jsonObject.getString("date_time"));
                    wrapper.notes.setUpdatedDateTime(jsonObject.getString("updated_date_time"));
                    wrapper.notes.setCreatedByUserPic(jsonObject.getString("user_pic"));
                    wrapper.notes.setNoteId(jsonObject.getString("notes_id"));
                    wrapper.notes.setCreatedByUserId(jsonObject.getString("created_by"));
                    wrapper.notes.setCategoryId(jsonObject.getString("category_id"));
                    wrapper.notes.setCategoryName(jsonObject.getString("category_name"));
                    wrapper.notes.setTitle(jsonObject.getString("title"));
                    wrapper.notes.setDescription(jsonObject.getString("description"));

                    //wrapper.notes.setDate_time(jsonObject.getString("date_time"));
                    // setting if note is created by self or has been shared by someone else
                    if (SessionHelper.getInstance(mContext).getUser().getUserId().equals(jsonObject.getString("created_by")))
                        wrapper.notes.setSelfCreated(true);
                    else
                        wrapper.notes.setSelfCreated(false);


                    // # getting images
                    String imagesArrayString = jsonObject.getString("notes_images");
                    if (imagesArrayString != null && !imagesArrayString.isEmpty() && !imagesArrayString.equals("null")) {
                        JSONArray imagesJson = new JSONArray(imagesArrayString);
                        if (imagesJson.length() < 1)
                            wrapper.notes.setListMedia(null);
                        else {
                            List<Notes.Media> mediaList = new ArrayList<>();
                            for (int i = 0; i < imagesJson.length(); i++) {
                                JSONObject images = imagesJson.getJSONObject(i);
                                Notes.Media media = new Notes.Media();

                                media.setMediaId(images.getString("id"));
                                media.setMediaUrl(images.getString("link"));
                                media.setMediaFromServer(true);   // setting media is from server, to check in adapter, it must be used

                                mediaList.add(media);
                            }

                            wrapper.notes.setListMedia(mediaList);
                        }

                    } else
                        wrapper.notes.setListMedia(null);  // if there is not image, setting null

                    // # getting documents
                    String documentArrayString = jsonObject.getString("notes_documents");
                    if (documentArrayString != null && !documentArrayString.isEmpty() && !documentArrayString.equals("null")) {
                        JSONArray documentJson = new JSONArray(documentArrayString);
                        if (documentJson.length() < 1)
                            wrapper.notes.setListDocument(null);
                        else {
                            List<Notes.Document> documentList = new ArrayList<>();
                            for (int i = 0; i < documentJson.length(); i++) {
                                JSONObject documentObject = documentJson.getJSONObject(i);
                                Notes.Document document = new Notes.Document();

                                document.setDocumentId(documentObject.getString("id"));
                                document.setDocumentUrl(documentObject.getString("link"));
                                document.setDocumentFromServer(true); // setting document from server, it must be used, it's used.

                                documentList.add(document);
                            }

                            wrapper.notes.setListDocument(documentList);
                        }
                    } else
                        wrapper.notes.setListDocument(null);  // if there is no data, setting null


                    // # getting link
                    String linkArrayString = jsonObject.getString("notes_links");
                    //   Log.i(TAG,"*****************************************************************\nlinkArrayString = "+linkArrayString);
                    if (linkArrayString != null && !linkArrayString.isEmpty() && !linkArrayString.equals("null")) {
                        //  Log.i(TAG,"getting link data");
                        JSONArray linkJson = new JSONArray(linkArrayString);
                        if (linkJson.length() < 1)
                            wrapper.notes.setListLinks(null);
                        else {
                            //  Log.i(TAG,"setting link data");
                            List<Notes.Link> linkList = new ArrayList<>();
                            for (int i = 0; i < linkJson.length(); i++) {
                                JSONObject linkObject = linkJson.getJSONObject(i);
                                Notes.Link link = new Notes.Link();

                                link.setLinkId(linkObject.getString("id"));
                                link.setLinkUrl(linkObject.getString("link"));
                                link.setLinkFromServer(true); // setting link from server, it must be used, it's used.

                                linkList.add(link);
                            }

                            wrapper.notes.setListLinks(linkList);   // adding link list to note object
                        }

                    } else
                        wrapper.notes.setListLinks(null);   // if there is not link, setting null

                } else
                    wrapper.message = json.getString("result");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return wrapper;
        }

/*

        @Override
        protected void onProgressUpdate(Notes... values) {
            super.onProgressUpdate(values);

            Notes notes = values[0];
            if (notes != null)
                mListener.onReceivedAllNotes(notes);  // updating category

        }
*/

        @Override
        protected void onPostExecute(Wrapper wrapper) {
            super.onPostExecute(wrapper);
            if (wrapper.notes == null)
                mListener.onFailed(wrapper.message);
            else
                mListener.onReceivedNoteData(wrapper.notes);
        }

    }


    // # this class parses Json data received from server and updates category list
    @SuppressLint("StaticFieldLeak")
    class HandleCreateCategoryResponse extends AsyncTask<String, Void, Wrapper> {
        @Override
        protected Wrapper doInBackground(String... strings) {
            Wrapper wrapper = new Wrapper();
            try {
                final JSONObject jsonObject = new JSONObject(strings[0]);
                Log.i(TAG, "josnObject of creating category");
                if ("1".equals(jsonObject.getString("success"))) {
                    JSONObject response = jsonObject.getJSONObject("message");
                    wrapper.notes = new Notes();
                    if (response != null) {
                        wrapper.notes.setCategoryId(response.getString("category_id"));
                        wrapper.notes.setCategoryName(response.getString("title"));
                    }
                } else
                    wrapper.message = jsonObject.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return wrapper;
        }


        @Override
        protected void onPostExecute(Wrapper wrapper) {
            super.onPostExecute(wrapper);
            if (wrapper.notes == null)
                mListener.onFailed(wrapper.message);
            else
                mListener.onCategoryCreated(wrapper.notes);  // updating category
        }

    }


    // # this class parses Json data received from server and updates category list
    @SuppressLint("StaticFieldLeak")
    class HandleGetAllCategoryResponse extends AsyncTask<String, Notes, Wrapper> {
        @Override
        protected Wrapper doInBackground(String... strings) {
            Wrapper wrapper = new Wrapper();
            try {
                final JSONObject jsonObject = new JSONObject(strings[0]);
                if ("1".equals(jsonObject.getString("success"))) {
                    JSONArray jsonArray = jsonObject.getJSONArray("message");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        wrapper.notes = new Notes();
                        wrapper.notes.setCategoryId(data.getString("id"));
                        wrapper.notes.setCategoryName(data.getString("category"));
                        wrapper.notes.setCreatedDateTime(data.getString("datetime"));

                        publishProgress(wrapper.notes);
                    }
                } else
                    wrapper.message = jsonObject.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return wrapper;
        }


        @Override
        protected void onProgressUpdate(Notes... values) {
            super.onProgressUpdate(values);

            Notes notes = values[0];
            if (notes != null)
                mListener.onReceivedAllCategory(notes);  // updating category

        }

        @Override
        protected void onPostExecute(Wrapper wrapper) {
            super.onPostExecute(wrapper);
            if (wrapper.notes == null)
                mListener.onFailed(wrapper.message);
        }

    }


    // # this class parses Json data received from server and updates category list
    @SuppressLint("StaticFieldLeak")
    class HandleGetAllNotesOfCategoryResponse extends AsyncTask<String, Notes, Wrapper> {
        @Override
        protected Wrapper doInBackground(String... strings) {
            Wrapper wrapper = new Wrapper();
            wrapper.message = null;
            try {
                final JSONObject jsonObject = new JSONObject(strings[0]);
                if ("1".equals(jsonObject.getString("res"))) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        wrapper.notes = new Notes();
                        wrapper.notes.setCreatedByUserName(data.getString("user_name"));
                        wrapper.notes.setCreatorRoleName(data.getString("role_name"));
                        wrapper.notes.setCreatedByUserPic(data.getString("user_pic"));
                        wrapper.notes.setCreatedDateTime(data.getString("date_time"));
                        wrapper.notes.setUpdatedDateTime(data.getString("updated_date_time"));

                        wrapper.notes.setNoteId(data.getString("id"));
                        wrapper.notes.setTitle(data.getString("title"));
                        wrapper.notes.setDescription(data.getString("description"));
                        wrapper.notes.setCategoryName(data.getString("category_name"));
                        wrapper.notes.setCategoryId(data.getString("category_id"));
                        wrapper.notes.setSelfCreated(true);   // # setting self created note true because in category all note will be self created. it must be used.

                        publishProgress(wrapper.notes);
                    }
                }
                /*else
                    wrapper.message = jsonObject.getString("message");*/

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return wrapper;
        }


        @Override
        protected void onProgressUpdate(Notes... values) {
            super.onProgressUpdate(values);

            Notes notes = values[0];
            if (notes != null)
                mListener.onReceivedAllNotesOfCategory(notes);  // updating category

        }

        @Override
        protected void onPostExecute(Wrapper wrapper) {
            super.onPostExecute(wrapper);
            if (wrapper.notes == null)
                mListener.onFailed(wrapper.message);
        }

    }


    // # this class parses Json data received from server and updates all notes list in "all notes" tab in main file and note age
    @SuppressLint("StaticFieldLeak")
    class HandleGetAllNotesResponse extends AsyncTask<String, Notes, Wrapper> {
        @Override
        protected Wrapper doInBackground(String... strings) {
            Wrapper wrapper = new Wrapper();
            try {
                final JSONObject jsonObject = new JSONObject(strings[0]);
                if ("1".equals(jsonObject.getString("res"))) {
                    String message = jsonObject.getString("result");
                    if ("No Data Found.".equalsIgnoreCase(message)) {
                        wrapper.message = message;
                        return wrapper;
                    }

                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);

                        Log.d(TAG, "note data "+data);


                        wrapper.notes = new Notes();
                        wrapper.notes.setNoteId(data.getString("id"));
                        wrapper.notes.setCreatedByUserId(data.getString("created_by"));
                        wrapper.notes.setCreatedByUserPic(data.getString("user_pic"));
                        wrapper.notes.setCategoryId(data.getString("category_id"));
                        wrapper.notes.setCategoryName(data.getString("category_name"));
                        wrapper.notes.setTitle(data.getString("title"));
                        wrapper.notes.setDescription(data.getString("description"));
                        wrapper.notes.setCreatedByUserName(data.getString("user_name"));
                        wrapper.notes.setCreatorRoleName(data.getString("role_name"));
                        wrapper.notes.setCreatedDateTime(data.getString("date_time"));
                        wrapper.notes.setUpdatedDateTime(data.getString("updated_date_time"));


                        // setting if note is created by self or has been shared by someone else
                        if (SessionHelper.getInstance(mContext).getUser().getUserId().equals(data.getString("created_by")))
                            wrapper.notes.setSelfCreated(true);
                        else
                            wrapper.notes.setSelfCreated(false);

                       /* wrapper.notes.setNoteId("2");
                        wrapper.notes.setTitle("server note title");
                        wrapper.notes.setDescription("server note description");*/

                        publishProgress(wrapper.notes);
                    }
                } else
                    wrapper.message = jsonObject.getString("message");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return wrapper;
        }


        @Override
        protected void onProgressUpdate(Notes... values) {
            super.onProgressUpdate(values);

            Notes notes = values[0];
            if (notes != null)
                mListener.onReceivedAllNotes(notes);  // updating category

        }

        @Override
        protected void onPostExecute(Wrapper wrapper) {
            super.onPostExecute(wrapper);
            if (wrapper.notes == null)
                mListener.onFailed(wrapper.message);
        }

    }

    // wrapper class to put message and Notes object it will be used to pass to onPostExecute or onPublish methods
    private class Wrapper {
        String message;
        Notes notes;
    }



}

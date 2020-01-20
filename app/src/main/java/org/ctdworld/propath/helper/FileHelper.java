package org.ctdworld.propath.helper;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import org.ctdworld.propath.AppController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileHelper
{
    private static final String TAG = FileHelper.class.getCanonicalName();
    private static Uri contentUri = null;
    //private static Context mContext;
    private static String encodedImageBase64 = "";


    // public directory to be used everywhere
    public static final String PUBLIC_DIRECTORY = Environment.DIRECTORY_DOWNLOADS;
    // folder name with public directory
    public static final String PUBLIC_DIRECTORY_WITH_APP_FOLDER = PUBLIC_DIRECTORY+"/Propath/";
    // complete folder path to put all app files
    public static final String FOLDER_FULL_PATH = Environment.getExternalStoragePublicDirectory(PUBLIC_DIRECTORY_WITH_APP_FOLDER).toString();

    public static final String TEMP_FILES_FOLDER = AppController.getContext().getFilesDir().toString();





    public interface Base64FileListener
    {
        void onBase64FileReceived(String base64File);
    }


    // it converts uri to Base64 string
    public void requestBase64File(final Context context, final Uri uri, final Base64FileListener listener)
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                Log.i(TAG, "requestBase64File(Uri uri,  final FileHelper.Base64FileListener listener) method() , Thread = " + Thread.currentThread());
                try
                {
                    InputStream inputStream = context.getContentResolver().openInputStream(uri);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int bufferSize = 1024;
                    byte[] byteArray = new byte[bufferSize];

                    int len = 0;

                    while ((len = inputStream.read(byteArray)) != -1)
                    {
                        baos.write(byteArray,0,len);
                    }

                    byte[] fileByte = baos.toByteArray();
                    String base64 = Base64.encodeToString(fileByte,Base64.DEFAULT);

                    listener.onBase64FileReceived(base64);

                }
                catch (FileNotFoundException e)
                {
                    listener.onBase64FileReceived(null);
                    e.printStackTrace();
                    Log.e(TAG,"file not found exception in requestBase64File() method , "+e.getMessage());
                }
                catch (IOException e)
                {
                    listener.onBase64FileReceived(null);
                    e.printStackTrace();
                    Log.e(TAG,"IOException exception in requestBase64File() method , "+e.getMessage());
                }

            }

        }).start();

    }



    // it return file size in kb by taking file Uri
    public static double getFileSizeInKB(Context context, Uri uri)
    {

        // Log.i(TAG,"file mime type = "+mContext.getContentResolver().getType(uri));
        InputStream inputStream = null;
        try
        {
            inputStream = context.getContentResolver().openInputStream(uri);
            return (inputStream.available())/1024.0;


        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Log.e(TAG,"FileNotFoundException in getFileSize() method , "+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"IOException in getFileSize() method , "+e.getMessage());

        }
        return 0 ;
    }


    // it return file size in mb by taking file Uri
    public static double getFileSizeInMb(Context context, Uri uri)
    {

        // Log.i(TAG,"file mime type = "+mContext.getContentResolver().getType(uri));
        InputStream inputStream = null;
        try
        {
            inputStream = context.getContentResolver().openInputStream(uri);
            return (inputStream.available())/(1024*1024.0);


        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Log.e(TAG,"FileNotFoundException in getFileSize() method , "+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"IOException in getFileSize() method , "+e.getMessage());

        }
        return 0 ;
    }


    public static void deleteFile(Context context, File file)
    {
        String[] projection = { MediaStore.Images.Media._ID };

        // Match on the file path
        String selection = MediaStore.Images.Media.DATA + " = ?";
        String[] selectionArgs = new String[] { file.getAbsolutePath() };

        // Query for the ID of the media matching the file path
        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
        if (c.moveToFirst()) {
            // We found the ID. Deleting the item via the content provider will also remove the file
            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            contentResolver.delete(deleteUri, null, null);
        }
        else {
            // File not found in media store DB
        }
        c.close();
    }


    // it takes Uri as parameter and returns file type
    public static String getFileExtension(Context context, Uri uri)
    {
        String uriString = uri.toString();
        File myFile = new File(uriString);
        String path = myFile.getAbsolutePath();


        //    Log.i(TAG,"absolute path = "+path);

        String displayName = "";

        if (uriString.startsWith("content://"))
        {
            Log.i(TAG,"file started with content");
            Cursor cursor = null;
            try
            {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        else if (uriString.startsWith("file://"))
        {
            Log.i(TAG,"file started with file");
            displayName = myFile.getName();
        }



        char[] charArr = displayName.toCharArray();
        StringBuilder builder = new StringBuilder();



        for(int i = (charArr.length-1) ; i>=0 ; i--)
        {
            char alpha = charArr[i];
            if (alpha != '.')
            {
                builder.append(alpha);    // appending characters from last index till character . is not found
            }
            else   // builder contains characters in reverse order, now putting them in correct order
            {
                StringBuilder typeBuilder = new StringBuilder();
                typeBuilder.append(".");  // adding . for extension

                char[] typeArr = builder.toString().toCharArray();
                for (int j= (typeArr.length-1); j>=0 ;  j--)
                {
                    typeBuilder.append(typeArr[j]);  // appending type characters
                }

                return typeBuilder.toString();
            }

        }





        return builder.toString();




        // Log.i(TAG,"mime type = "+mContext.getContentResolver().getType(uri));
       /* String text = context.getContentResolver().getType(uri);
        if (text == null)
            return null;
        char[] charArr = text.toCharArray();
        StringBuilder builder = new StringBuilder();
        boolean slashFound = false;
        for(int i=0 ; i<charArr.length ; i++)
        {
            char alpha = charArr[i];
            if (alpha == '/')
            {
                slashFound = true;
                continue;
            }
            if (slashFound)
                builder.append(alpha);
        }
    //    Log.i(TAG,"builder image type = "+builder.toString());
        return builder.toString() ;*/
    }


    // // it takes Uri as parameter and returns file name
    public static String getFileName(Context context, Uri uri)
    {

        String uriString = uri.toString();
        File myFile = new File(uriString);
        String path = myFile.getAbsolutePath();


        //    Log.i(TAG,"absolute path = "+path);

        String displayName = "";

        if (uriString.startsWith("content://"))
        {
            Log.i(TAG,"file started with content");
            Cursor cursor = null;
            try
            {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        else if (uriString.startsWith("file://"))
        {
            Log.i(TAG,"file started with file");
            displayName = myFile.getName();
        }


        return displayName;



      /*  String fileName = uri.getLastPathSegment();
        Log.i(TAG,"last path segment = "+fileName);
      //  Log.i(TAG,"fileName = "+fileName);
        if (fileName == null)
            return null;
        char[] nameArr = fileName.toCharArray();
        StringBuilder fileNameBuilder = new StringBuilder();
        // sometimes filename contains " primary:", so below we are removing "primary:" from filename
        boolean colonFound = false;  // character will be added to fileNameBuilder after ':' is found
        int index = -1;
        for (char aNameArr : nameArr)
        {
            index++;
           // Log.i(TAG,"nameArr characters = "+aNameArr);
            if (aNameArr == ':') // checking if  ':' is found
            {
               // Log.i(TAG,": found");
                colonFound = true;
                continue;
            }
            if (colonFound)  // character will be added to fileNameBuilder after ':' is found
            {
              //  Log.i(TAG,"appeding file name characters");
                fileNameBuilder.append(aNameArr);
            }
            else  // if ':' character is not in fileName then fileName will be returned directly
            {
                if (index == (nameArr.length-1)) //  check if index is las, to make sure fileName is returned after checking all characters
                    return fileName;
            }
        }
        return fileNameBuilder.toString();*/
    }


    // it takes url as parameter and returns file name
    public static String getFileName(Context context, String url)
    {
        // Log.i(TAG,"mime type = "+mContext.getContentResolver().getType(uri));


        if (url == null)
            return null;

        char[] charArr = url.toCharArray();
        StringBuilder builder = new StringBuilder();



        for(int i = (charArr.length-1) ; i>=0 ; i--)
        {
            char alpha = charArr[i];
            if (alpha != '/')
            {
                builder.append(alpha);
            }
            else
            {
                StringBuilder typeBuilder = new StringBuilder();
                char[] typeArr = builder.toString().toCharArray();
                for (int j= (typeArr.length-1); j>=0 ;  j--)
                {
                    typeBuilder.append(typeArr[j]);
                }

                return typeBuilder.toString();
            }

        }

        //    Log.i(TAG,"builder image type = "+builder.toString());



        return builder.toString() ;
    }


    // it takes url as parameter and returns file type
    public static String getFileExtension(Context context, String url)
    {
        // Log.i(TAG,"mime type = "+mContext.getContentResolver().getType(uri));


        if (url == null)
            return null;

        char[] charArr = url.toCharArray();
        StringBuilder builder = new StringBuilder();



        for(int i = (charArr.length-1) ; i>=0 ; i--)
        {
            char alpha = charArr[i];
            if (alpha != '.')
            {
                builder.append(alpha);    // appending characters from last index till character . is not found
            }
            else   // builder contains characters in reverse order, now putting them in correct order
            {
                StringBuilder typeBuilder = new StringBuilder();
                typeBuilder.append(".");  // adding . for extension

                char[] typeArr = builder.toString().toCharArray();
                for (int j= (typeArr.length-1); j>=0 ;  j--)
                {
                    typeBuilder.append(typeArr[j]);  // appending type characters
                }

                return typeBuilder.toString();
            }

        }

        //    Log.i(TAG,"builder image type = "+builder.toString());



        return builder.toString() ;
    }
    // it takes url as parameter and returns file type
    public static String getFileTypeWithoutExtension(Context context, String url)
    {
        // Log.i(TAG,"mime type = "+mContext.getContentResolver().getType(uri));


        if (url == null)
            return null;

        char[] charArr = url.toCharArray();
        StringBuilder builder = new StringBuilder();



        for(int i = (charArr.length-1) ; i>=0 ; i--)
        {
            char alpha = charArr[i];
            if (alpha != '.')
            {
                builder.append(alpha);    // appending characters from last index till character . is not found
            }
            else   // builder contains characters in reverse order, now putting them in correct order
            {
                StringBuilder typeBuilder = new StringBuilder();

                char[] typeArr = builder.toString().toCharArray();
                for (int j= (typeArr.length-1); j>=0 ;  j--)
                {
                    typeBuilder.append(typeArr[j]);  // appending type characters
                }

                return typeBuilder.toString();
            }

        }

        //    Log.i(TAG,"builder image type = "+builder.toString());



        return builder.toString() ;
    }




    /*public static String getImagePath( Context context, Uri uri ) {
        if(uri != null) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();
            cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            if ( cursor == null)
                return null;
            if (cursor.getCount() < 1)
            {
                Log.e(TAG,"no image found = ");
                return null;
            }
            cursor.moveToFirst();
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
            return path;
        }
        else
            return null;
    }*/




    // to get file path
    /*@TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getFilePath(final Context context, final Uri uri)
    {
        if (uri == null)
        {
            Log.e(TAG,"uri is null in getFilePath() method");
            return null;
        }
        //check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            //DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                try
                {
                    final String id = DocumentsContract.getDocumentId(uri);
                    *//*if (id != null && id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }*//*
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                }
                catch (Exception e) {
                }
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }*/

    @SuppressLint("NewApi")
    public static String getFilePath(final Context context, final Uri uri) {
        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        String selection = null;
        String[] selectionArgs = null;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri))
        {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                String fullPath = getPathFromExtSD(split);
                if (fullPath != "") {
                    return fullPath;
                } else {
                    return null;
                }
            }

            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    final String id;
                    Cursor cursor = null;
                   /* try {
                        cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DISPLAY_NAME}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            String fileName = cursor.getString(0);
                            String path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                            if (!TextUtils.isEmpty(path)) {
                                return path;
                            }
                        }
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }*/
                    id = DocumentsContract.getDocumentId(uri);
                    if (!TextUtils.isEmpty(id)) {
                        if (id.startsWith("raw:")) {
                            return id.replaceFirst("raw:", "");
                        }
                        String[] contentUriPrefixesToTry = new String[]{
                                "content://downloads/public_downloads",
                                "content://downloads/my_downloads"
                        };
                        for (String contentUriPrefix : contentUriPrefixesToTry) {
                            try {
                                final Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));

                         /*   final Uri contentUri = ContentUris.withAppendedId(
                                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));*/

                                return getDataColumn(context, contentUri, null, null);
                            } catch (NumberFormatException e) {
                                //In Android 8 and Android P the id is not a number
                                return uri.getPath().replaceFirst("^/document/raw:", "").replaceFirst("^raw:", "");
                            }
                        }


                    }

                } else {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final boolean isOreo = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                    try {
                        contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (contentUri != null) {
                        return getDataColumn(context, contentUri, null, null);
                    }
                }


            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;

                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split[1]};


                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            } else if (isGoogleDriveUri(uri)) {
                return getDriveFilePath(uri, context);
            }
        }


        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            if (isGoogleDriveUri(uri)) {
                return getDriveFilePath(uri, context);
            }
            if( Build.VERSION.SDK_INT == Build.VERSION_CODES.N)
            {
                // return getFilePathFromURI(context,uri);
                return getMediaFilePathForN(uri, context);
                // return getRealPathFromURI(context,uri);
            }else
            {

                return getDataColumn(context, uri, null, null);
            }


        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Check if a file exists on device
     *
     * @param filePath The absolute file path
     */
    private static boolean fileExists(String filePath) {
        File file = new File(filePath);

        return file.exists();
    }


    /**
     * Get full file path from external storage
     *
     * @param pathData The storage type and the relative path
     */
    private static String getPathFromExtSD(String[] pathData) {
        final String type = pathData[0];
        final String relativePath = "/" + pathData[1];
        String fullPath = "";

        // on my Sony devices (4.4.4 & 5.1.1), `type` is a dynamic string
        // something like "71F8-2C0A", some kind of unique id per storage
        // don't know any API that can get the root path of that storage based on its id.
        //
        // so no "primary" type, but let the check here for other devices
        if ("primary".equalsIgnoreCase(type)) {
            fullPath = Environment.getExternalStorageDirectory() + relativePath;
            if (fileExists(fullPath)) {
                return fullPath;
            }
        }

        // Environment.isExternalStorageRemovable() is `true` for external and internal storage
        // so we cannot relay on it.
        //
        // instead, for each possible path, check if file exists
        // we'll start with secondary storage as this could be our (physically) removable sd card
        fullPath = System.getenv("SECONDARY_STORAGE") + relativePath;
        if (fileExists(fullPath)) {
            return fullPath;
        }

        fullPath = System.getenv("EXTERNAL_STORAGE") + relativePath;
        if (fileExists(fullPath)) {
            return fullPath;
        }

        return fullPath;
    }

    private static String getDriveFilePath(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getCacheDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }

    private static String getMediaFilePathForN(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getFilesDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }


    private static String getDataColumn(Context context, Uri uri,
                                        String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }

    /**
     * @param uri - The Uri to check.
     * @return - Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri - The Uri to check.
     * @return - Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri - The Uri to check.
     * @return - Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri - The Uri to check.
     * @return - Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Drive.
     */
    private static boolean isGoogleDriveUri(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority()) || "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
   /* public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    *//**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 *//*
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    *//**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 *//*
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    *//**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 *//*
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    *//**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 *//*
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }*/
}
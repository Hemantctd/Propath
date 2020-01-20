package org.ctdworld.propath.helper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class VideoHelper
{
    private final String TAG = VideoHelper.class.getSimpleName();
    private Context mContext;


    public VideoHelper(Context context)
    {
        this.mContext = context;
    }

    public String encodeFileToBase64Binary(Uri uri) throws IOException
    {

     //   File file = new File(fileName);
        File file = getFile(uri);
        byte[] bytes = loadFile(file);
        String encoded = Base64.encodeToString(bytes,Base64.DEFAULT);
      //  String encodedString = new String(encoded);

        return encoded;
    }




    private  byte[] loadFile(File file) throws IOException
    {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        long kb = length/1024;
        long mb = kb/1024;
        Log.i(TAG,"Video length in mb = "+mb);
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }


    public File getFile(Uri uri)
    {

        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return new File(s);
    }
}

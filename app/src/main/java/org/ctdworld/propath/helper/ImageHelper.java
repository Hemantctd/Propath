package org.ctdworld.propath.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ImageHelper {
    private static final String TAG = ImageHelper.class.getSimpleName();

    Context mContext;

    private static String encodedImageBase64 = null;
    private static Bitmap bitmap = null;
    private static Bitmap mScaledBitmap = null;


    public interface BitmapListener{void onBitmapReceived(Bitmap bitmap);}
    public interface ScaledBitmapListener{void onScaledBitmapReceived(Bitmap scaledBitmap);}
    public interface Base64EncodedListener{void onBase64ImageReceived(String imageBase64);}

    private static final String BASE_64_IMAGE_TYPE = "data:image/jpeg;base64,";

    public ImageHelper(Context context)
    {
        this.mContext = context;
    }


    public void requestScaledBitmap(final Bitmap bitmap, final int width, final int height,  final ScaledBitmapListener listener)
    {
        mScaledBitmap = null;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
              //  Log.i(TAG, "requestScaledBitmap(final Bitmap bitmap, final int width, final int height,  final ScaledBitmapListener listener) method thread = " + Thread.currentThread());
                if (bitmap.getByteCount() > (width*height*4)) //4 is multiplied for 32 bit image
                    mScaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
                else
                    mScaledBitmap = bitmap;

                listener.onScaledBitmapReceived(mScaledBitmap);
            }
        }).start();

    }


    public void requestBitmap(final Uri uri, final BitmapListener listener)
    {
        bitmap = null;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if (uri != null)
                    {
                        bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(),uri);
                        listener.onBitmapReceived(bitmap);
                    }
                    else
                        Log.e(TAG,"uri is null in requestBitmap(final Uri uri, final BitmapListener listener) method ");
                }
                catch (Exception e)
                {
                    Log.i(TAG,"Error in requestBitmap(final Uri uri, final BitmapListener listener) method , "+e.getMessage());
                    e.printStackTrace();
                    listener.onBitmapReceived(bitmap);
                }
            }
        }).start();

    }

    public void requestBitmap(final File file, final BitmapListener listener)
    {
        bitmap = null;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
              //  Log.i(TAG, "requestBitmap(final File file, final BitmapListener listener) thread = " + Thread.currentThread());
              //  Log.i(TAG, "requestBitmap(final File file, final BitmapListener listener) method uri  = " + file);

                try
                {
                    if (file != null)
                    {
                        final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        listener.onBitmapReceived(bitmap);
                    }
                    else
                        Log.e(TAG,"file is null requestBitmap(final File file, final BitmapListener listener) method ");
                }
                catch (Exception e)
                {
                    Log.i(TAG,"Error in requestBitmap(final File file, final BitmapListener listener) method , "+e.getMessage());
                    e.printStackTrace();
                    listener.onBitmapReceived(bitmap);
                }
            }
        }).start();

    }

    public void requestBitmap(final Uri uri, final int requiredWidth, final int requiredHeight, final BitmapListener listener)
    {
        bitmap = null;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Log.i(TAG, "requestBitmap(final Uri uri, final int requiredWidth, final int requiredHeight, final BitmapListener listener) method thread = " + Thread.currentThread());
                try
                {
                    if (uri != null)
                    {
                        bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(),uri);

                        if (bitmap != null)
                        {
                            requestScaledBitmap(bitmap, requiredWidth, requiredHeight, new ScaledBitmapListener() {
                                @Override
                                public void onScaledBitmapReceived(Bitmap scaledBitmap) {
                                    if (scaledBitmap != null)
                                        listener.onBitmapReceived(scaledBitmap);
                                    else
                                        Log.i(TAG,"scaledBitmap is null in  requestBitmap(final Uri uri, final int requiredWidth, final int requiredHeight, final BitmapListener listener)");
                                }
                            });
                        }
                        else
                            Log.i(TAG,"bitmap is null in  requestBitmap(final Uri uri, final int requiredWidth, final int requiredHeight, final BitmapListener listener)");


                    }
                    else
                        Log.i(TAG,"uri is null in requestBitmap(final Uri uri, final int requiredWidth, final int requiredHeight, final BitmapListener listener)");
                }
                catch (Exception e)
                {
                    Log.i(TAG,"Error in requestBitmap(final Uri uri, final int requiredWidth, final int requiredHeight, final BitmapListener listener) method , "+e.getMessage());
                    e.printStackTrace();
                    listener.onBitmapReceived(bitmap);
                }
            }
        }).start();

    }


    public void requestBitmap(final File file, final int requiredWidth, final int requiredHeight, final BitmapListener listener)
    {
        bitmap = null;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
               // Log.i(TAG, "requestBitmap(final File file, final int requiredWidth, final int requiredHeight, final BitmapListener listener) method thread = " + Thread.currentThread());
                try
                {
                    if (file != null)
                    {
                        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                        if (bitmap != null)
                        {
                            requestScaledBitmap(bitmap, requiredWidth, requiredHeight, new ScaledBitmapListener() {
                                @Override
                                public void onScaledBitmapReceived(Bitmap scaledBitmap) {
                                    if (scaledBitmap != null)
                                        listener.onBitmapReceived(scaledBitmap);
                                    else
                                        Log.i(TAG,"scaledBitmap is null in  requestBitmap(final File file, final int requiredWidth, final int requiredHeight, final BitmapListener listener)");
                                }
                            });
                        }
                        else
                            Log.e(TAG,"bitmap is null in  public void requestBitmap(final File file, final int requiredWidth, final int requiredHeight, final BitmapListener listener)");


                    }
                    else
                        Log.e(TAG,"file is null in requestBitmap(final File file, final int requiredWidth, final int requiredHeight, final BitmapListener listener) method ");
                }
                catch (Exception e)
                {
                    Log.e(TAG,"Error in requestBitmap(final File file, final int requiredWidth, final int requiredHeight, final BitmapListener listener) method , "+e.getMessage());
                    e.printStackTrace();
                    listener.onBitmapReceived(bitmap);
                }
            }
        }).start();

    }


    public void requestBase64Image(final Bitmap bitmap, final int requiredWidth, final int requiredHeight, final Base64EncodedListener listener)
    {
        encodedImageBase64 = null;
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                Log.i(TAG, "requestBase64Image(final Bitmap bitmap, final int requiredWidth, final int requiredHeight, final Base64EncodedListener listener) method() , Thread = " + Thread.currentThread());
                if (bitmap != null)
                {
                   requestScaledBitmap(bitmap, requiredWidth, requiredHeight, new ScaledBitmapListener() {
                       @Override
                       public void onScaledBitmapReceived(Bitmap scaledBitmap) {
                           if (scaledBitmap != null)
                           {
                               ByteArrayOutputStream baos = new ByteArrayOutputStream();
                               bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                               byte[] imageBytes = baos.toByteArray();
                               try
                               {
                                   encodedImageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                   listener.onBase64ImageReceived(BASE_64_IMAGE_TYPE +encodedImageBase64);
                               } catch (OutOfMemoryError error)
                               {
                                   Log.e(TAG, "outOFMemoryError occurred , " + error.getMessage());
                                   error.printStackTrace();
                                   listener.onBase64ImageReceived(encodedImageBase64);
                               }
                           }
                           Log.i(TAG,"scaledBitmap is null in requestBase64Image(final Bitmap bitmap, final int requiredWidth, final int requiredHeight, final Base64EncodedListener listener) ");
                       }
                   });
                }
                else
                    Log.i(TAG, "bitmap is null in requestBase64Image(final Bitmap bitmap, final int requiredWidth, final int requiredHeight, final Base64EncodedListener listener) method ");


            }

        }).start();

    }

    public void requestBase64Image(final Bitmap bitmap, final Base64EncodedListener listener)
    {
        encodedImageBase64 = null;
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                Log.i(TAG, "requestBase64Image(final Bitmap bitmap, final Base64EncodedListener listener) method() , Thread = " + Thread.currentThread());
                if (bitmap != null)
                {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    try
                    {
                        encodedImageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        listener.onBase64ImageReceived(BASE_64_IMAGE_TYPE +encodedImageBase64);
                    } catch (OutOfMemoryError error)
                    {
                        Log.e(TAG, "outOFMemoryError occurred in requestBase64Image(final Bitmap bitmap, final Base64EncodedListener listener), " + error.getMessage());
                        error.printStackTrace();
                        listener.onBase64ImageReceived(encodedImageBase64);
                    }
                }
                else
                    Log.i(TAG, "bitmap is null in requestBase64Image(final Bitmap bitmap, final Base64EncodedListener listener) method ");


            }

        }).start();

    }


    public void requestBase64Image(final Uri uri, final int requiredWidth, final int requiredHeight, final Base64EncodedListener listener)
    {
        encodedImageBase64 = null;
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                if (uri != null)
                {
                    requestBitmap(uri, new BitmapListener() {
                        @Override
                        public void onBitmapReceived(Bitmap bitmap) {
                            requestScaledBitmap(bitmap, requiredWidth, requiredHeight, new ScaledBitmapListener()
                            {
                                @Override
                                public void onScaledBitmapReceived(Bitmap scaledBitmap) {

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] imageBytes = baos.toByteArray();
                                    try
                                    {
                                        encodedImageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                        listener.onBase64ImageReceived(BASE_64_IMAGE_TYPE +encodedImageBase64);
                                    } catch (OutOfMemoryError error)
                                    {
                                        Log.e(TAG, "outOFMemoryError occurred in requestBase64Image(final Uri uri, final int requiredWidth, final int requiredHeight, final Base64EncodedListener listener) , " + error.getMessage());
                                        error.printStackTrace();
                                        listener.onBase64ImageReceived(encodedImageBase64);
                                    }
                                }
                            });
                        }
                    });

                }
                else
                    Log.e(TAG, "uri is null in requestBase64Image(final Uri uri, final int requiredWidth, final int requiredHeight, final Base64EncodedListener listener) method ");
            }

        }).start();

    }

    public void requestBase64Image(final Uri uri, final Base64EncodedListener listener)
    {
        encodedImageBase64 = null;
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                Log.i(TAG, "requestBase64Image(final Uri uri, final Base64EncodedListener listener) method() , Thread = " + Thread.currentThread());
                if (uri != null)
                {
                    requestBitmap(uri, new BitmapListener() {
                        @Override
                        public void onBitmapReceived(Bitmap bitmap)
                        {
                            if (bitmap != null)
                            {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] imageBytes = baos.toByteArray();
                                try
                                {
                                    encodedImageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                    listener.onBase64ImageReceived(BASE_64_IMAGE_TYPE +encodedImageBase64);
                                } catch (OutOfMemoryError error)
                                {
                                    Log.e(TAG, "outOFMemoryError occurred in requestBase64Image(final Uri uri, final Base64EncodedListener listener) , " + error.getMessage());
                                    error.printStackTrace();
                                    listener.onBase64ImageReceived(encodedImageBase64);
                                }
                            }
                            else
                                Log.e(TAG,"bitmap is null in  requestBase64Image(final Uri uri, final Base64EncodedListener listener)");

                        }
                    });

                }
                else
                    Log.e(TAG, "uri is null in requestBase64Image(final Uri uri, final Base64EncodedListener listener) method ");
            }

        }).start();

    }


    public void requestBase64Image(final File file, final Base64EncodedListener listener)
    {
        encodedImageBase64 = null;
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                Log.i(TAG, "requestBase64Image(final File file, final Base64EncodedListener listener) method() , Thread = " + Thread.currentThread());
                if (file != null)
                {
                    requestBitmap(file, new BitmapListener() {
                        @Override
                        public void onBitmapReceived(Bitmap bitmap)
                        {
                            if (bitmap != null)
                            {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] imageBytes = baos.toByteArray();
                                try
                                {
                                    encodedImageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                    listener.onBase64ImageReceived(BASE_64_IMAGE_TYPE +encodedImageBase64);
                                } catch (OutOfMemoryError error)
                                {
                                    Log.e(TAG, "outOFMemoryError occurred , " + error.getMessage());
                                    error.printStackTrace();
                                    listener.onBase64ImageReceived(encodedImageBase64);
                                }
                            }
                            else
                                Log.e(TAG,"bitmap is null in  requestBase64Image(final File file, final Base64EncodedListener listener)");

                        }
                    });

                }
                else
                    Log.e(TAG, "file is null in requestBase64Image(final File file, final Base64EncodedListener listener) method ");
            }

        }).start();

    }

    public void requestBase64Image(final File file, final int requiredWidth, final int requiredHeight, final Base64EncodedListener listener)
    {
        encodedImageBase64 = null;
        new Thread(new Runnable() {
            @Override
            public void run()
            {
              //  Log.i(TAG, "requestBase64Image(final File file, final int requiredWidth, final int requiredHeight, final Base64EncodedListener listener) method() , Thread = " + Thread.currentThread());
                if (file != null)
                {
                    requestBitmap(file, new BitmapListener() {
                        @Override
                        public void onBitmapReceived(Bitmap bitmap) {
                            requestScaledBitmap(bitmap, requiredWidth, requiredHeight, new ScaledBitmapListener() {
                                @Override
                                public void onScaledBitmapReceived(Bitmap scaledBitmap) {

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] imageBytes = baos.toByteArray();
                                    try
                                    {
                                        encodedImageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                        listener.onBase64ImageReceived(BASE_64_IMAGE_TYPE +encodedImageBase64);
                                    } catch (OutOfMemoryError error)
                                    {
                                        Log.e(TAG, "outOFMemoryError occurred in requestBase64Image(final File file, final int requiredWidth, final int requiredHeight, final Base64EncodedListener listener) , " + error.getMessage());
                                        error.printStackTrace();
                                        listener.onBase64ImageReceived(encodedImageBase64);
                                    }
                                }
                            });
                        }
                    });

                }
                else
                    Log.e(TAG, "file is null in requestBase64Image(final File file, final int requiredWidth, final int requiredHeight, final Base64EncodedListener listener) method ");
            }

        }).start();

    }

}



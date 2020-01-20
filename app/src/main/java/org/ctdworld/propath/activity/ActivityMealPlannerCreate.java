package org.ctdworld.propath.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.yalantis.ucrop.UCrop;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterMealPlanCreate;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.fragment.DialogEditText;
import org.ctdworld.propath.fragment.FragCreatingDetails;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.fragment.FragmentSpeechRecognition;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.FileHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.CreatedDataDetails;
import org.ctdworld.propath.model.MealPlan;
import org.ctdworld.propath.model.User;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.utils.UiHelper;

import java.io.File;
import java.io.IOException;

public class ActivityMealPlannerCreate extends BaseActivity {

    private static final String TAG = ActivityMealPlannerCreate.class.getSimpleName();

    UiHelper uiHelper = new UiHelper();
    private static final int REQUEST_CODE_SNACK_IMAGE = 101;
    private static final int REQUEST_CODE_SNACK_IMAGE2 = 1011;
    private static final int REQUEST_CODE_SNACK_IMAGE3 = 1012;
    private static final int REQUEST_CODE_SNACK_IMAGE4 = 1013;

    private static final int REQUEST_CODE_SNACK_VIDEO = 201;
    private static final int REQUEST_CODE_SNACK_VIDEO2 = 2011;
    private static final int REQUEST_CODE_SNACK_VIDEO3 = 2012;
    private static final int REQUEST_CODE_SNACK_VIDEO4 = 2013;


    private static final int REQUEST_CODE_BREAKFAST_IMAGE = 102;
    private static final int REQUEST_CODE_BREAKFAST_VIDEO = 202;
    private static final int REQUEST_CODE_LUNCH_IMAGE = 103;
    private static final int REQUEST_CODE_LUNCH_VIDEO = 203;
    private static final int REQUEST_CODE_DINNER_IMAGE = 104;
    private static final int REQUEST_CODE_DINNER_VIDEO = 204;

    private static final int REQUEST_CODE_IMAGE_FROM_ADAPTER = 300; // TO CHANGE IMAGE FROM ADAPTER
    private static final int REQUEST_CODE_VIDEO_FROM_ADAPTER = 400;

    private Context mContext;

    private ImageView activity_meal_plan_create_img_mic_plan_title;
    private EditText activity_meal_plan_create_edit_plan_title;

    private Toolbar mToolbar;
    private TextView mToolbarTxtTitle;
    private ImageView mToolbarImgSave;

    private ImageView snacks_image, snacksImage2, snacksImage3,snacksImage4;


    private ImageView breakfast_image_or_video;
    private ImageView lunch_image_or_video;
    private ImageView dinner_image_or_video;
    private ImageView add_snacks_item_row;
    private ImageView add_snacks_item_row2;
    private ImageView add_snacks_item_row3;
    private ImageView add_snacks_item_row4;


    private ImageView add_breakfast_item_row;
    private ImageView add_lunch_item_row;
    private ImageView add_dinner_item_row;
    private EditText snacks_title;
    private EditText snacks_title2;
    private EditText snacks_title3;
    private EditText snacks_title4;

    private EditText breakfast_title;
    private EditText lunch_title;
    private EditText dinner_title;
    private ImageView snacks_img_video_icon,snacks_img_video_icon2,snacks_img_video_icon3,snacks_img_video_icon4, breakfast_img_video_icon, lunch_img_video_icon, dinner_img_video_icon;

    private String mStrSelectedMediaUriSnacks = null;
    private String mStrSelectedMediaUriSnacks2 = null;
    private String mStrSelectedMediaUriSnacks3 = null;
    private String mStrSelectedMediaUriSnacks4 = null;
    private String mStrSelectedMediaUriBreakfast = null;
    private String mStrSelectedMediaUriLunch = null;
    private String mStrSelectedMediaUriDinner = null;
    private String mStrSelectedMediaTypeSnacks = null;
    private String mStrSelectedMediaTypeSnacks2 = null;
    private String mStrSelectedMediaTypeSnacks3 = null;
    private String mStrSelectedMediaTypeSnacks4 = null;
    private String mStrSelectedMediaTypeBreakfast = null;
    private String mStrSelectedMediaTypeLunch = null;
    private String mStrSelectedMediaTypeDinner = null;
    private String mLinkSnacks,mLinkSnacks2,mLinkSnacks3,mLinkSnacks4, mLinkBreakfast, mLinkLunch, mLinkDinner;

    private AdapterMealPlanCreate mAdapterMealPlanSnacks,mAdapterMealPlanSnacks2,mAdapterMealPlanSnacks3,mAdapterMealPlanSnacks4;
    AdapterMealPlanCreate mAdapterMealPlanBreakfast;
    AdapterMealPlanCreate mAdapterMealPlanLunch;
    AdapterMealPlanCreate mAdapterMealPlanDinner;
    private RecyclerView rv_snacks, rv_breakfast, rv_lunch, rv_dinner,rv_snacks2,rv_snacks3,rv_snacks4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_planner_create);

        init();
        setToolbar();
        initListners();
        showCreatedDataDetailsFragment();
    }

    private void initListners() {

        activity_meal_plan_create_img_mic_plan_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionHelper permissionHelper = new PermissionHelper(mContext);
                if (!permissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
                    permissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO, getString(R.string.message_microphone_permission));
                else {
                    FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();
                    fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
                        @Override
                        public void onReceiveText(String spokenText) {
                            if (spokenText != null && !spokenText.isEmpty())
                                activity_meal_plan_create_edit_plan_title.setText(spokenText);
                            activity_meal_plan_create_edit_plan_title.requestFocus(EditText.FOCUS_RIGHT);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    fragmentSpeechRecognition.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.SPEECH_RECOGNITION);
                }

            }
        });

        snacks_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheetForMedia(REQUEST_CODE_SNACK_IMAGE);
            }
        });
        snacksImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheetForMedia(REQUEST_CODE_SNACK_IMAGE2);
            }
        });
        snacksImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheetForMedia(REQUEST_CODE_SNACK_IMAGE3);
            }
        });
        snacksImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheetForMedia(REQUEST_CODE_SNACK_IMAGE4);
            }
        });

        breakfast_image_or_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetForMedia(REQUEST_CODE_BREAKFAST_IMAGE);
            }
        });

        lunch_image_or_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetForMedia(REQUEST_CODE_LUNCH_IMAGE);
            }
        });

        dinner_image_or_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetForMedia(REQUEST_CODE_DINNER_IMAGE);
            }
        });

        add_snacks_item_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemTitle = snacks_title.getText().toString().trim();
                if (!itemTitle.isEmpty()) {
                    snacks_title.setText("");
                    Log.i(TAG, "adding plan item to list");
                    MealPlan.Plan.MealPlanItem planItem = new MealPlan.Plan.MealPlanItem();
                    planItem.setTitle(itemTitle);

                    planItem.setMediaUrl(mStrSelectedMediaUriSnacks);
                    planItem.setMediaType(mStrSelectedMediaTypeSnacks);
                    planItem.setLink(mLinkSnacks);

                    // clearing previous data
                    mStrSelectedMediaUriSnacks = null;
                    mStrSelectedMediaTypeSnacks = null;
                    mLinkSnacks = null;
                    snacks_image.setImageResource(R.drawable.img_default_black);
                    snacks_image.setVisibility(View.VISIBLE);
                    snacks_img_video_icon.setVisibility(View.GONE);

                    mAdapterMealPlanSnacks.addPlanItem(planItem);
                    rv_snacks.scrollToPosition(0);
                } else
                    Snackbar.make(add_snacks_item_row, "Please enter item title", Snackbar.LENGTH_SHORT).show();

            }
        });

        add_breakfast_item_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemTitle = breakfast_title.getText().toString().trim();
                if (!itemTitle.isEmpty()) {
                    breakfast_title.setText("");
                    Log.i(TAG, "adding plan item to list");
                    MealPlan.Plan.MealPlanItem planItem = new MealPlan.Plan.MealPlanItem();
                    planItem.setTitle(itemTitle);

                    planItem.setMediaUrl(mStrSelectedMediaUriBreakfast);
                    planItem.setMediaType(mStrSelectedMediaTypeBreakfast);
                    planItem.setLink(mLinkBreakfast);

                    // clearing previous data
                    mStrSelectedMediaUriBreakfast = null;
                    mStrSelectedMediaTypeBreakfast = null;
                    mLinkBreakfast = null;
                    breakfast_image_or_video.setImageResource(R.drawable.img_default_black);
                    breakfast_img_video_icon.setVisibility(View.GONE);
                    breakfast_image_or_video.setVisibility(View.VISIBLE);

                    mAdapterMealPlanBreakfast.addPlanItem(planItem);
                    rv_breakfast.scrollToPosition(0);
                } else
                    Snackbar.make(add_snacks_item_row, "Please enter item title", Snackbar.LENGTH_SHORT).show();

            }
        });

        add_lunch_item_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemTitle = lunch_title.getText().toString().trim();
                if (!itemTitle.isEmpty()) {
                    lunch_title.setText("");
                    Log.i(TAG, "adding plan item to list");
                    MealPlan.Plan.MealPlanItem planItem = new MealPlan.Plan.MealPlanItem();
                    planItem.setTitle(itemTitle);

                    planItem.setMediaUrl(mStrSelectedMediaUriLunch);
                    planItem.setMediaType(mStrSelectedMediaTypeLunch);
                    planItem.setLink(mLinkLunch);

                    // clearing previous data
                    mStrSelectedMediaUriLunch = null;
                    mStrSelectedMediaTypeLunch = null;
                    mLinkLunch = null;
                    lunch_image_or_video.setImageResource(R.drawable.img_default_black);
                    lunch_img_video_icon.setVisibility(View.GONE);
                    lunch_image_or_video.setVisibility(View.VISIBLE);

                    mAdapterMealPlanLunch.addPlanItem(planItem);
                    rv_lunch.scrollToPosition(0);
                } else
                    Snackbar.make(add_snacks_item_row, "Please enter item title", Snackbar.LENGTH_SHORT).show();

            }
        });

        add_dinner_item_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemTitle = dinner_title.getText().toString().trim();
                if (!itemTitle.isEmpty()) {
                    dinner_title.setText("");
                    Log.i(TAG, "adding plan item to list");
                    MealPlan.Plan.MealPlanItem planItem = new MealPlan.Plan.MealPlanItem();
                    planItem.setTitle(itemTitle);

                    planItem.setMediaUrl(mStrSelectedMediaUriDinner);
                    planItem.setMediaType(mStrSelectedMediaTypeDinner);
                    planItem.setLink(mLinkDinner);

                    // clearing previous data
                    mStrSelectedMediaUriDinner = null;
                    mStrSelectedMediaTypeDinner = null;
                    mLinkDinner = null;
                    dinner_image_or_video.setImageResource(R.drawable.img_default_black);
                    dinner_img_video_icon.setVisibility(View.GONE);
                    dinner_image_or_video.setVisibility(View.VISIBLE);

                    mAdapterMealPlanDinner.addPlanItem(planItem);
                    rv_dinner.scrollToPosition(0);
                } else
                    Snackbar.make(add_snacks_item_row, "Please enter item title", Snackbar.LENGTH_SHORT).show();

            }
        });
    }

    private void openBottomSheetForMedia(final int requestCode) {

        BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                .addOption(BottomSheetOption.OPTION_LINK, "Add Link")
                .addOption(BottomSheetOption.OPTION_IMAGE, "Select Image")
                .addOption(BottomSheetOption.OPTION_VIDEO, "Select Video");

        FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
        options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
            @Override
            public void onOptionSelected(int option) {
                switch (option) {
                    case BottomSheetOption.OPTION_LINK:

                        DialogEditText dialogEditText = DialogEditText.getInstance("Item Link", "Add Link", false, "Add", true);
                        dialogEditText.setOnButtonClickListener(new DialogEditText.OnButtonClickListener() {
                            @Override
                            public void onButtonClicked(String enteredValue) {

                                if (enteredValue != null && !Patterns.WEB_URL.matcher(enteredValue).matches())
                                    DialogHelper.showSimpleCustomDialog(mContext, "Enter valid link");
                                else {
                                    Log.i(TAG, "youtube link = " + enteredValue);
                                    if (enteredValue == null)
                                        return;

                                    if (requestCode == REQUEST_CODE_SNACK_IMAGE) {
                                        mLinkSnacks = enteredValue;
                                        String thumbnailUrl = UtilHelper.getYoutubeVideoThumbnailUrl(mLinkSnacks);
                                        UtilHelper.loadImageWithGlide(mContext, thumbnailUrl, 200, 200, snacks_image);
                                    } else if (requestCode == REQUEST_CODE_BREAKFAST_IMAGE) {
                                        mLinkBreakfast = enteredValue;
                                        String thumbnailUrl = UtilHelper.getYoutubeVideoThumbnailUrl(mLinkBreakfast);
                                        UtilHelper.loadImageWithGlide(mContext, thumbnailUrl, 200, 200, breakfast_image_or_video);
                                    } else if (requestCode == REQUEST_CODE_LUNCH_IMAGE) {
                                        mLinkLunch = enteredValue;
                                        String thumbnailUrl = UtilHelper.getYoutubeVideoThumbnailUrl(mLinkLunch);
                                        UtilHelper.loadImageWithGlide(mContext, thumbnailUrl, 200, 200, lunch_image_or_video);
                                    } else if (requestCode == REQUEST_CODE_DINNER_IMAGE) {
                                        mLinkDinner = enteredValue;
                                        String thumbnailUrl = UtilHelper.getYoutubeVideoThumbnailUrl(mLinkDinner);
                                        UtilHelper.loadImageWithGlide(mContext, thumbnailUrl, 200, 200, dinner_image_or_video);
                                    }
                                }
                            }
                        });
                        dialogEditText.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_EDIT_TEXT);
                        break;

                    case BottomSheetOption.OPTION_IMAGE:

                        if (requestCode == REQUEST_CODE_SNACK_IMAGE) {
                            selectMedia(REQUEST_CODE_SNACK_IMAGE, "image/*");
                        }
                        else if (requestCode == REQUEST_CODE_SNACK_IMAGE2) {
                            selectMedia(REQUEST_CODE_SNACK_IMAGE2, "image/*");
                        }

                        else if (requestCode == REQUEST_CODE_SNACK_IMAGE3) {
                            selectMedia(REQUEST_CODE_SNACK_IMAGE3, "image/*");
                        }
                        else if (requestCode == REQUEST_CODE_SNACK_IMAGE4) {
                            selectMedia(REQUEST_CODE_SNACK_IMAGE4, "image/*");
                        }
                        else if (requestCode == REQUEST_CODE_BREAKFAST_IMAGE) {
                            selectMedia(REQUEST_CODE_BREAKFAST_IMAGE, "image/*");
                        }
                        else if (requestCode == REQUEST_CODE_LUNCH_IMAGE) {
                            selectMedia(REQUEST_CODE_LUNCH_IMAGE, "image/*");
                        } else if (requestCode == REQUEST_CODE_DINNER_IMAGE) {
                            selectMedia(REQUEST_CODE_DINNER_IMAGE, "image/*");
                        }
                        break;

                    case BottomSheetOption.OPTION_VIDEO:

                        if (requestCode == 1) {
                            selectMedia(REQUEST_CODE_SNACK_VIDEO, "video/*");
                        } else if (requestCode == 2) {
                            selectMedia(REQUEST_CODE_BREAKFAST_VIDEO, "video/*");
                        } else if (requestCode == 3) {
                            selectMedia(REQUEST_CODE_LUNCH_VIDEO, "video/*");
                        } else if (requestCode == 4) {
                            selectMedia(REQUEST_CODE_DINNER_VIDEO, "video/*");
                        }
                        break;
                }
            }
        });

        options.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
    }


    // to select media from device
    private void selectMedia(int requestCode, String type) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (uiHelper.checkSelfPermissions(this)) {
                Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pictureIntent.setType(type);
                pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
                String[] mimeTypes = new String[]{"image/jpeg", "image/png"};
                pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(Intent.createChooser(pictureIntent, "Select Picture"),requestCode);
            }

        else
            {
                String permissionMessage = "Without storage permission you can not upload file.";

                PermissionHelper permissionHelper = new PermissionHelper(mContext);
                if (permissionHelper.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType(type);
                    startActivityForResult(intent, requestCode);
                } else {
                    Log.e(TAG, "storage permission is not granted in selectMedia");
                    permissionHelper.requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, permissionMessage);
                }
            }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = UCrop.getOutput(data);
                    mStrSelectedMediaUriSnacks = FileHelper.getFilePath(mContext, uri);
                    UtilHelper.loadImageWithGlide(mContext, mStrSelectedMediaUriSnacks, snacks_image);
                    mStrSelectedMediaTypeSnacks = MealPlan.Plan.MealPlanItem.MEDIA_TYPE_IMAGE;
                    snacks_img_video_icon.setVisibility(View.GONE);
                    snacks_image.setVisibility(View.VISIBLE);


    /*            else if (requestCode  == REQUEST_CODE_SNACK_IMAGE2) {
                    mStrSelectedMediaUriSnacks2 = FileHelper.getFilePath(mContext, uri);
                    UtilHelper.loadImageWithGlide(mContext, mStrSelectedMediaUriSnacks2, snacksImage2);
                    mStrSelectedMediaTypeSnacks2 = MealPlan.Plan.MealPlanItem.MEDIA_TYPE_IMAGE;
                    snacks_img_video_icon2.setVisibility(View.GONE);
                    snacksImage2.setVisibility(View.VISIBLE);
                }
                else if (requestCode  == REQUEST_CODE_SNACK_IMAGE3) {
                    mStrSelectedMediaUriSnacks3 = FileHelper.getFilePath(mContext, uri);
                    UtilHelper.loadImageWithGlide(mContext, mStrSelectedMediaUriSnacks3, snacksImage3);
                    mStrSelectedMediaTypeSnacks3 = MealPlan.Plan.MealPlanItem.MEDIA_TYPE_IMAGE;
                    snacks_img_video_icon3.setVisibility(View.GONE);
                    snacksImage3.setVisibility(View.VISIBLE);
                }
                else if (requestCode  == REQUEST_CODE_SNACK_IMAGE4) {
                    mStrSelectedMediaUriSnacks4 = FileHelper.getFilePath(mContext, uri);
                    UtilHelper.loadImageWithGlide(mContext, mStrSelectedMediaUriSnacks4, snacksImage4);
                    mStrSelectedMediaTypeSnacks4 = MealPlan.Plan.MealPlanItem.MEDIA_TYPE_IMAGE;
                    snacks_img_video_icon4.setVisibility(View.GONE);
                    snacksImage4.setVisibility(View.VISIBLE);
                }
               else if (requestCode  == REQUEST_CODE_BREAKFAST_IMAGE) {
                    mStrSelectedMediaUriBreakfast = FileHelper.getFilePath(mContext, data.getData());
                    UtilHelper.loadImageWithGlide(mContext, mStrSelectedMediaUriBreakfast, breakfast_image_or_video);
                    mStrSelectedMediaTypeBreakfast = MealPlan.Plan.MealPlanItem.MEDIA_TYPE_IMAGE;
                    breakfast_img_video_icon.setVisibility(View.GONE);
                    breakfast_image_or_video.setVisibility(View.VISIBLE);
                }*/

            }


        }
        else if (requestCode == REQUEST_CODE_SNACK_IMAGE && resultCode == RESULT_OK && data != null)
        {
            try {
                Uri sourceUri = data.getData();
                File file = getImageFile();
                Uri destinationUri = Uri.fromFile(file);
                openCropActivity(sourceUri, destinationUri);
            } catch (Exception e) {
                uiHelper.toast(this, "Please select another image");
            }


        }

        else if (requestCode == REQUEST_CODE_BREAKFAST_IMAGE && resultCode == RESULT_OK && data != null) {
            try {
                Uri sourceUri = data.getData();
                File file = getImageFile();
                Uri destinationUri = Uri.fromFile(file);
                openCropActivity(sourceUri, destinationUri);
            } catch (Exception e) {
                uiHelper.toast(this, "Please select another image");
            }


        }





/*
        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            // mTrainingPlanItem.setMediaUrl(FileHelper.getFilePath(mContext, data.getData()));
            int imageSize = UtilHelper.convertDpToPixel(mContext, (int) getResources().getDimension(R.dimen.trainingPlanItemImageSize));

            if (requestCode == REQUEST_CODE_SNACK_IMAGE) {
                mStrSelectedMediaUriSnacks = FileHelper.getFilePath(mContext, data.getData());
                UtilHelper.loadImageWithGlide(mContext, mStrSelectedMediaUriSnacks, imageSize, imageSize, snacks_image);
                mStrSelectedMediaTypeSnacks = MealPlan.Plan.MealPlanItem.MEDIA_TYPE_IMAGE;
                snacks_img_video_icon.setVisibility(View.GONE);
                snacks_image.setVisibility(View.VISIBLE);
            }

            else if (requestCode == REQUEST_CODE_SNACK_VIDEO) {
                mStrSelectedMediaUriSnacks = FileHelper.getFilePath(mContext, data.getData());
                UtilHelper.loadImageWithGlide(mContext, mStrSelectedMediaUriSnacks, imageSize, imageSize, snacks_image);
                mStrSelectedMediaTypeSnacks = MealPlan.Plan.MealPlanItem.MEDIA_TYPE_VIDEO;
                snacks_img_video_icon.setVisibility(View.VISIBLE);
                snacks_image.setVisibility(View.GONE);
            } else if (requestCode == REQUEST_CODE_BREAKFAST_IMAGE) {
                mStrSelectedMediaUriBreakfast = FileHelper.getFilePath(mContext, data.getData());
                UtilHelper.loadImageWithGlide(mContext, mStrSelectedMediaUriBreakfast, imageSize, imageSize, breakfast_image_or_video);
                mStrSelectedMediaTypeBreakfast = MealPlan.Plan.MealPlanItem.MEDIA_TYPE_IMAGE;
                breakfast_img_video_icon.setVisibility(View.GONE);
                breakfast_image_or_video.setVisibility(View.VISIBLE);
            } else if (requestCode == REQUEST_CODE_BREAKFAST_VIDEO) {
                mStrSelectedMediaUriBreakfast = FileHelper.getFilePath(mContext, data.getData());
                UtilHelper.loadImageWithGlide(mContext, mStrSelectedMediaUriBreakfast, imageSize, imageSize, breakfast_image_or_video);
                mStrSelectedMediaTypeBreakfast = MealPlan.Plan.MealPlanItem.MEDIA_TYPE_VIDEO;
                breakfast_img_video_icon.setVisibility(View.VISIBLE);
                breakfast_image_or_video.setVisibility(View.GONE);
            } else if (requestCode == REQUEST_CODE_LUNCH_IMAGE) {
                mStrSelectedMediaUriLunch = FileHelper.getFilePath(mContext, data.getData());
                UtilHelper.loadImageWithGlide(mContext, mStrSelectedMediaUriLunch, imageSize, imageSize, lunch_image_or_video);
                mStrSelectedMediaTypeLunch = MealPlan.Plan.MealPlanItem.MEDIA_TYPE_IMAGE;
                lunch_img_video_icon.setVisibility(View.GONE);
                lunch_image_or_video.setVisibility(View.VISIBLE);
            } else if (requestCode == REQUEST_CODE_LUNCH_VIDEO) {
                mStrSelectedMediaUriLunch = FileHelper.getFilePath(mContext, data.getData());
                UtilHelper.loadImageWithGlide(mContext, mStrSelectedMediaUriLunch, imageSize, imageSize, lunch_image_or_video);
                mStrSelectedMediaTypeLunch = MealPlan.Plan.MealPlanItem.MEDIA_TYPE_VIDEO;
                lunch_img_video_icon.setVisibility(View.VISIBLE);
                lunch_image_or_video.setVisibility(View.GONE);
            } else if (requestCode == REQUEST_CODE_DINNER_IMAGE) {
                mStrSelectedMediaUriDinner = FileHelper.getFilePath(mContext, data.getData());
                UtilHelper.loadImageWithGlide(mContext, mStrSelectedMediaUriDinner, imageSize, imageSize, dinner_image_or_video);
                mStrSelectedMediaTypeDinner = MealPlan.Plan.MealPlanItem.MEDIA_TYPE_IMAGE;
                dinner_img_video_icon.setVisibility(View.GONE);
                dinner_image_or_video.setVisibility(View.VISIBLE);
            } else if (requestCode == REQUEST_CODE_DINNER_VIDEO) {
                mStrSelectedMediaUriDinner = FileHelper.getFilePath(mContext, data.getData());
                UtilHelper.loadImageWithGlide(mContext, mStrSelectedMediaUriDinner, imageSize, imageSize, dinner_image_or_video);
                mStrSelectedMediaTypeDinner = MealPlan.Plan.MealPlanItem.MEDIA_TYPE_VIDEO;
                dinner_img_video_icon.setVisibility(View.VISIBLE);
                dinner_image_or_video.setVisibility(View.GONE);
            }

            if (requestCode == REQUEST_CODE_IMAGE_FROM_ADAPTER) {
                if (mAdapterMealPlanLunch != null) {
                    int updatingItemPosition = mAdapterMealPlanLunch.getUpdatingItemPosition();
                    if (updatingItemPosition == ConstHelper.NOT_FOUND)
                        return;

                    MealPlan.Plan.MealPlanItem planItem = mAdapterMealPlanLunch.getPlanItemList().get(updatingItemPosition);
                    planItem.setMediaUrl(mStrSelectedMediaUriLunch);
                    planItem.setMediaType(MealPlan.Plan.MealPlanItem.MEDIA_TYPE_IMAGE);

                    mAdapterMealPlanLunch.updateItem(updatingItemPosition, planItem);

                }
            }

            // if video requested from adapter
            else if (requestCode == REQUEST_CODE_VIDEO_FROM_ADAPTER) {
               */
/* if (mAdapterTrainingPlanItem != null)
                    mAdapterTrainingPlanItem.updateItem(mAdapterTrainingPlanItem.getUpdatingItemPosition(), mStrSelectedMediaUri,TrainingPlan.Plan.PlanItem.MEDIA_TYPE_VIDEO);*//*


                if (mAdapterMealPlanLunch != null) {
                    int updatingItemPosition = mAdapterMealPlanLunch.getUpdatingItemPosition();
                    if (updatingItemPosition == ConstHelper.NOT_FOUND)
                        return;

                    MealPlan.Plan.MealPlanItem planItem = mAdapterMealPlanLunch.getPlanItemList().get(updatingItemPosition);
                    planItem.setMediaUrl(mStrSelectedMediaUriLunch);
                    planItem.setMediaType(MealPlan.Plan.MealPlanItem.MEDIA_TYPE_VIDEO);

                    mAdapterMealPlanLunch.updateItem(updatingItemPosition, planItem);

                }
            }
        }
*/
    }
    private File getImageFile() throws IOException {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        );
        System.out.println(storageDir.getAbsolutePath());
        if (storageDir.exists())
            System.out.println("File exists");
        else
            System.out.println("File not exists");
        File file = File.createTempFile(
                imageFileName, ".jpg", storageDir
        );
        String currentPhotoPath = "file:" + file.getAbsolutePath();
        return file;
    }




    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        int croppedImageHeight = UtilHelper.convertDpToPixel(mContext, (int) getResources().getDimension(R.dimen.trainingPlanItemImageSize));
        int croppedImageWidth = UtilHelper.getScreenWidth(mContext);
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.colorAccent));
        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(16,9)
                .withMaxResultSize(croppedImageWidth, croppedImageHeight)
                .start(this);
    }


    // setting toolbar data
    private void setToolbar() {
        setSupportActionBar(mToolbar);

        mToolbarTxtTitle.setText(getString(R.string.create_meal_plan));

//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mToolbarImgSave.getLayoutParams();
//        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
//        mToolbarImgSave.setVisibility(View.VISIBLE);
//        mToolbarImgSave.setLayoutParams(params);
//        mToolbarImgSave.setImageResource(R.drawable.ic_notes_save);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    private void init() {
        mContext = this;

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTxtTitle = findViewById(R.id.toolbar_txt_title);
        mToolbarImgSave = findViewById(R.id.toolbar_img_1_customisable);

        activity_meal_plan_create_img_mic_plan_title = findViewById(R.id.activity_meal_plan_create_img_mic_plan_title);
        activity_meal_plan_create_edit_plan_title = findViewById(R.id.activity_meal_plan_create_edit_plan_title);

        snacks_image = findViewById(R.id.snacks_image);
        snacksImage2 = findViewById(R.id.snacks_image2);
        snacksImage3 = findViewById(R.id.snacks_image3);
        snacksImage4 = findViewById(R.id.snacks_image4);
        breakfast_image_or_video = findViewById(R.id.breakfast_image_or_video);
        lunch_image_or_video = findViewById(R.id.lunch_image_or_video);
        dinner_image_or_video = findViewById(R.id.dinner_image_or_video);

        add_snacks_item_row = findViewById(R.id.add_snacks_item_row);
        add_snacks_item_row2 = findViewById(R.id.add_snacks_item_row2);
        add_snacks_item_row3 = findViewById(R.id.add_snacks_item_row3);
        add_snacks_item_row4 = findViewById(R.id.add_snacks_item_row4);

        add_breakfast_item_row = findViewById(R.id.add_breakfast_item_row);
        add_lunch_item_row = findViewById(R.id.add_lunch_item_row);
        add_dinner_item_row = findViewById(R.id.add_dinner_item_row);

        snacks_img_video_icon = findViewById(R.id.snacks_img_video_icon);
        snacks_img_video_icon2 = findViewById(R.id.snacks_img_video_icon2);
        snacks_img_video_icon3 = findViewById(R.id.snacks_img_video_icon3);
        snacks_img_video_icon4 = findViewById(R.id.snacks_img_video_icon4);
        breakfast_img_video_icon = findViewById(R.id.breakfast_img_video_icon);
        lunch_img_video_icon = findViewById(R.id.lunch_img_video_icon);
        dinner_img_video_icon = findViewById(R.id.dinner_img_video_icon);

        snacks_title = findViewById(R.id.snacks_title);
        snacks_title2 = findViewById(R.id.snacks_title2);
        snacks_title3 = findViewById(R.id.snacks_title3);
        snacks_title4 = findViewById(R.id.snacks_title4);
        breakfast_title = findViewById(R.id.breakfast_title);
        lunch_title = findViewById(R.id.lunch_title);
        dinner_title = findViewById(R.id.dinner_title);

        rv_snacks = findViewById(R.id.rv_snacks);
        rv_breakfast = findViewById(R.id.rv_breakfast);
        rv_lunch = findViewById(R.id.rv_lunch);
        rv_dinner = findViewById(R.id.rv_dinner);
        rv_snacks2 = findViewById(R.id.rv_snack1);
        rv_snacks3= findViewById(R.id.rv_snack2);
        rv_snacks4 = findViewById(R.id.rv_snack3);
        mToolbarImgSave= findViewById(R.id.toolbar_img_options_menu);
        mToolbarImgSave.setVisibility(View.VISIBLE);

        mAdapterMealPlanSnacks = new AdapterMealPlanCreate(mContext, onAdapterTrainingPlanCreateListener);
        LinearLayoutManager layoutManagerLink = new LinearLayoutManager(mContext);
        rv_snacks.setLayoutManager(layoutManagerLink);
        rv_snacks.setAdapter(mAdapterMealPlanSnacks);

        mAdapterMealPlanSnacks2 = new AdapterMealPlanCreate(mContext, onAdapterTrainingPlanCreateListener);
        LinearLayoutManager layoutManagerLink21 = new LinearLayoutManager(mContext);
        rv_snacks2.setLayoutManager(layoutManagerLink21);
        rv_snacks2.setAdapter(mAdapterMealPlanSnacks2);

        mAdapterMealPlanSnacks3 = new AdapterMealPlanCreate(mContext, onAdapterTrainingPlanCreateListener);
        LinearLayoutManager layoutManagerLink31 = new LinearLayoutManager(mContext);
        rv_snacks3.setLayoutManager(layoutManagerLink31);
        rv_snacks3.setAdapter(mAdapterMealPlanSnacks3);

        mAdapterMealPlanSnacks4 = new AdapterMealPlanCreate(mContext, onAdapterTrainingPlanCreateListener);
        LinearLayoutManager layoutManagerLink41 = new LinearLayoutManager(mContext);
        rv_snacks4.setLayoutManager(layoutManagerLink41);
        rv_snacks4.setAdapter(mAdapterMealPlanSnacks4);

        mAdapterMealPlanBreakfast = new AdapterMealPlanCreate(mContext, onAdapterTrainingPlanCreateListener);
        LinearLayoutManager layoutManagerLink1 = new LinearLayoutManager(mContext);
        rv_breakfast.setLayoutManager(layoutManagerLink1);
        rv_breakfast.setAdapter(mAdapterMealPlanBreakfast);

        mAdapterMealPlanLunch = new AdapterMealPlanCreate(mContext, onAdapterTrainingPlanCreateListener);
        LinearLayoutManager layoutManagerLink2 = new LinearLayoutManager(mContext);
        rv_lunch.setLayoutManager(layoutManagerLink2);
        rv_lunch.setAdapter(mAdapterMealPlanLunch);

        mAdapterMealPlanDinner = new AdapterMealPlanCreate(mContext, onAdapterTrainingPlanCreateListener);
        LinearLayoutManager layoutManagerLink3 = new LinearLayoutManager(mContext);
        rv_dinner.setLayoutManager(layoutManagerLink3);
        rv_dinner.setAdapter(mAdapterMealPlanDinner);


        mToolbarImgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                        .addOption(BottomSheetOption.OPTION_SAVE, "Save")
                        .addOption(BottomSheetOption.OPTION_EDIT, "Edit")
                        .addOption(BottomSheetOption.OPTION_SHARE, "Share")
                        .addOption(BottomSheetOption.OPTION_COPY, "Copy")
                        .addOption(BottomSheetOption.OPTION_DELETE, "Delete");

                FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
                options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                    @Override
                    public void onOptionSelected(int option) {
                        Log.i(TAG, "onOptionSelected() method called, option = " + option);
                        switch (option) {
                            case BottomSheetOption.OPTION_SAVE:


                                break;

                            case BottomSheetOption.OPTION_EDIT:
                                break;

                            case BottomSheetOption.OPTION_SHARE:
                                DialogHelper.showCustomDialog(mContext, "Are you sure want to share this meal plan ?",
                                        new DialogHelper.ShowDialogListener() {
                                            @Override
                                            public void onOkClicked() {


                                            }

                                            @Override
                                            public void onCancelClicked() {

                                            }
                                        });

                                break;


                            case BottomSheetOption.OPTION_DELETE:

                                DialogHelper.showCustomDialog(mContext, "Are you sure want to delete this meal plan ?",
                                        new DialogHelper.ShowDialogListener() {
                                            @Override
                                            public void onOkClicked() {


                                            }

                                            @Override
                                            public void onCancelClicked() {

                                            }
                                        });

                                break;

                            case BottomSheetOption.OPTION_COPY:
                                DialogEditText dialogEditText = DialogEditText.getInstance("Copy Meal Plan", "Enter Meal Plan", "Save", false);
                                dialogEditText.setOnButtonClickListener(new DialogEditText.OnButtonClickListener() {
                                    @Override
                                    public void onButtonClicked(String enteredValue) {

                                        if (enteredValue.equals(""))
                                        {
                                            DialogHelper.showSimpleCustomDialog(mContext,"Plz fill the meal plan");
                                        }
                                        else
                                        {

                                        }

                                    }
                                });
                                if (getFragmentManager() != null) {
                                    dialogEditText.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_EDIT_TEXT);
                                }
                                break;

                        }
                    }
                });

                options.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);

            }
        });
    }


    AdapterMealPlanCreate.OnAdapterAdapterMealPlanCreateListener onAdapterTrainingPlanCreateListener = new AdapterMealPlanCreate.OnAdapterAdapterMealPlanCreateListener() {
        @Override
        public void onChangeImageOrVideoClickedInAdapter() {
            Log.i(TAG, "onChangeImageOrVideoClickedInAdapter() method called");

            BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                    .addOption(BottomSheetOption.OPTION_LINK, "Add Link")
                    .addOption(BottomSheetOption.OPTION_IMAGE, "Select Image")
                    .addOption(BottomSheetOption.OPTION_VIDEO, "Select Video");

            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(int option) {
                    Log.i(TAG, "onOptionSelected() method called, option = " + option);
                    switch (option) {
                        case BottomSheetOption.OPTION_LINK:

                            DialogEditText dialogEditText = DialogEditText.getInstance("Item Link", "Add Link", false, "Add", true);
                            dialogEditText.setOnButtonClickListener(new DialogEditText.OnButtonClickListener() {
                                @Override
                                public void onButtonClicked(String enteredValue) {
                                    mLinkLunch = enteredValue;
                                    if (mAdapterMealPlanLunch != null) {

                                        if (enteredValue != null && !Patterns.WEB_URL.matcher(enteredValue).matches())
                                            DialogHelper.showSimpleCustomDialog(mContext, "Enter valid link");
                                        else {
                                            int updatingItemPosition = mAdapterMealPlanLunch.getUpdatingItemPosition();
                                            if (updatingItemPosition == ConstHelper.NOT_FOUND)
                                                return;

                                            MealPlan.Plan.MealPlanItem planItem = mAdapterMealPlanLunch.getPlanItemList().get(updatingItemPosition);
                                            if (planItem != null) {
                                                planItem.setLink(enteredValue);
                                                planItem.setMediaType(MealPlan.Plan.MealPlanItem.MEDIA_TYPE_VIDEO);
                                                mAdapterMealPlanLunch.updateItem(updatingItemPosition, planItem);

                                            }
                                        }

                                    }
                                }
                            });
                            dialogEditText.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_EDIT_TEXT);

                            break;

                        case BottomSheetOption.OPTION_IMAGE:
                            selectMedia(REQUEST_CODE_IMAGE_FROM_ADAPTER, "image/*");
                            break;

                        case BottomSheetOption.OPTION_VIDEO:
                            selectMedia(REQUEST_CODE_VIDEO_FROM_ADAPTER, "video/*");
                            break;
                    }
                }
            });

            options.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);

        }
    };


    private void showCreatedDataDetailsFragment() {
        FragCreatingDetails fragCreatingDetails = FragCreatingDetails.newInstance(getCreatedDataDetails());
        String tag = ConstHelper.Tag.Fragment.CREATED_DATA_DETAILS;
        getSupportFragmentManager().beginTransaction().add(R.id._fragment_container_creator_details, fragCreatingDetails, tag).commit();
    }


    // setting data to its object
    public CreatedDataDetails getCreatedDataDetails() {
        User user = SessionHelper.getInstance(mContext).getUser();
        if (user == null)
            return null;

        CreatedDataDetails createdDataDetails = new CreatedDataDetails();
        createdDataDetails.setName(user.getName());
        createdDataDetails.setRoleId(user.getRoleId());
        createdDataDetails.setUserPicUrl(user.getUserPicUrl());
        createdDataDetails.setCreatedDate(DateTimeHelper.getCurrentSystemDateTime());
        createdDataDetails.setUpdatedDate(null);

        return createdDataDetails;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}

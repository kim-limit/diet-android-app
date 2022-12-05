package com.example.mydietandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageView;
    private Uri imageUri;
    Button navigate_btn2;
    private TextView textView_Date;
    private TextView textView_Time;
    private DatePickerDialog.OnDateSetListener dateCallbackMethod;
    private TimePickerDialog.OnTimeSetListener timeCallbackMethod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);

        navigate_btn2 = (Button) findViewById(R.id.navigate_btn2);
        navigate_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.example.mydietandroidapp.LAUNCH2");
                startActivity(intent);
            }
        });

        InitializeDateView();
        InitializeTimeView();
        InitializeDateListener();
        InitializeTimeListener();

        if (savedInstanceState == null) {

            MainFragment mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFragment, mainFragment, "main")
                    .commit();
        }

    }

    public void InitializeDateView() {
        textView_Date = (TextView) findViewById(R.id.mealDate);
    }

    public void InitializeDateListener() {
        dateCallbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                textView_Date.setText(year + "년 " + (month + 1) + "월 " + day + "일");
            }
        };
    }

    public void InitializeTimeView() {
        textView_Time = (TextView) findViewById(R.id.mealTime);
    }

    public void InitializeTimeListener() {
        timeCallbackMethod = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                textView_Time.setText(hour + "시" + min + "분");
            }
        };
    }

    public void OnClickTimeHandler(View view) {
        TimePickerDialog dialog = new TimePickerDialog(this, timeCallbackMethod, 8, 10, true);

        dialog.show();
    }

    public void OnClickDateHandler(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, dateCallbackMethod, 2022, 12, 1);

        dialog.show();
    }

    // 갤러리 여는 코드
    public void onClickGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GET_GALLERY_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    public void navigateGoogleMap(View view) {
        Intent intent = new Intent(".MapsActivity");
        startActivity(intent);
    }

    public void addMeal(View view) {

        // 음식 사진과 식사 장소 입력 부 추가해야함.
        ContentValues addValues = new ContentValues();
        addValues.put(MyContentProvider.NAME,
                ((EditText) findViewById(R.id.foodName)).getText().toString());
        addValues.put(MyContentProvider.MEAL_COUNT,
                Integer.parseInt(((EditText) findViewById(R.id.mealCount)).getText().toString()));
        addValues.put(MyContentProvider.REVIEW,
                ((EditText) findViewById(R.id.mealReview)).getText().toString());
        addValues.put(MyContentProvider.MEAL_DATE,
                ((TextView) findViewById(R.id.mealDate)).getText().toString());
        addValues.put(MyContentProvider.MEAL_TIME,
                ((TextView) findViewById(R.id.mealTime)).getText().toString());

        System.out.println(((TextView) findViewById(R.id.mealTime)).getText().toString());
        if (imageUri != null) {

            addValues.put(MyContentProvider.IMAGE_URI,
                    imageUri.toString()
            );
        } else {
            addValues.put(MyContentProvider.IMAGE_URI,
                    "no image");
        }
//        System.out.println(imageUri.toString());
        System.out.println(getPackageName());
        getContentResolver().insert(MyContentProvider.CONTENT_URI, addValues);

        imageUri = null;
        Toast.makeText(getBaseContext(),
                "Record Added", Toast.LENGTH_LONG).show();

    }

}
package com.example.swd_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.swd_project.Model.CourseDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CreateCourseActivity extends AppCompatActivity {
    EditText edtCourseName, edtCourseTime;
    CheckBox ckbAvailable ;
    Button btnCreate;
    CourseDTO course;
    boolean isAvailable = false ;
    private String id;
    SharedPreferences sharedPreferences;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        Intent intent = this.getIntent();
        sharedPreferences =  getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token","");


        edtCourseName = findViewById(R.id.edtCourseName);
        edtCourseTime = findViewById(R.id.edtCourseTime);
        ckbAvailable =  findViewById(R.id.ckbAvailable);
        btnCreate = findViewById(R.id.btnCreate);
        course =(CourseDTO) intent.getSerializableExtra("INFO");
        if(course !=null){
            edtCourseName.setText(course.getName());
            edtCourseTime.setText(course.getTime()+"");
            ckbAvailable.setChecked(course.isStatus());
            isAvailable = course.isStatus();
            //btnCreate.setText(course.getId());
            btnCreate.setText("Update");
        }
        ckbAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAvailable = isChecked;
            }
        });
    }

    public void clickToCreate(View view) {
        if (course == null){
            ApiCall(2);
        }else {
            ApiCall(1);
        }
    }

    //Post :1  . Put :2
    public void ApiCall(int method ){
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", edtCourseName.getText());
            jsonBody.put("time", edtCourseTime.getText());
            jsonBody.put("isAvailable",isAvailable+"" );
            if (method == 1){
                jsonBody.put("courseId",course.getId()+"");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        int a = -1;
        if(method==1){
            a= Request.Method.POST;
        }else {
            a = Request.Method.PUT;
        }

        final String requestBody = jsonBody.toString();
        Log.e("a",requestBody);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(a, ApiHolder.urlCourse, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(getApplicationContext(),ShowListCourseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params =  new HashMap<String, String>();
                params.put("Authorization","Bearer "+token);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        requestQueue.add(sr);
    }
}

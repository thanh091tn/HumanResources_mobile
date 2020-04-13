package com.example.swd_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.swd_project.Adapter.ListCourseAdapter;
import com.example.swd_project.Model.CourseDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowListCourseActivity extends AppCompatActivity {
    private List<CourseDTO> listCourse;
    private ListView listViewCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_course);

        listViewCourse =  findViewById(R.id.listCourses);
        listCourse = new ArrayList<>();
        LoadListCourse();


    }

    private void LoadListCourse(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.GET, ApiHolder.urlGetCourse, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("R",response);
                if(response != null){
                    JSONObject j = null;
                    try {
                        j = new JSONObject(response);
                        listCourse =  new ArrayList<>();
                        JSONArray data = new JSONArray(j.getString("data"));
                        for (int i = 0; i <data.length() ; i++) {
                            JSONObject course =  data.getJSONObject(i);
                            String id = course.getString("id");
                            String name =  course.getString("name");
                            Double time =  course.getDouble("time");
                            boolean status =  course.getBoolean("isAvailable");
                            CourseDTO dto = new CourseDTO(id,name,time,status);
                            listCourse.add(dto);

                        }
                        ListCourseAdapter adapter = new ListCourseAdapter(listCourse,getApplicationContext());
                        listViewCourse.setAdapter(adapter);
                        //Log.e("das", listCourse.size()+"");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(sr);
    }

    public void clickToAddCourse(View view) {
        Intent intent = new Intent(getApplicationContext(),CreateCourseActivity.class);
        startActivity(intent);
    }
}

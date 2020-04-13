package com.example.swd_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.swd_project.Adapter.ListCourseAdapter;
import com.example.swd_project.Adapter.ListProgramAdapter;
import com.example.swd_project.Model.CourseDTO;
import com.example.swd_project.Model.ProgramDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowListProgramActivity extends AppCompatActivity {
    private List<ProgramDTO> listProgram;
    private ListView listViewProgram;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_program);

        listViewProgram =  findViewById(R.id.listProgram);
        listProgram = new ArrayList<>();
        LoadListProgram();

    }


    private void LoadListProgram(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.GET, ApiHolder.urlProgramList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    JSONObject j = null;
                    try {
                        j = new JSONObject(response);
                        listProgram =  new ArrayList<>();
                        JSONArray data = new JSONArray(j.getString("data"));
                        for (int i = 0; i <data.length() ; i++) {
                            JSONObject course =  data.getJSONObject(i);
                            String id = course.getString("id");
                            String name =  course.getString("name");

                            ProgramDTO dto = new ProgramDTO(id,name);
                            listProgram.add(dto);

                        }
                        ListProgramAdapter adapter = new ListProgramAdapter(listProgram,getApplicationContext());
                        listViewProgram.setAdapter(adapter);
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

    public void clickToAddProgram(View view) {
        Intent intent =  new Intent(getApplicationContext(), CreateProgramActivity.class);
        startActivity(intent);
    }
}

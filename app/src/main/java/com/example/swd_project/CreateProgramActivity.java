package com.example.swd_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.swd_project.Adapter.ListCourseAdapter;
import com.example.swd_project.Model.CourseDTO;
import com.example.swd_project.Model.ProgramDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateProgramActivity extends AppCompatActivity {
    private List<String> listCourseName ;
    private List<String> listCourseId ;
    private List<String> listCourseAvailableName ;
    private List<String> listCourseAvailableId ;
    private EditText edtprogramName;
    private List<CourseDTO> listCourse;
    private Button btnAddProgram;
    Spinner splistprogram;
    ListView listViewCourse;
    private String id="";
    private String na;
    SharedPreferences sharedPreferences;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_program);

        sharedPreferences =  getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token","");

        Intent intent =  getIntent();
        id =  intent.getStringExtra("ID");
        na = intent.getStringExtra("NAME");

        btnAddProgram =  findViewById(R.id.btnADDProgram);
        edtprogramName = findViewById(R.id.edtProgramName);
        splistprogram=findViewById(R.id.spinnerCourses);
        listViewCourse=findViewById(R.id.listviewCourse);
        listViewCourse.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listCourseName =  new ArrayList<>();

        listCourseAvailableName = new ArrayList<>();

        listCourseId = new ArrayList<>();

        final ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,listCourseName);
        listViewCourse.setAdapter(adapter);

        final ArrayAdapter<String> spinneradapter =  new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,listCourseAvailableName);
        splistprogram.setAdapter(spinneradapter);
        LoadListCourse(adapter);








        if (id!=null){
            btnAddProgram.setText("Update");
            edtprogramName.setText(na);
            //LoadListCourseUpdate(adapter,spinneradapter);
        }


        splistprogram.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {

                    listCourseName.add(listCourseAvailableName.get(position));
                    listCourseId.add(listCourseAvailableId.get(position));
                    listCourseAvailableName.remove(position);
                    listCourseAvailableId.remove(position);



                    adapter.notifyDataSetChanged();
                    spinneradapter.notifyDataSetChanged();
                    splistprogram.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void LoadListCourse(final ArrayAdapter adapter){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.GET, ApiHolder.urlGetCourse, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    JSONObject j = null;
                    try {
                        j = new JSONObject(response);
                        listCourseAvailableId =  new ArrayList<>();
                        listCourseAvailableName =  new ArrayList<>();
                        listCourseAvailableName.add("Select Course Name");
                        listCourseAvailableId.add("Id");

                        JSONArray data = new JSONArray(j.getString("data"));
                        for (int i = 0; i <data.length() ; i++) {
                            JSONObject course =  data.getJSONObject(i);
                            String id = course.getString("id");
                            String name =  course.getString("name");
                            Double time =  course.getDouble("time");
                            boolean status =  course.getBoolean("isAvailable");

                            listCourseAvailableName.add(name);
                            listCourseAvailableId.add(id);

                        }
                        ArrayAdapter<String> spinneradapter =  new ArrayAdapter<>(getApplication(),android.R.layout.simple_spinner_dropdown_item,listCourseAvailableName);
                        splistprogram.setAdapter(spinneradapter);
                        if(id != null){
                            LoadListCourseUpdate(adapter,spinneradapter);
                        }

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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params =  new HashMap<String, String>();
                params.put("Authorization","Bearer "+token);
                return params;
            }
        };
        requestQueue.add(sr);
    }

    public void CreateProgram(){
        JSONArray courses = new JSONArray();
        for (String x: listCourseId
             ) {
            courses.put(x);
        }

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", edtprogramName.getText());
            jsonBody.put("courses", courses);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        final String requestBody = jsonBody.toString();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.PUT, ApiHolder.urlProgramCourse, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(getApplicationContext(),ShowListProgramActivity.class);
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
    public void UpdateProgram(){
        JSONArray courses = new JSONArray();
        for (String x: listCourseId
        ) {
            courses.put(x);
        }

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("programId", id);
            jsonBody.put("listCourseId", courses);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        final String requestBody = jsonBody.toString();
        Log.e("A",requestBody);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.POST, ApiHolder.urlProgramCourses, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                UpdateProgramName();
                Intent intent = new Intent(getApplicationContext(),ShowListProgramActivity.class);
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
    public void UpdateProgramName(){

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.POST, ApiHolder.urlProgramCourse+"/"+id+"?name="+edtprogramName.getText(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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
        };
        requestQueue.add(sr);
    }


    private void LoadListCourseUpdate(final ArrayAdapter adapter, final ArrayAdapter spinneradapter){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.GET, ApiHolder.urlProgramCourses+"?programid="+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("das",response);
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

                            listCourseId.add(id);
                            listCourseName.add(name);
                            if(listCourseAvailableId != null && listCourseAvailableName !=null){
                                if(listCourseAvailableName.contains(name)){
                                    listCourseAvailableName.remove(name);
                                }
                                if(listCourseAvailableId.contains(id)){
                                    listCourseAvailableId.remove(id);
                                }
                            }

                        }

                        ArrayAdapter<String> sp =  new ArrayAdapter<>(getApplication(),android.R.layout.simple_spinner_dropdown_item,listCourseAvailableName);
                        splistprogram.setAdapter(sp);
                        adapter.notifyDataSetChanged();

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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params =  new HashMap<String, String>();
                params.put("Authorization","Bearer "+token);
                return params;
            }
        };
        requestQueue.add(sr);
    }

    public void clickToCreateProgram(View view) {
        if (id != null){
            UpdateProgram();

        }else {
            CreateProgram();
        }

    }
}

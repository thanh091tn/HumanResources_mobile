package com.example.swd_project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.swd_project.ApiHolder;
import com.example.swd_project.CreateCourseActivity;
import com.example.swd_project.Model.CourseDTO;
import com.example.swd_project.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListCourseAdapter extends BaseAdapter {
    List<CourseDTO> listCourse;
    Context context;
    ListCourseAdapter listCourseAdapter;
    SharedPreferences sharedPreferences;

    public ListCourseAdapter(List<CourseDTO> listCourse, Context context) {
        this.listCourse = listCourse;
        this.context = context;
        this.listCourseAdapter = this;
    }

    @Override
    public int getCount() {
        return listCourse.size();
    }

    @Override
    public Object getItem(int position) {
        return listCourse.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView== null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.course_item,parent,false);
        }

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtTime = convertView.findViewById(R.id.txtTime);
        TextView txtCount = convertView.findViewById(R.id.txtNumberCount);
        TextView txtStatus =  convertView.findViewById(R.id.txtStatus);
        Button btnDelete  = convertView.findViewById(R.id.btnDelete);
        final CourseDTO course = listCourse.get(position);

        txtName.setText(course.getName());
        txtCount.setText(position+1+"");
        txtTime.setText(course.getTime().toString());
        if(course.isStatus()==true){
            txtStatus.setTextColor(Color.GREEN);
            txtStatus.setText("Available");
        }else {
            txtStatus.setTextColor(Color.RED);
            txtStatus.setText("Not Available");
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(context, CreateCourseActivity.class);
                intent.putExtra("INFO",(Serializable) course);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCourse(course.getId(),position);
            }
        });


        return convertView;
    }

    private void DeleteCourse(String id, final int pos){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.DELETE, ApiHolder.urlCourse+"/"+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listCourse.remove(pos);
                listCourseAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                sharedPreferences =  context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token","");
                Map<String,String> params =  new HashMap<String, String>();
                params.put("Authorization","Bearer "+token);
                return params;
            }
        };
        requestQueue.add(sr);
    }
}

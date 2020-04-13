package com.example.swd_project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.example.swd_project.CreateProgramActivity;
import com.example.swd_project.Model.CourseDTO;
import com.example.swd_project.Model.ProgramDTO;
import com.example.swd_project.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListProgramAdapter extends BaseAdapter {
    List<ProgramDTO> listProgram;
    Context context;
    ListProgramAdapter listProgramAdapter;
    SharedPreferences sharedPreferences;

    public ListProgramAdapter(List<ProgramDTO> listProgram, Context context) {
        this.listProgram = listProgram;
        this.context = context;
        this.listProgramAdapter = this;
    }
    @Override
    public int getCount() {
        return listProgram.size();
    }

    @Override
    public Object getItem(int position) {
        return listProgram.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView== null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.course_item,parent,false);
        }

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtTime = convertView.findViewById(R.id.txtTime);
        TextView txtCount = convertView.findViewById(R.id.txtNumberCount);
        TextView txtStatus =  convertView.findViewById(R.id.txtStatus);
        Button btnDelete  = convertView.findViewById(R.id.btnDelete);
        final ProgramDTO program = listProgram.get(position);

        txtName.setText(program.getName());
        txtCount.setText(position+1+"");
        txtTime.setVisibility(View.INVISIBLE);
        txtStatus.setVisibility(View.INVISIBLE);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateProgramActivity.class);
                intent.putExtra("ID",program.getId());
                intent.putExtra("NAME",program.getName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteProgram(program.getId(),position);
            }
        });
        return convertView;
    }

    private void DeleteProgram(String id, final int pos){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.DELETE, ApiHolder.urlProgramCourse+"/"+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listProgram.remove(pos);
                listProgramAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        ){
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

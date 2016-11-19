package com.sachin.karthik.sachinist;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * A simple {@link Fragment} subclass.
 */
public class Messages extends Fragment {

    private String[] title;
    private String[] desc;
    private String currTitle, currMssg;
    private static EditText mssgTitle,mssgDesc;
    private ViewGroup cont;
    MessageAdapter adapter;

    private String jsonString = "results";
    private static final String get_url = "http://192.168.0.7/get_mssgs.php";
    private static final String insert_url = "http://192.168.0.7/insert_mssg.php";
    private JSONArray mssgs = null;
    private String jsonResult;

    class getJson extends AsyncTask<String,Void,String>{

        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(getActivity(),"Loading...",null,true,true);
        }

        @Override
        protected String doInBackground(String... strings) {
            String uri = strings[0];
            HttpURLConnection httpURLConnection = null;
            InputStreamReader in = null;
            BufferedReader bufferedReader = null;
            try{
                URL url1 = new URL(uri);

                httpURLConnection = (HttpURLConnection)url1.openConnection();
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.connect();

                StringBuilder sb = new StringBuilder();
                String json;

                InputStream is = httpURLConnection.getInputStream();
                in = new InputStreamReader(is);
                bufferedReader = new BufferedReader(in);

                while((json = bufferedReader.readLine()) != null){
                    sb.append(json + "\n");
                }

                is.close();
                bufferedReader.close();
                httpURLConnection.disconnect();

                return sb.toString().trim();
            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getContext(),"Failed to load data from server",Toast.LENGTH_SHORT).show();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            jsonResult = s;
            parseJson();
        }
    }

    class InsertData extends AsyncTask<String,Void,Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            String link = params[0];
            String tit = params[1];
            String msg = params[2];

            try{
                URL ins_link = new URL(link);

                HttpURLConnection httpsURLConnection = (HttpURLConnection)ins_link.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setConnectTimeout(10000);

                DataOutputStream dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());

                String data = URLEncoder.encode("currTitle","UTF-8") + "=" + URLEncoder.encode(tit,"UTF-8") + "&"
                        + URLEncoder.encode("currMssg","UTF-8") + "=" + URLEncoder.encode(msg,"UTF-8");
                dataOutputStream.writeBytes(data);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));

                String json;
                StringBuilder sb = new StringBuilder();

                while((json = bufferedReader.readLine()) != null){
                    sb.append(json + "\n");
                }

                dataOutputStream.close();
                bufferedReader.close();
                httpsURLConnection.disconnect();

                return 1;
            }catch (Exception e){
                e.printStackTrace();
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            super.onPostExecute(aVoid);
            if(aVoid == 1) {
                Toast.makeText(getContext(), "Your Message has been added", Toast.LENGTH_SHORT).show();
                adapter.addItems(currTitle,currMssg);
                adapter.notifyDataSetChanged();
            }else
                Toast.makeText(getContext(),"Failed to Add your Message",Toast.LENGTH_SHORT).show();
        }
    }

    public Messages() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView mssgRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_centuries,container,false);
        cont = container;

        new getJson().execute(get_url);

        adapter = new MessageAdapter();
        mssgRecycler.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mssgRecycler.setLayoutManager(layoutManager);

        setHasOptionsMenu(true);

        return mssgRecycler;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.message_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void parseJson(){
        try{
            JSONObject jsonObject = new JSONObject(jsonResult);
            mssgs = jsonObject.getJSONArray("result");

            title = new String[mssgs.length()];
            desc = new String[mssgs.length()];
            for(int i = 0;i < mssgs.length();++i){
                JSONObject ob = mssgs.getJSONObject(i);
                String tit = ob.getString("TITLE");
                String mssg = ob.getString("MSSG");
                adapter.addItems(tit,mssg);
            }
            adapter.notifyDataSetChanged();
        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getActivity(),"Failed to Parse 1",Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(),"Failed to Parse 2",Toast.LENGTH_SHORT).show();
        }
    }

    public void createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_view,cont,false);
        builder.setView(dialogView);
        mssgTitle = (EditText)dialogView.findViewById(R.id.mssgTitle);
        mssgDesc = (EditText) dialogView.findViewById(R.id.mssgDesc);
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mssgDesc.setText("");
                        mssgTitle.setText("");
                    }
                });
        builder.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currTitle = mssgTitle.getText().toString();
                        currMssg = mssgDesc.getText().toString();
                        new InsertData().execute(insert_url,currTitle,currMssg);
                    }
                });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.newMssg) {
            createDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
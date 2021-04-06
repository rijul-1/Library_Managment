package com.example.library_managment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class home_page extends AppCompatActivity {
    Adapter adapter;
    RecyclerView recyclerView;
    TextView preloader;
    List<book> books;
    String value;
    String url = "https://www.googleapis.com/books/v1/volumes?q=computer+networking";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        preloader = findViewById(R.id.preloader);
        value = getIntent().getExtras().getString("uname");
//        Log.d("values",value);
        preloader.setText(value);
        books = new ArrayList<>();

        final Handler handler  = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                preloader.setVisibility(View.GONE);
                extractbooks();
                setRecyclerView();
            }
        },5000);

    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.book_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void extractbooks() {
        RequestQueue requestQueue;
        final String[] title = new String[1];
        final String[] author = new String[1];
        final String[] image_url = new String[1];
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = response.getJSONArray("items");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i = 0 ; i<jsonArray.length() ;i++) {
                    try {
                        JSONObject jsonObject;
                        jsonObject = jsonArray.getJSONObject(i);
                        JSONObject jsonObjecttitle;
                        jsonObjecttitle = jsonObject.getJSONObject("volumeInfo");
                        title[0] = jsonObjecttitle.getString("title");
                        JSONArray authors;
                        authors = jsonObjecttitle.getJSONArray("authors");
                        author[0] = authors.toString();
                        JSONObject imageurl;
                        imageurl = jsonObjecttitle.getJSONObject("imageLinks");
                        image_url[0] = imageurl.getString("smallThumbnail");
                        book book_details = new book();
                        book_details.setTitle(title[0]);
                        book_details.setAuthor(author[0]);
                        book_details.setImage_url(image_url[0]);
                        books.add(book_details);
                        Log.d("bookurl",book_details.getImage_url());
                        adapter = new Adapter(books);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    public void onItemClicked(book item) {
        Toast.makeText(this,item.getTitle() ,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
package com.codepath.instagramviewer;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class PhotosActivity extends Activity {
    
    public static final String CLIENT_ID = "d607b8041db74ab0be5256436b7a0e73";
    public ArrayList<InstagramPhoto> photos;
    public InstagramPhotosAdapter photoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        _fetchPopularPhotos();
    }

    private void _fetchPopularPhotos() {
        photos = new ArrayList<InstagramPhoto>();
        photoAdapter = new InstagramPhotosAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(photoAdapter);
        
        String reqUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(reqUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;
                try {
                    photos.clear();
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);                        
                        photos.add(_jsonRawToPhotoObject(photoJSON));
                    }
                    photoAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                    Throwable throwable) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
    
    private InstagramPhoto _jsonRawToPhotoObject(JSONObject photoJSON) throws JSONException {
        InstagramPhoto photo = new InstagramPhoto();
        photo.username = photoJSON.getJSONObject("user").getString("username");
        photo.profilePicture = photoJSON.getJSONObject("user").getString("profile_picture");
        if (photoJSON.getJSONObject("caption") != null) {
            photo.caption = photoJSON.getJSONObject("caption").getString("text");
        }
        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
        
        if (photoJSON.getJSONObject("likes") != null) {
            photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
        } else {
            photo.likesCount = 0;
        }
        
        photo.createTime = photoJSON.getInt("created_time");
        
        return photo;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

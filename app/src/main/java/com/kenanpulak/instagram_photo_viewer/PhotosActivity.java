package com.kenanpulak.instagram_photo_viewer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends Activity {

    public static final String CLIENT_ID = "c8a8bd2350c64669b0e51bcb463db3aa";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
            }
        });
        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        photos = new ArrayList<InstagramPhoto>(); //Initialize the array list
        //Create Adapter and bind it to the data in the arrayList
        aPhotos = new InstagramPhotosAdapter(this, photos);
        //Populate the data into the ListView
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        // Set the adapter to the listView
        lvPhotos.setAdapter(aPhotos);
        // Setup popular url endpoint
        String popularURL = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        // Create the Network Client
        AsyncHttpClient client = new AsyncHttpClient();
        // Trigger the Network Request
        client.get(popularURL, new JsonHttpResponseHandler() {
            //define success and failure callbacks
            //Handle The Successful JSON Response

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                //fired once successful response back
                swipeContainer.setRefreshing(false);

                JSONArray photosJSON = null;

                photos.clear();

                try {
                    photosJSON = response.getJSONArray("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < photosJSON.length(); i++) {
                    JSONObject photoJSON = new JSONObject();
                    try {
                        photoJSON = photosJSON.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    InstagramPhoto photo = new InstagramPhoto();

                    try {
                        if (photoJSON.getJSONObject("images").getJSONObject("standard_resolution") != null) {
                            photo.imageURL = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                            photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (photoJSON.getJSONObject("likes") != null) {
                            photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (photoJSON.getJSONObject("user") != null) {
                            photo.username = photoJSON.getJSONObject("user").getString("username");
                            photo.userImageURL = photoJSON.getJSONObject("user").getString("profile_picture");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (photoJSON.getJSONObject("caption") != null) {
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (photoJSON.getJSONObject("location") != null) {
                            if (photoJSON.getJSONObject("location").getString("name") != null) {
                                photo.placename = photoJSON.getJSONObject("location").getString("name");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    photos.add(photo);
                }
                //Notified the adapter that it should populate new changes into the listView
                aPhotos.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                swipeContainer.setRefreshing(false);

            }
        });
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

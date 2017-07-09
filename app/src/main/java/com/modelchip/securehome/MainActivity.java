package com.modelchip.securehome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import static android.R.id.content;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String server_name = sharedPref.getString("server_name", "");
        String server_port = sharedPref.getString("server_port", "");

        new GetMethodDemo().execute("http://"+ server_name + ":"+ server_port + "/_checkout");

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Starting a new Intent
            Intent nextScreen = new Intent(getApplicationContext(), SettingsActivity.class);

            //Sending data to another Activity
            //nextScreen.putExtra("name", inputName.getText().toString());
            //nextScreen.putExtra("email", inputEmail.getText().toString());

            //Log.e("n", inputName.getText()+"."+ inputEmail.getText());

            startActivity(nextScreen);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            String server_name = sharedPref.getString("server_name", "");
            String server_port = sharedPref.getString("server_port", "");

            new GetMethodDemo().execute("http://"+ server_name + ":"+ server_port + "/_checkout");
        } else if (id == R.id.nav_slideshow) {
            //Starting a new Intent
            Intent nextScreen = new Intent(getApplicationContext(), SettingsActivity.class);

            //Sending data to another Activity
            //nextScreen.putExtra("name", server_name.getText().toString());
            //nextScreen.putExtra("email", server_port.getText().toString());


            startActivity(nextScreen);

            Log.e("n", nextScreen.getStringExtra("server_port"));

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class GetMethodDemo extends AsyncTask<String, Void, String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.v("CatalogClient", server_response);
                    try {
                        JSONObject jObject = new JSONObject(server_response);
                        try {
                            final JSONArray jArray = jObject.getJSONArray("bricks");
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            final LinearLayout layout = (LinearLayout) findViewById(R.id.mainLayout);
                                            layout.removeAllViews();
                                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                            LayoutInflater inflater = getLayoutInflater();
                                            for (int i = 0; i < jArray.length(); i++) {
                                                final JSONObject element = jArray.getJSONObject(i);
                                                View myLayout = inflater.inflate(R.layout.entry_layout, null);
                                                TextView myText = (TextView) myLayout.findViewById(R.id.desc);
                                                myText.setText(element.getString("Title"));
                                                if (element.has("Button")) {
                                                    Button myButton = (Button) myLayout.findViewById(R.id.button);
                                                    myButton.setText(element.getString("Button"));
                                                    myButton.setVisibility(View.VISIBLE);
                                                    setOnClick(myButton, element.getString("Filename"));
                                                }
                                                if (element.has("Text")) {
                                                    myText.setText(element.getString("Title") + " ("+ element.getString("Text") + ")");
                                                }
                                                if (element.has("Img")) {
                                                    ImageView mImgView1 = (ImageView) myLayout.findViewById(R.id.image);
                                                    try {
                                                        mImgView1.setImageBitmap(new LoadImage().execute(element.getString("Img")).get());
                                                    } catch (InterruptedException e) {

                                                    } catch (ExecutionException e) {

                                                    }
                                                }
                                                layout.addView(myLayout);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                        }
                                    }
                                });
                            } catch (Exception e) {
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("Response", "" + server_response);


        }
    }

    public class RemoteRun extends AsyncTask<String, Void, String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpUrlConnection.setDoOutput(true);
//                httpUrlConnection.setDoInput(true);
                httpUrlConnection.setRequestMethod(strings[1]);
                httpUrlConnection.setRequestProperty("Content-Type", "application/json");
//                httpUrlConnection.setUseCaches (false);
                httpUrlConnection.connect();

                if(strings[2] != "") {
                    DataOutputStream outputStream = new DataOutputStream(httpUrlConnection.getOutputStream());
                    outputStream.writeBytes(strings[2]);
                    outputStream.flush();
                    outputStream.close();
                }
                int responseCode = httpUrlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(httpUrlConnection.getInputStream());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("Response", "" + server_response);


        }
    }

    public class LoadImage extends AsyncTask<String, Void, Bitmap> {
        String server_response;

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                return BitmapFactory.decodeStream((InputStream) url.getContent());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private void setOnClick(final Button btn, final String str) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Response", "" + str);
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String server_name = sharedPref.getString("server_name", "");
                String server_port = sharedPref.getString("server_port", "");

                new RemoteRun().execute("http://"+ server_name + ":"+ server_port + "/_run", "POST", str);
                // Do whatever you want(str can be used here)

            }
        });
    }
// Converting InputStream to String

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    public static Bitmap loadBitmap(String URL, BitmapFactory.Options options) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            in.close();
        } catch (IOException e1) {
        }
        return bitmap;
    }

    private static InputStream OpenHttpConnection(String strURL)
            throws IOException {
        InputStream inputStream = null;
        URL url = new URL(strURL);
        URLConnection conn = url.openConnection();

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return inputStream;
    }
}

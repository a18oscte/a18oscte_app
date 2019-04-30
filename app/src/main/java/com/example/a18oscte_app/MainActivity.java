package com.example.a18oscte_app;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ArrayList<Snus> list=new ArrayList<Snus>();

    public static final String EXTRA_MESSAGE = "MESSAGE";
    public static final String EXTRA_MESSAGE2 = "MESSAGE2";
    public static final String EXTRA_MESSAGE3 = "MESSAGE3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new FetchData().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        if(id == R.id.action_refresh){
            new FetchData().execute();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class FetchData extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... params) {
            // These two variables need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a Java string.
            String jsonStr = null;

            try {
                // Construct the URL for the Internet service
                URL url = new URL("http://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=a18oscte");

                // Create the request to the PHP-service, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                jsonStr = buffer.toString();
                return jsonStr;
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in
                // attempting to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Network error", "Error closing stream", e);
                    }
                }
            }

        }
        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);

            final ArrayList<Snus> snusArr = new ArrayList();

            class lista {
                public String name;
                public String lo;
                public String heigth;

                public lista(String name,String lo, String heigth) {
                    this.name = name;
                    this.lo = lo;
                    this.heigth = heigth;
                }
            }

            class UsersAdapter extends ArrayAdapter<lista> {
                public UsersAdapter(Context context, ArrayList<lista> users) {
                    super(context, 0, users);
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    // Get the data item for this position
                    lista user = getItem(position);
                    // Check if an existing view is being reused, otherwise inflate the view
                    if (convertView == null) {
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_textview, parent, false);
                    }
                    // Lookup view for data population
                    TextView tvn = (TextView) convertView.findViewById(R.id.list_item_textview);
                    TextView tvl = (TextView) convertView.findViewById(R.id.list_item_textview2);
                    TextView tvh = (TextView) convertView.findViewById(R.id.list_item_textview3);
                    // Populate the data into the template view using the data object
                    tvn.setText(user.name);
                    tvl.setText(user.lo);
                    tvh.setText(user.heigth);
                    // Return the completed view to render on screen
                    return convertView;
                }
            }

            // Construct the data source
            ArrayList<lista> arrayOfUsers = new ArrayList<lista>();
// Create the adapter to convert the array to views
            UsersAdapter adapter = new UsersAdapter(getApplicationContext(), arrayOfUsers);
// Attach the adapter to a ListView
            ListView listView = (ListView) findViewById(R.id.my_listview);
            listView.setAdapter(adapter);




            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String message = snusArr.get(position).info();
                    String Title = snusArr.get(position).namn();
                    String url = snusArr.get(position).img();
                    launchSecondActivity(view, message, Title, url);
                }
            });
            // This code executes after we have received our data. The String object o holds
            // the un-parsed JSON string or is null if we had an IOException during the fetch.

            // Implement a parsing code that loops through the entire JSON and creates objects
            // of our newly created Mountain class.


            try {

                JSONArray a = new JSONArray(o);

                for(int i = 0; i < a.length(); i++){
                    JSONObject json1 = (JSONObject) a.get(i);

                    Snus m = new Snus(json1.getString("name"),json1.getString("company"),json1.getString("location"),json1.getString("category"), json1.getInt("size"),json1.getInt("cost"), json1.getString("auxdata"));

                    snusArr.add(m);

                    lista newUser = new lista(snusArr.get(i).namn(), snusArr.get(i).com(), snusArr.get(i).cost());

                    adapter.add(newUser);


                }

            }catch (JSONException e) {
                Log.e("brom", "E:" + e.getMessage());
            }

        }




    }

    public void launchSecondActivity(View view, String n, String t, String u) {
        Intent intent = new Intent(this, Snusdetails.class);
        intent.putExtra(EXTRA_MESSAGE, n);
        intent.putExtra(EXTRA_MESSAGE2, t);
        intent.putExtra(EXTRA_MESSAGE3, u);
        startActivity(intent);




    }


}



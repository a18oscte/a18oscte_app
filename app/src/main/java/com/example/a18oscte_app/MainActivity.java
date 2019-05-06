package com.example.a18oscte_app;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String sortOrder;
    String val = "";

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        val = parent.getItemAtPosition(pos).toString();
        visa();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    public static final String EXTRA_MESSAGE = "MESSAGE";
    public static final String EXTRA_MESSAGE2 = "MESSAGE2";
    public static final String EXTRA_MESSAGE3 = "MESSAGE3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        visa();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_refresh){
            new FetchData().execute();
            visa();
            return true;
        }

        if(id == R.id.action_about){
            launch3Activity();

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
                URL url = new URL("https://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=a18oscte");

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

            // This code executes after we have received our data. The String object o holds
            // the un-parsed JSON string or is null if we had an IOException during the fetch.

            // Implement a parsing code that loops through the entire JSON and creates objects
            // of our newly created Mountain class.


            try {

                JSONArray a = new JSONArray(o);
                SnusReaderDbHelper snusReaderDBHelper = new SnusReaderDbHelper(getApplicationContext());
                final SQLiteDatabase db = snusReaderDBHelper.getWritableDatabase();
                db.delete(SnusReaderContract.Entry.TABLE_NAME,null,null);

                for(int i = 0; i < a.length(); i++){
                    JSONObject json1 = (JSONObject) a.get(i);

                    Snus m = new Snus(json1.getString("name"),json1.getString("company"),json1.getString("location"),json1.getString("category"), json1.getInt("size"),json1.getInt("cost"), json1.getString("auxdata"));

                    ContentValues values = new ContentValues();
                    values.put(SnusReaderContract.Entry.COLUMN_NAME_NAME, m.getName());
                    values.put(SnusReaderContract.Entry.COLUMN_NAME_FORETAG, m.getCompany());
                    values.put(SnusReaderContract.Entry.COLUMN_NAME_PRIS, m.getPris());
                    values.put(SnusReaderContract.Entry.COLUMN_NAME_MANGD, m.getMangd());
                    values.put(SnusReaderContract.Entry.COLUMN_NAME_KATEGORI, m.getKategori());
                    values.put(SnusReaderContract.Entry.COLUMN_NAME_NIKOTINHALT, m.getStyrka());
                    values.put(SnusReaderContract.Entry.COLUMN_NAME_BILD, m.getBild());


// Insert the new row, returning the primary key value of the new row
                    long newRowId = db.insert(SnusReaderContract.Entry.TABLE_NAME, null, values);




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
    public void launch3Activity() {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void visa() {
        SnusReaderDbHelper mountainReaderDBHelper = new SnusReaderDbHelper(getApplicationContext());
        final SQLiteDatabase db = mountainReaderDBHelper.getWritableDatabase();

        String[] projection = {
                SnusReaderContract.Entry.COLUMN_NAME_NAME,
                SnusReaderContract.Entry.COLUMN_NAME_PRIS,
                SnusReaderContract.Entry.COLUMN_NAME_FORETAG,
                SnusReaderContract.Entry.COLUMN_NAME_MANGD,
                SnusReaderContract.Entry.COLUMN_NAME_KATEGORI,
                SnusReaderContract.Entry.COLUMN_NAME_NIKOTINHALT,
                SnusReaderContract.Entry.COLUMN_NAME_BILD
        };


        sortOrder = null;

        if (val.equals("A-Ö")) {
            sortOrder = SnusReaderContract.Entry.COLUMN_NAME_NAME + " ASC";
        } else if (val.equals("Ö-A")) {
            sortOrder = SnusReaderContract.Entry.COLUMN_NAME_NAME+ " DESC";
        } else if (val.equals("Sortera efter nikotinhalt")){
            sortOrder = SnusReaderContract.Entry.COLUMN_NAME_NIKOTINHALT + " ASC";
        }else if(val.equals("Sortera efter pris")){
            sortOrder = SnusReaderContract.Entry.COLUMN_NAME_PRIS + " ASC";
        }


        final Cursor cursor = db.query(
                SnusReaderContract.Entry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );




        class lista {
            public String name;
            public String lo;
            public String heigth;
            public String bild;

            public lista(String name,String lo, String heigth, String bild) {
                this.name = name;
                this.lo = lo;
                this.heigth = heigth;
                this.bild = bild;
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
                ImageView Bild = (ImageView) convertView.findViewById(R.id.imageView);
                // Populate the data into the template view using the data object
                tvn.setText(user.name);
                tvl.setText(user.lo);
                tvh.setText(user.heigth + " kr/dosa");
                new DownloadImageTask(Bild) .execute(user.bild);

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

        while(cursor.moveToNext()) {
            String namn = cursor.getString(
                    cursor.getColumnIndexOrThrow(SnusReaderContract.Entry.COLUMN_NAME_NAME));
            String com = cursor.getString(
                    cursor.getColumnIndexOrThrow(SnusReaderContract.Entry.COLUMN_NAME_FORETAG));
            String cost = cursor.getString(
                    cursor.getColumnIndexOrThrow(SnusReaderContract.Entry.COLUMN_NAME_PRIS));
            String bild = cursor.getString(
                    cursor.getColumnIndexOrThrow(SnusReaderContract.Entry.COLUMN_NAME_BILD));
            lista newUser = new lista(namn, com, cost, bild);
            adapter.add(newUser);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                cursor.moveToPosition(position);

                String tmp = "Pris per dosa: ";
                tmp += cursor.getString(cursor.getColumnIndexOrThrow(SnusReaderContract.Entry.COLUMN_NAME_PRIS));
                tmp +=" Kr\nFöretag: ";
                tmp += cursor.getString(cursor.getColumnIndexOrThrow(SnusReaderContract.Entry.COLUMN_NAME_FORETAG));
                tmp +="\nMängd i dosan: ";
                tmp += cursor.getString(cursor.getColumnIndexOrThrow(SnusReaderContract.Entry.COLUMN_NAME_MANGD));
                tmp += "g\nKategori: ";
                tmp += cursor.getString(cursor.getColumnIndexOrThrow(SnusReaderContract.Entry.COLUMN_NAME_KATEGORI));
                tmp += "\nNikotinhalt: ";
                tmp += cursor.getString(cursor.getColumnIndexOrThrow(SnusReaderContract.Entry.COLUMN_NAME_NIKOTINHALT));
                tmp += " mg/g";

                String message = tmp;
                String Title = cursor.getString(cursor.getColumnIndexOrThrow(SnusReaderContract.Entry.COLUMN_NAME_NAME));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(SnusReaderContract.Entry.COLUMN_NAME_BILD));
                launchSecondActivity(view, message, Title, url);
            }
        });
    }


}



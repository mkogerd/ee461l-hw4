package slowger.hwk4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;


public class MainActivity extends AppCompatActivity {

    EditText address;
    TextView tempTextView;
    public double lat;
    public double lng;
    public String timeZone;
    String geoLocationKey = "AIza SyD8orFbuR-q_BZZfizdvtOKfeUPEs-iul8";
    String timeZoneKey = "AIzaSyDD7HM0NNOqF4pVXajvh7HTYkhigbX592s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread t = new Thread() {
            @Override
            public void run(){
                try{
                    while (!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run () {
                                TextView tdate = (TextView) findViewById(R.id.date);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy\nhh-mm-ss a");
                                String dateString = sdf.format(date);
                                tdate.setText(dateString);
                            }
                        });
                    }
                }catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }

    /**
     * Called when the user clicks the Submit button
     */
    public void submitLocation(View view) {
        // Get view elements
        address = (EditText) findViewById(R.id.address);
        tempTextView = (TextView) findViewById(R.id.textView); // (TEMP) used for displaying latitude and longitude

        // Create request URL
        String formatedAddress = URLEncoder.encode(address.getText().toString());
        final String geoLocationUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + formatedAddress + "&key=" +geoLocationKey;

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, geoLocationUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Request URL is: " + geoLocationUrl);    // DEBUG
                        System.out.println("Response is: " + response);  // DEBUG

                        // Convert string response to JSONObject to parse for latitude and longitude
                        try {
                            JSONObject responseJSON = new JSONObject(response);
                            JSONArray results = responseJSON.getJSONArray("results");
                            JSONArray address_components = ((JSONObject)results.get(0)).getJSONArray("address_components");
                            JSONObject geometry = ((JSONObject) results.get(0)).getJSONObject("geometry");
                            JSONObject loc = geometry.getJSONObject("location");
                            String county = ((JSONObject) address_components.get(4)).getString("long_name");
                            String zip = ((JSONObject) address_components.get(7)).getString("long_name");
                            lat = loc.getDouble("lat");
                            lng = loc.getDouble("lng");
                            //String county = county.get("long_name");

                            System.out.println("Latitude : Longitude --- " + lat + " : " + lng);   // DEBUG
                            tempTextView.setText("Latitude: " + lat + "\nLongitude: " + lng +"\n"+county+", "+zip);    // DEBUG
                            System.out.println(county);

                            // Call function to display timezone
                            getTimeZone();

                            Intent editIntent = new Intent(MainActivity.this, MapsActivity.class);
                            Bundle bundle = new Bundle();

                            bundle.putDouble("latt", lat);
                            bundle.putDouble("long", lng);
                            editIntent.putExtras(bundle);

                            startActivity(editIntent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.getMessage());
            }
        });

        // Add the requests to the RequestQueue.
        queue.add(stringRequest);
    }

    public void getTimeZone() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request Timezone associated with latitude and longitude
        String cords = lat+","+lng;
        Long ts = System.currentTimeMillis()/1000;
        final String timeZoneUrl = "https://maps.googleapis.com/maps/api/timezone/json?location=" + cords + "&timestamp="+ts+"&key=" +timeZoneKey;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, timeZoneUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Request URL is: " + timeZoneUrl);   // DEBUG
                        System.out.println("Response is: " + response);         // DEBUG

                        // Convert string response to JSONObject to parse for latitude and longitude
                        try {
                            // Find Timezone
                            JSONObject responseJSON = new JSONObject(response);
                            timeZone = responseJSON.getString("timeZoneId");

                            tempTextView.setText(tempTextView.getText()+"\nTimezone: "+timeZone);    // DEBUG
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.getMessage());
            }
        });

        // Add the requests to the RequestQueue.
        queue.add(stringRequest);
    }
}

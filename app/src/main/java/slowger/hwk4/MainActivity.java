package slowger.hwk4;

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


public class MainActivity extends AppCompatActivity {

    EditText address;
    TextView tempTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user clicks the Submit button */
    public void submitLocation(View view){
        // Get view elements
        address = (EditText)findViewById(R.id.address);
        tempTextView = (TextView)findViewById(R.id.textView); // (TEMP) used for displaying latitude and longitude

        // Create request URL
        String formatedAddress = URLEncoder.encode(address.getText().toString());
        final String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+formatedAddress+"&key=AIzaSyD8orFbuR-q_BZZfizdvtOKfeUPEs-iul8";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Request URL is: "+ url);    // DEBUG
                        System.out.println("Response is: "+ response);  // DEBUG

                        // Convert string response to JSONObject to parse for latitude and longitude
                        try {
                            JSONObject responseJSON = new JSONObject(response);
                            JSONArray results = responseJSON.getJSONArray("results");
                            JSONObject geometry = ((JSONObject)results.get(0)).getJSONObject("geometry");
                            JSONObject loc = geometry.getJSONObject("location");
                            double lat = loc.getDouble("lat");
                            double lng = loc.getDouble("lng");

                            System.out.println("Latitude : Longitude --- "+lat+" : "+lng);   // DEBUG
                            tempTextView.setText("Latitude: "+lat+"\nLongitude: "+lng);    // DEBUG
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

        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }
}

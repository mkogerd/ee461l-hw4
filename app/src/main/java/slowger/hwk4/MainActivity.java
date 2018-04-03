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

import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {

    EditText location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user clicks the Submit button */
    public void submitLocation(View view){
        // Get text input
        location = (EditText)findViewById(R.id.location);
        System.out.println("Text field contains: " + location.getText());

        // Create request URL
        String address = URLEncoder.encode(location.getText().toString());
        final String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+address+"&key=AIzaSyD8orFbuR-q_BZZfizdvtOKfeUPEs-iul8";

        // Get textview to show some results (TEMP)
        final TextView mTextView = (TextView) findViewById(R.id.textView);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mTextView.setText("Response is: "+ response);
                        System.out.println("url is: "+ url);            // DEBUG
                        System.out.println("Response is: "+ response);  // DEBUG
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
                Log.e("VOLLEY", error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}

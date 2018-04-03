package slowger.hwk4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    EditText location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user clicks the Submit button */
    public void submitLocation(View view){
        // Do something in response to button
        location = (EditText)findViewById(R.id.location);

        req = https://maps.googleapis.com/maps/api/geocode/json
      ?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA
                &client=YOUR_CLIENT_ID
                &signature=SIGNATURE

        System.out.print("Text field contains: ");
        System.out.println(location.getText());
    }
}

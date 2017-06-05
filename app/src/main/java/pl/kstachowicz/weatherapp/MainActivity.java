package pl.kstachowicz.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherRequest();
    }

    protected void checkWeather(){

    }

    protected void displayWeather(JSONObject response){
        try {
            JSONObject coords = response.getJSONObject("coord");
            TextView lon = (TextView) findViewById(R.id.lon);
            String lonString = getString(R.string.longitude) + " " + coords.getString("lon");
            lon.setText(lonString);

            TextView lat = (TextView) findViewById(R.id.lat);
            String latString = getString(R.string.latitude) + " " + coords.getString("lat");
            lat.setText(latString);

            JSONObject main = response.getJSONObject("main");

            TextView temp = (TextView) findViewById(R.id.temp);
            String tempString = getString(R.string.temperature) + " " + Double.toString(Double.parseDouble(main.getString("temp")) - 273.15) + " °C";
            temp.setText(tempString);

            JSONObject wind = response.getJSONObject("wind");

            TextView windVelo = (TextView) findViewById(R.id.windVelo);
            String windVeloString = getString(R.string.velocity) + " " + wind.getString("speed") + " m/s";
            windVelo.setText(windVeloString);

            TextView windDir = (TextView) findViewById(R.id.windDir);
            int degree = Integer.parseInt(wind.getString("deg"));
            String windDirString = getString(R.string.direction) + " " + Integer.toString(degree);
            windDir.setText(windDirString);

            //RotateAnimation rotateAnimation = new RotateAnimation(0, degree);
            //RotateAnimation rotateAnimation = new RotateAnimation(0, degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            //rotateAnimation.setDuration(500);
            //rotateAnimation.setInterpolator(new LinearInterpolator());

            ImageView imageView = (ImageView) findViewById(R.id.arrowImg);
            imageView.setRotation(degree);
            //imageView.startAnimation(rotateAnimation);



        } catch (JSONException e){
            e.printStackTrace();
        }
    }

   protected void weatherRequest(){
       RequestQueue mRequestQueue;

       Cache cache = new DiskBasedCache(getCacheDir(), 1024*1024);

       Network network = new BasicNetwork(new HurlStack());

       mRequestQueue = new RequestQueue(cache, network);

       mRequestQueue.start();

        //TextView jsonView = (TextView) findViewById(R.id.textView2);
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=50.0646501&lon=19.9449799&appid=cf60ac4fc28ae926204091d31c37b69e";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                displayWeather(response);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                //TextView jsonView = (TextView) findViewById(R.id.textView2);
                //jsonView.setText("No i chuj");
            }
        });

       mRequestQueue.add(jsonObjectRequest);

    }

}

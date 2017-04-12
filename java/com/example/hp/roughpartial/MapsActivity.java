package com.example.hp.roughpartial;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity  {

    GoogleMap googleMap;
    String[] stops = {"Vadakke Stand",
            "Sakthan Stand",
            "Naduvilal",
            "Kuruppam Road",
            "Kairali Sree Theatre Complex",
            "Sankarankulangara Temple",
            "Kerala Varma College",
            "Punkunnam",
            "Paaturaikkal",
            "East Fort",
            "Municipal Corporation",
            "Paramekkavu Temple",
            "Ashwini Junction",
            "MG Road",
            "Railway Station",
            "Sapna Theatre",
            "Sankaraiyyar Road"
    };
    double[][] loc={{10.5307, 76.2149},
            {10.5152, 76.2150},
            {10.52434, 76.21157},
            {10.5225, 76.212778},
            {10.5281, 76.2139}
            ,{10.53065, 76.202735},
            {10.525856,76.203035},
            {10.5352, 76.2015},
            {10.5354, 76.2115},
            {10.52281,76.2256},
            {10.5276, 76.2145},
            {10.5253, 76.2181},
            {10.534621, 76.2146867},
            {10.524119,76.2113643},
            {10.5150, 76.2080},
            {10.5263,76.2169},
            {10.5219783,76.2043816}
    };

    String gotBread;
    int i,f=0;
    LatLng stop,bus,user;
    LocationManager locationManager;
    ArrayList<LatLng> markerPoints;
    TextView tvDistanceDuration1,tvDistanceDuration2;
    double pLatitude,pLongitude;
    private static final String JSON_URL = "http://www.trial11.esy.es/fetch.php";
    String latitude, longitude;
    private static final String JSON_ARRAY = "result";
    private static final String ID = "id";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private JSONArray users = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        tvDistanceDuration1 = (TextView) findViewById(R.id.tv_distance_time);
        //tvDistanceDuration2 = (TextView) findViewById(R.id.tv_distance_time2);
        Bundle gotBasket = getIntent().getExtras();
        gotBread = gotBasket.getString("key");
        markerPoints=new ArrayList<LatLng>();
        SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting Map for the SupportMapFragment
        googleMap = fm.getMap();
        //setUpMapIfNeeded();
        googleMap.setMyLocationEnabled(true);
               googleMap.addMarker(new MarkerOptions()
                       .position(new LatLng(10.5525133, 76.22208))
                       .anchor(0.5f, 0.5f)
                       .title("Government Engineering College Thrissur")
                       .snippet("You final destination.")
                       .icon(BitmapDescriptorFactory.fromResource(R.mipmap.dest)));

        for(i=0;i<17;i++)
        {
            if(gotBread.contentEquals(stops[i])){

                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(loc[i][0],loc[i][1]))
                        .anchor(0.5f, 0.5f)
                        .title(gotBread)
                        .snippet("Your current stop.")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.stopsi)));
                stop=new LatLng(loc[i][0],loc[i][1]);
                //markerPoints.add(stop);
                //drawCircle(point);


            }
            else{
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(loc[i][0],loc[i][1]))
                        .anchor(0.5f, 0.5f)
                        .title(stops[i])
                        .snippet(" ")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher11)));

            }


        }
        getJSON(JSON_URL);

    }
    private void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MapsActivity.this, "Please Wait...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                extractJSON(s);

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }
    public void extractJSON(String myJSONString) {
        try {
            JSONObject jsonObject = new JSONObject(myJSONString);
            users = jsonObject.getJSONArray(JSON_ARRAY);
            JSONObject yu=users.getJSONObject(users.length()-1);
            latitude=yu.getString(LATITUDE);
            longitude=yu.getString(LONGITUDE);
            pLatitude=Double.parseDouble(latitude);
            pLongitude=Double.parseDouble(longitude);

            bus=new LatLng(pLatitude,pLongitude);


            googleMap.addMarker(new MarkerOptions()
                    .position(bus)
                    .anchor(0.5f, 0.5f)
                    .title("GECT College Bus")
                    .snippet("Your choice mode of transport ")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.busmar)));

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stop, 16));
            //user = new LatLng(10.534621, 76.2146867);

            markerPoints.add(bus);
            markerPoints.add(stop);
            //markerPoints.add(user);

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if(markerPoints.size() >= 2 ){
                LatLng origin = markerPoints.get(0);
                LatLng dest = markerPoints.get(1);

                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(origin, dest);

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(7);
                lineOptions.color(Color.DKGRAY);


            }

            tvDistanceDuration1.setText("College Bus to My Stop : Distance: " + distance + ", Duration: " + duration);


        googleMap.addPolyline(lineOptions);
    }
    }
}


package com.example.hp.roughpartial;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

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


public class KnowTime extends Activity {

    int flag = 0, f = 0,ind=0;

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
    double[][] loc = {{10.5309554, 76.2152562},
            {10.5152, 76.2150},
            {10.52434, 76.21157},
            {10.5225, 76.212778},
            {10.5281, 76.2139}
            , {10.53065, 76.202735},
            {10.525856, 76.203035},
            {10.5352, 76.2015},
            {10.5354, 76.2115},
            {10.52281, 76.2256},
            {10.5276, 76.2145},
            {10.5253339, 76.2181273},
            {10.534621, 76.2146867},
            {10.524119, 76.2113643},
            {10.5150, 76.2080},
            {10.5263, 76.2169},
            {10.5219783, 76.2043816}
    };

    String gotBread;
    public int mode;
    int i;
    LatLng stop, dest, bus, user,bus2;
    double mLatitude, mLongitude, nLatitude, nLongitude, pLatitude=10.5309554, pLongitude=76.2152562, mp, np, up;

    Intent intent,ini;
    PendingIntent pIntent;
    Notification noti;
    NotificationManager nm;
    ArrayList<LatLng> markerPoints;
    TextView tvDistanceDuration,stopinfo,tvDistanceDuration2,infoText;
    LocationManager locationManager;
    //ProgressBar progressBar;
    //private static final String TAG = "MAIN_ACTIVITY_ASYNC";
    private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds

    MediaPlayer notleftgec, leftgec, reachedstop, late,one, two,three,four,five,usereachedstop;
    //public static final String MY_JSON ="MY_JSON";
    int fetchType = Constants.USE_ADDRESS_LOCATION;

    private static final String TAG = "MAIN_ACTIVITY";
    AddressResultReceiver mResultReceiver;

    public String duration=" ",restoredText;


    // private static final String JSON_URL = "http://www.simplifiedcoding.16mb.com/myjson.php";
    public static final String MY_JSON ="MY_JSON";

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
        setContentView(R.layout.activity_know_time);
        notleftgec = MediaPlayer.create(this, R.raw.notleftgec);
        leftgec = MediaPlayer.create(this, R.raw.leftgec);
        reachedstop = MediaPlayer.create(this, R.raw.arrivestop);
        late = MediaPlayer.create(this, R.raw.late);
        one = MediaPlayer.create(this, R.raw.one);
        two = MediaPlayer.create(this, R.raw.two);
        three = MediaPlayer.create(this, R.raw.three);
        four = MediaPlayer.create(this, R.raw.four);
        five = MediaPlayer.create(this, R.raw.five);
        usereachedstop=MediaPlayer.create(this,R.raw.usereachedstop);
        markerPoints = new ArrayList<LatLng>();
        SharedPreferences prefs=getSharedPreferences("prior_file",MODE_PRIVATE);
        mode=prefs.getInt("mode",0);


        Bundle gotBasket = getIntent().getExtras();
        gotBread = gotBasket.getString("key");

        tvDistanceDuration = (TextView) findViewById(R.id.value1);
        //tvDistanceDuration2 = (TextView) findViewById(R.id.value2);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        stopinfo= (TextView) findViewById(R.id.stopinfo);
        infoText=(TextView)findViewById(R.id.infoText);
        mResultReceiver = new AddressResultReceiver(null);
        stopinfo.setText(gotBread);
        for (i = 0; i < 17; i++) {
            if (gotBread.contentEquals(stops[i])) {
                stop = new LatLng(loc[i][0], loc[i][1]);
            }
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        dest = new LatLng(10.5525133, 76.22208);
        user = new LatLng(10.534621, 76.2146867);
        mLatitude = stop.latitude;
        mLongitude = stop.longitude;
        //gec coordinates
        nLatitude = dest.latitude;
        nLongitude = dest.longitude;

        getJSON(JSON_URL);

    }

    private void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(KnowTime.this, "Please Wait...",null,true,true);
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
                    return "a";
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
            markerPoints.add(bus);
            markerPoints.add(stop);
            //bus to stop dist
            mp = distFrom(mLatitude, mLongitude, pLatitude, pLongitude);
            //gec to bus distance
            np = distFrom(nLatitude, nLongitude, pLatitude, pLongitude);
            //user to stop distance
            up = distFrom(user.latitude, user.longitude, mLatitude, mLongitude);
            // user to stop dist less than stop to bus


            if (np < 1) {
                //Bus has not left gec
                if(mode==1){
                notleftgec.start();}
                //flag = 1;
                intent = new Intent(this, NotificationView.class);
                pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                noti = new Notification.Builder(getApplicationContext())
                        .setTicker("Bus Not Yet Dispatched")
                        .setContentTitle("College bus hasn't left GEC yet.")
                        .setContentText("Stay tuned for more updates")
                        .setSmallIcon(R.mipmap.aaaa)
                        .addAction(R.mipmap.ic_launcher, "Action 1", pIntent)
                        .addAction(R.mipmap.ic_launcher, "Action 2", pIntent)
                        .setContentIntent(pIntent).getNotification();
                noti.flags = Notification.FLAG_AUTO_CANCEL;
                nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                nm.notify(0, noti);

            }
            else if(up<2){
                ind=1;
                if(mode==1){usereachedstop.start();}
                //tvDistanceDuration.setText(" stop");
                intent = new Intent(this, NotificationView.class);
                pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                noti = new Notification.Builder(getApplicationContext())
                        .setTicker("You have reached your stop.")
                        .setContentTitle("Congratulations! You are right on time.")
                        .setContentText("Have a great day")
                        .setSmallIcon(R.mipmap.aaaa)
                        .addAction(R.mipmap.ic_launcher, "Action 1", pIntent)
                        .addAction(R.mipmap.ic_launcher, "Action 2", pIntent)
                        .setContentIntent(pIntent).getNotification();
                noti.flags = Notification.FLAG_AUTO_CANCEL;
                nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0, noti);
                //showOneButtonDialog();

            }

            else if (up > mp) {
                //f = 1;
                if(mode==1){late.start();}
                tvDistanceDuration.setText("Left your stop");
                intent = new Intent(this, NotificationView.class);
                pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                noti = new Notification.Builder(getApplicationContext())
                        .setTicker("You are too late!")
                        .setContentTitle("You cannot make it to catch the bus")
                        .setContentText("Change your stop immediately")
                        .setSmallIcon(R.mipmap.aaaa)
                        .addAction(R.mipmap.ic_launcher, "Action 1", pIntent)
                        .addAction(R.mipmap.ic_launcher, "Action 2", pIntent)
                        .setContentIntent(pIntent).getNotification();
                noti.flags = Notification.FLAG_AUTO_CANCEL;
                nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0, noti);
                showOneButtonDialog();

            }
            else if (mp < 3 ) {
                //Bus has reached stop
                f=1;
                if(mode==1){
                reachedstop.start();}
                intent = new Intent(this, NotificationView.class);
                pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                noti = new Notification.Builder(getApplicationContext())
                        .setTicker("Bus has arrived at your stop!")
                        .setContentTitle("The bus has arrived at your destined stop.")
                        .setContentText("Have a great day!")
                        .setSmallIcon(R.mipmap.aaaa)
                        .addAction(R.mipmap.ic_launcher, "Action 1", pIntent)
                        .addAction(R.mipmap.ic_launcher, "Action 2", pIntent)
                        .setContentIntent(pIntent).getNotification();
                noti.flags = Notification.FLAG_AUTO_CANCEL;
                nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0, noti);
                tvDistanceDuration.setText("0 mins");


            } else {
                //Bus has left gec
                //flag = 0;
                if(mode==1){
                leftgec.start();}
                intent = new Intent(this, NotificationView.class);
                pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                noti = new Notification.Builder(getApplicationContext())
                        .setTicker("Bus is dispatched from GEC")
                        .setContentTitle("Bus left GEC and is now on road.")
                        .setContentText("Get ready and be right on time. Stay tuned for more updates.")
                        .setSmallIcon(R.mipmap.aaaa)
                        .addAction(R.mipmap.ic_launcher, " ", pIntent)
                        .addAction(R.mipmap.ic_launcher, " ", pIntent)
                        .setContentIntent(pIntent).getNotification();
                noti.flags = Notification.FLAG_AUTO_CANCEL;
                nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0, noti);

            }

            Intent in = new Intent(this, GeocodeAddressIntentService.class);
            in.putExtra(Constants.RECEIVER, mResultReceiver);
            in.putExtra(Constants.FETCH_TYPE_EXTRA, fetchType);

            in.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA,
                    pLatitude);
            in.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA,
                    pLongitude);

            //infoText.setVisibility(View.INVISIBLE);
            //progressBar.setVisibility(View.VISIBLE);
            Log.e(TAG, "Starting Service");
            startService(in);
            if(markerPoints.size() >= 2 && flag == 0 && f == 0 ) {
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

    private void showOneButtonDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("You are late");
        dialogBuilder.setMessage("Change your stop to reach on time.");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(KnowTime.this, "Go to Change Stop and enter a different stop.", Toast.LENGTH_SHORT).show();

            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public static float distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
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

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
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

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

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
            //PolylineOptions lineOptions = null;
            //MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            //String duration = "";

            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                //lineOptions = new PolylineOptions();

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

                }

            }
            if(duration.contentEquals("1 min") && f!=1 && ind!=1){
                if(mode==1){one.start();}
                ini = new Intent(getApplicationContext(), NotificationView.class);
                pIntent = PendingIntent.getActivity(getApplicationContext(), 0, ini, 0);
                noti = new Notification.Builder(getApplicationContext())
                        .setTicker("Bus is 1 min away from your stop")
                        .setContentTitle("Bus is 1 min away from your stop.")
                        .setContentText(" ")
                        .setSmallIcon(R.mipmap.aaaa)
                        .addAction(R.mipmap.ic_launcher, " ", pIntent)
                        .addAction(R.mipmap.ic_launcher, " ", pIntent)
                        .setContentIntent(pIntent).getNotification();
                noti.flags = Notification.FLAG_AUTO_CANCEL;
                nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0, noti);
            }
            else if(duration.contentEquals("2 mins")){
                if(mode==1){two.start();}
                ini = new Intent(getApplicationContext(), NotificationView.class);
                pIntent = PendingIntent.getActivity(getApplicationContext(), 0, ini, 0);
                noti = new Notification.Builder(getApplicationContext())
                        .setTicker("Bus is 2 min away from your stop")
                        .setContentTitle("Bus is 2 min away from your stop.")
                        .setContentText(" ")
                        .setSmallIcon(R.mipmap.aaaa)
                        .addAction(R.mipmap.ic_launcher, " ", pIntent)
                        .addAction(R.mipmap.ic_launcher, " ", pIntent)
                        .setContentIntent(pIntent).getNotification();
                noti.flags = Notification.FLAG_AUTO_CANCEL;
                nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0, noti);
            }
            else if(duration.contentEquals("3 mins")){
                if(mode==3){three.start();}
                ini = new Intent(getApplicationContext(), NotificationView.class);
                pIntent = PendingIntent.getActivity(getApplicationContext(), 0, ini, 0);
                noti = new Notification.Builder(getApplicationContext())
                        .setTicker("Bus is 3 min away from your stop")
                        .setContentTitle("Bus is 3 min away from your stop.")
                        .setContentText(" ")
                        .setSmallIcon(R.mipmap.aaaa)
                        .addAction(R.mipmap.ic_launcher, " ", pIntent)
                        .addAction(R.mipmap.ic_launcher, " ", pIntent)
                        .setContentIntent(pIntent).getNotification();
                noti.flags = Notification.FLAG_AUTO_CANCEL;
                nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0, noti);
            }
            else if(duration.contentEquals("4 mins")){
                if(mode==4){four.start();}
                ini = new Intent(getApplicationContext(), NotificationView.class);
                pIntent = PendingIntent.getActivity(getApplicationContext(), 0, ini, 0);
                noti = new Notification.Builder(getApplicationContext())
                        .setTicker("Bus is 4 min away from your stop")
                        .setContentTitle("Bus is 4 min away from your stop.")
                        .setContentText(" ")
                        .setSmallIcon(R.mipmap.aaaa)
                        .addAction(R.mipmap.ic_launcher, " ", pIntent)
                        .addAction(R.mipmap.ic_launcher, " ", pIntent)
                        .setContentIntent(pIntent).getNotification();
                noti.flags = Notification.FLAG_AUTO_CANCEL;
                nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0, noti);
            }
            else if(duration.contentEquals("5 mins")){
                if(mode==5){five.start();}
                ini = new Intent(getApplicationContext(), NotificationView.class);
                pIntent = PendingIntent.getActivity(getApplicationContext(), 0, ini, 0);
                noti = new Notification.Builder(getApplicationContext())
                        .setTicker("Bus is 5 min away from your stop")
                        .setContentTitle("Bus is 5 min away from your stop.")
                        .setContentText(" ")
                        .setSmallIcon(R.mipmap.aaaa)
                        .addAction(R.mipmap.ic_launcher, " ", pIntent)
                        .addAction(R.mipmap.ic_launcher, " ", pIntent)
                        .setContentIntent(pIntent).getNotification();
                noti.flags = Notification.FLAG_AUTO_CANCEL;
                nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0, noti);
            }

            tvDistanceDuration.setText(duration);
        }
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                final Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progressBar.setVisibility(View.GONE);
                        //infoText.setVisibility(View.VISIBLE);
                        infoText.setText(resultData.getString(Constants.RESULT_DATA_KEY));
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progressBar.setVisibility(View.GONE);
                        //infoText.setVisibility(View.VISIBLE);
                        infoText.setText(resultData.getString(Constants.RESULT_DATA_KEY));
                    }
                });
            }
        }
    }

}




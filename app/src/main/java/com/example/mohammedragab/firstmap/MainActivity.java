package com.example.mohammedragab.firstmap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private TextView source;
    private TextView destination;
    private EditText sLatitude1;
    private EditText sLongtiude1;
    private EditText dLatitude2;
    private EditText dLongtiude2;
    private Button button;
    private  TextView mylocation;
    private  TextView mydestination;


    private GoogleMap mMap;
    boolean mapReady = false;
    MarkerOptions place1;
    MarkerOptions place2;
   private   double lati1;

   private  double longi1;
   private   double lati2;
   private  double longi2;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find text that display distance
        textView = (TextView) findViewById(R.id.distance);
        final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // find edit text and text view
        source = (TextView) findViewById(R.id.sourc);
        destination = (TextView) findViewById(R.id.destination);
        sLatitude1 = (EditText) findViewById(R.id.lat1);
        sLongtiude1 = (EditText) findViewById(R.id.long1);
        dLatitude2 = (EditText) findViewById(R.id.lat2);
        dLongtiude2 = (EditText) findViewById(R.id.long2);
        mylocation=(TextView)findViewById(R.id.sourc);
        mydestination=(TextView)findViewById(R.id.destination);
        mylocation.setText("MyLocation");
         double facaio=distanceBetween(new LatLng(30.044774, 31.236663),new LatLng(29.322870, 30.846750));
        String stringDistance= formatNumber(facaio);
        textView.setText(stringDistance);

        // find button
        button = (Button) findViewById(R.id.getDistance);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // find string from edittext
                String lat1 = sLatitude1.getText().toString();
                // parse string to double
                lati1 = ParseDouble(lat1);
                String lon1 = sLongtiude1.getText().toString();
                longi1 = ParseDouble(lon1);
                String lat2 = dLatitude2.getText().toString();
                lati2 = ParseDouble(lat2);
                String lon2 = dLongtiude2.getText().toString();
                longi2 = ParseDouble(lon2);
                Log.i("**lat", lat2);
                if(((lati1>=91||lati1<= -91)||(lati2>=91||lati2<= -91))|| ((longi1>=181||longi1<= -181 ||(longi2>=181||longi2<= -181)))){
                    Toast.makeText(MainActivity.this,"wrong Longitude and latitude",Toast.LENGTH_LONG).show();
                    return;}else{
                    CameraPosition place = CameraPosition.builder()
                            .target(new LatLng(lati1, longi1))
                            .zoom(3)
                            .bearing(6)
                            .tilt(45)
                            .build();
                    flyTo(place);

                    double xy1 = distanceBetween(new LatLng(lati1, longi1), new LatLng(lati2, longi2));
                    String distanceis = formatNumber(xy1);
                    textView.setText(distanceis);
                    place1= new MarkerOptions().position(new LatLng(lati1,longi1))
                            .title("i am here").icon(BitmapDescriptorFactory.fromResource(R.drawable.place));
                    place2= new MarkerOptions().position(new LatLng(lati2,longi2))
                            .title("my destination").icon(BitmapDescriptorFactory.fromResource(R.drawable.place));

                    mMap.addPolyline(new PolylineOptions().geodesic(true)
                            .add(new LatLng(lati1, longi1))
                            .add(new LatLng(lati2, longi2))
                    .width(23));

                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(lati1, longi1))
                            .radius(5000)
                            .strokeColor(Color.GREEN)
                            .fillColor(Color.argb(54, 99, 255, 0)));
                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(lati2, longi2))
                            .radius(5000)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.argb(54, 99, 255, 0)));
                    mMap.addMarker(place1);
                    mMap.addMarker(place2);}

            }

        });
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

    }

    // mehtod to parse double from string

    double ParseDouble(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Double.parseDouble(strNumber);
            } catch (Exception e) {
                return -1;   // or some value to mark this field is wrong. or make a function validates field first ...
            }
        } else return 0;
    }

    // get distance
    public static Double distanceBetween(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) {
            return null;
        }
        double vw = SphericalUtil.computeDistanceBetween(point1, point2);
        return vw;
    }

    private String formatNumber(double distance) {
        String unit = "m";
        if (distance < 1) {
            distance *= 1000;
            unit = "mm";
        } else if (distance > 1000) {
            distance /= 1000;
            unit = "km";
        }

        return String.format("%4.3f%s", distance, unit);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mapReady = true;
       mMap = googleMap;

       mMap.addPolyline(new PolylineOptions().geodesic(true)
                       .add(new LatLng(30.044774, 31.236663))
                        .add(new LatLng(29.322870, 30.846750)));
     MarkerOptions   cairo= new MarkerOptions().position(new LatLng(30.044774, 31.236663))
                .title("i am here");
      MarkerOptions  fayoum= new MarkerOptions().position(new LatLng(29.322870, 30.846750))
                .title("i am here");
        mMap.addMarker(cairo);
        mMap.addMarker(fayoum);
        CameraPosition place1 = CameraPosition.builder()
                .target(new LatLng(30.044774, 31.236663))
                .zoom(5)
                .bearing(10)
                .tilt(45)
                .build();
        flyTo(place1);

    }

    private void flyTo(CameraPosition target) {
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(target));


    }
}

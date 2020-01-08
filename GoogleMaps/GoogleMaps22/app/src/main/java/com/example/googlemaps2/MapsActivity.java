package com.example.googlemaps2;

import    android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    private double latitude, longitude;
    private int ProximityRadius = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission(); //checks if user gave permission
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public void onClick(View v)
    {
        String food = "convenience_store", clothes = "clothing_store", entertainment = "bar";
        Object transferData[] = new Object[2]; //use google keyword to search for specific thing


        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();


        switch(v.getId()){


        case R.id.food_Nearby:
                mMap.clear();//remove current markers
                String url = getUrl(latitude, longitude, food); //passing parameters to getUrl method
                transferData[0] = mMap;  //search for groceries
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching For Nearby Supermarkets", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing Nearby Supermarkets", Toast.LENGTH_SHORT).show(); //displaying messages

                break;


            case R.id.clothes_Nearby:
                System.out.println("THIS IS THE CLOTHES BUTTON"+v.getId());
                mMap.clear();//remove current markers
                url = getUrl(latitude, longitude, clothes); //passing parameters to getUrl method
                transferData[0] = mMap;  //search for clothes
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching For Nearby Clothes Stores", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing Nearby Clothes Stores", Toast.LENGTH_SHORT).show(); //displaying messages

                break;


            case R.id.entertainment_Neaby:
                mMap.clear();//remove current markers
                url = getUrl(latitude, longitude, entertainment); //passing parameters to getUrl method
                transferData[0] = mMap;  //search for entertainment
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching For Nearby Entertainment", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing Nearby Entertainment", Toast.LENGTH_SHORT).show(); //displaying messages

                break;

        }
    }


    private String getUrl(double latitude, double longitude, String nearbyPlace) //declaring type of data
    {
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=" + latitude + "," +longitude); //gets the longitude and latitude of places
        googleURL.append("&radius=" + ProximityRadius); //finds within the radius
        googleURL.append("&type=" + nearbyPlace); //grabs all the data and outputs
        googleURL.append("&sensor=true");
        googleURL.append("&key=" + "AIzaSyAGsCfnIUERf13Y24O4CFUsajxKBsLWGRw"); //the API key

        Log.d("MapsActivity", "url =" + googleURL.toString()); //collects al the places with their data

        return googleURL.toString(); //outputs results

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();

            mMap.setMyLocationEnabled(true);
        }
    }


    public boolean checkUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { //if permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) { //show permission screen
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);

            }     //sends request to get permission, if permission is granted allow if not dont
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if (googleApiClient == null)
                        {
                            buildGoogleApiClient();  //Permission Granted
                        }
                    }
                    mMap.setMyLocationEnabled(true);
                } else
                    {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;

        }
    }

    protected  synchronized  void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location)
    {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        lastLocation = location;

        if (currentUserLocationMarker != null)
        {  //replaces marker and puts it at new location
            currentUserLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Users Current Location"); //Displays marker on the location
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

        currentUserLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(1)); //moves camera to currentlocation and zooms

        if (googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
     locationRequest = new LocationRequest();
     locationRequest.setInterval(1100); //Gets location and how often to change the position
     locationRequest.setFastestInterval(1100);
     locationRequest.setPriority(locationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


     if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
     {
         LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

     }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

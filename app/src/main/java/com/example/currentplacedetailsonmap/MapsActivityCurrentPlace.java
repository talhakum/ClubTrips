package com.example.currentplacedetailsonmap;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsActivityCurrentPlace extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final String TAG = MapsActivityCurrentPlace.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    LocationRequest mLocationRequest;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    private LocationCallback mLocationCallback;

    private Marker mLastKnownMarker;
    private List<Marker> mLastKnownFriendMarkers = new ArrayList<>();

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private static final int MILLISECONDS_PER_SECOND = 1000;

    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                if (mLastKnownMarker != null) {
                    mLastKnownMarker.remove();
                }
                Log.e("username", SaveSharedPreference.getUserName(MapsActivityCurrentPlace.this));
                for (Location location : locationResult.getLocations()) {
                    Log.e("test", location.getLatitude() + " " + location.getLongitude());
                    mLastKnownMarker = mMap.addMarker(new MarkerOptions()
                            .title(SaveSharedPreference.getUserName(MapsActivityCurrentPlace.this))
                            .position(new LatLng(location.getLatitude(), location.getLongitude())));
//                            .snippet("Actually this is so easy"));

                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                    DatabaseReference myRef = database.getReference("locations").child(SaveSharedPreference.getGroupName(MapsActivityCurrentPlace.this).toString());

                    myRef.child(SaveSharedPreference.getUserName(MapsActivityCurrentPlace.this).toString()).child("lat").setValue(location.getLatitude());
                    myRef.child(SaveSharedPreference.getUserName(MapsActivityCurrentPlace.this).toString()).child("lng").setValue(location.getLongitude());


                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> list = dataSnapshot.getChildren();

                            Iterator<Marker> iterator = mLastKnownFriendMarkers.iterator();

                            while (iterator.hasNext()) {
                                Marker mLastKnownFriendMarker = iterator.next();
                                mLastKnownFriendMarker.remove();
                                iterator.remove();
                            }

                            for (DataSnapshot dataSnapshot1 : list) {
                                if (!dataSnapshot1.getKey().equals(SaveSharedPreference.getUserName(MapsActivityCurrentPlace.this).toString())) {
                                    Log.e("friendtest: ", dataSnapshot1.getKey() + " lat: " + dataSnapshot1.child("lat").getValue() + " lng: " + dataSnapshot1.child("lng").getValue());
                                    Double lat = (Double) dataSnapshot1.child("lat").getValue();
                                    Double lng = (Double) dataSnapshot1.child("lng").getValue();
                                    Marker willAdd = mMap.addMarker(new MarkerOptions()
                                            .title(dataSnapshot1.getKey())
                                            .position(new LatLng(lat, lng))
//                                            .snippet(dataSnapshot1.getKey())
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                                    mLastKnownFriendMarkers.add(willAdd);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * This method moves camera to clicked user
     *
     * @param clickedUser
     */
    private void focusClickedUser(final String clickedUser) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("locations").child(SaveSharedPreference.getGroupName(MapsActivityCurrentPlace.this).toString());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> list = dataSnapshot.getChildren();

                for (DataSnapshot dataSnapshot1 : list) {
                    if (dataSnapshot1.getKey().equals(clickedUser)) {
                        Double lat = (Double) dataSnapshot1.child("lat").getValue();
                        Double lng = (Double) dataSnapshot1.child("lng").getValue();
//                    Move camera to clicked user
                        Log.v("testNew", String.valueOf(lat + " " + lng));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(lat,
                                        lng), DEFAULT_ZOOM));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("locations").child(SaveSharedPreference.getGroupName(MapsActivityCurrentPlace.this).toString());

        myRef.child(SaveSharedPreference.getUserName(MapsActivityCurrentPlace.this).toString()).setValue(null);

        super.onStop();
    }

    /**
     * Sets up the options menu.
     *
     * @param menu The options menu.
     * @return Boolean.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.current_place_menu, menu);
        return true;
    }

    /**
     * Handles a click on the menu option to get a place.
     *
     * @param item The menu item to handle.
     * @return Boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.remove) {
            mMap.clear();
        } else if (item.getItemId() == R.id.addfriend) {
//            mMap.addMarker(new MarkerOptions()
//                    .title("Friend")
//                    .position(new LatLng(37.424,-122.084))
//                    .snippet("Actually this is so easy"));

            View promptView = getLayoutInflater().inflate(R.layout.prompts, null);

            final EditText longtitude = (EditText) promptView.findViewById(R.id.editTextDialogUserInput);
//
            final EditText latitude = (EditText) promptView.findViewById(R.id.editTextDialogUserInput2);
//
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapsActivityCurrentPlace.this);
//
            alertDialogBuilder.setView(promptView);
//
            alertDialogBuilder.setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mMap.addMarker(new MarkerOptions()
                                    .title("Friend")
                                    .position(new LatLng(Double.parseDouble(longtitude.getText().toString()), Double.parseDouble(latitude.getText().toString())))
                                    .snippet("Actually this is so easy"));
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
//
            AlertDialog alertDialog = alertDialogBuilder.create();
//
            alertDialog.show();
        }
        return true;
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

//        Move camera if user clicked a user in a list
        Intent intent = getIntent();
        if (intent.getStringExtra("methodName").equals("focusClickedUser")) {
            Log.v("testNew", "onNewIntent");
            String clickedUser = intent.getStringExtra("clickedUser");
            focusClickedUser(clickedUser);
        } else {
            // Get the current location of the device and set the position of the map.
            getDeviceLocation();
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }


    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}

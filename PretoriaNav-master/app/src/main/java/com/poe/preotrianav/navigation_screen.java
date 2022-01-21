package com.poe.preotrianav;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.directions.route.AbstractRouting;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.maps.android.SphericalUtil;
import com.poe.preotrianav.models.landmark;
import com.poe.preotrianav.models.location;
import com.poe.preotrianav.models.userLocation;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class navigation_screen extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, RoutingListener {

    //Loading obj
    Loading l;

    //google map object
    private GoogleMap mMap;
    //
    Double distance;

    //current and destination location objects
    Location myLocation = null;
    public LatLng start = null;
    public LatLng end = null;

    //to get location permissions.
    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission = false;

    //polyline object
    private List<Polyline> polylines = null;
    private Object Iterable;
    private Object LatLng;
    //
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    ProgressBar progressBar;
    private ProgressDialog progressDialog;
    //button to display nearby landmarks
    //button to logout
    Button button,btnLogOut;
    //
     //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_screen);
       /* progressBar = findViewById(R.id.progressBar);*/
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting your current location");
        progressDialog.setCanceledOnTouchOutside(false);
        //Loading Function
        l = new Loading(findViewById(R.id.mainLay),findViewById(R.id.pb));

        //request location permission.
        requestPermision();

        //init google map fragment to show map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        drawerLayout = findViewById(R.id.drawer_layout);

        //
        button  = findViewById(R.id.button);

        button.setOnClickListener(v -> {

            bottom_sheet BottomSheet = new bottom_sheet();
            BottomSheet.show(getSupportFragmentManager(), "");

        });
        //log out
        btnLogOut  = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(v -> {

            Intent intent = new Intent(navigation_screen.this,
                    log_in.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        });
        //display bottomSheet
        //class bottom_sheet
        bottom_sheet BottomSheet = new bottom_sheet();
        BottomSheet.show(getSupportFragmentManager(), "");

        navigationView = findViewById(R.id.navigation_view);
        //
        navigationView.setNavigationItemSelectedListener(item -> {
            // intent to open the sign up page
            Intent open = new Intent(navigation_screen.this,bookmarked.class);
            startActivity(open);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            //showBottomSheetDialog();
           /* bottom_sheet bottomSheet = new bottom_sheet();
            bottomSheet.show(
                    getSupportFragmentManager(), ""
            );*/

            return true;
        });
    }

    public void open_drawer(View v){

        drawerLayout.openDrawer(Gravity.LEFT);

    }
    private void requestPermision() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            locationPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //if permission granted.
                locationPermission = true;


                getMyLocation();

            }

        }
    }

    //to get user location
    private void getMyLocation() {
        //loader
        progressDialog.setMessage("Finding where you are....");
        progressDialog.show();
//        Works as a flag to determined if the user is loading the map for the first time,
//        so that we can zoom in their location
        AtomicBoolean firstLoad = new AtomicBoolean(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(location -> {

            if(firstLoad.get()){
                firstLoad.set(false);
                myLocation=location;

                //Saving the curr loc
                CurrDbSave(location.getLatitude(),location.getLongitude());

                LatLng ltLng=new LatLng(location.getLatitude(),location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        ltLng,13);
                mMap.animateCamera(cameraUpdate);
               /* progressBar.setVisibility(View.GONE);*/
                progressDialog.dismiss();
            }else{
                myLocation=location;

                //Saving the curr loc
                CurrDbSave(location.getLatitude(),location.getLongitude());

                LatLng ltLng=new LatLng(location.getLatitude(),location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(
                        ltLng);
            }

        });

        //get destination location when user click on map
        mMap.setOnMapClickListener(latLng -> {

            end=latLng;
            //clears map after each an every click
            mMap.clear();
            loadMarkers();
            CurrDbSave(myLocation.getLatitude(),myLocation.getLongitude());

            start=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());

            //start route finding
            FindRoutes(start,end);
        });

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        loadMarkers();

        if(locationPermission) {
            getMyLocation();
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("Loc").document("curr").get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        location l = task.getResult().toObject(location.class);

                        LatLng currLoc = new LatLng(l.getLat(),l.getLng());
                        FindRoutes(currLoc, marker.getPosition());
                        Toast.makeText(navigation_screen.this,"Lat: "+l.getLat()+"\nLng: "+l.getLng(),Toast.LENGTH_LONG).show();
                        mMap.clear();
                        loadMarkers();
                    }
                });

                return false;
            }
        });

    }


    // method to determine route, using car by default
    // method to determine route, using car by default
    public void FindRoutes(LatLng Start, LatLng End)
    {

        if(Start==null || End==null) {
            Toast.makeText(navigation_screen.this,"Unable to get location",
                    Toast.LENGTH_LONG).show();
        }

        else
        {
            location loc = new location();
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .waypoints(Start,End)
                    .key("AIzaSyDDK3oPPrdKVojzrSjNToyI65_KYoOEE0U")
                    .build();
            routing.execute();

            //calculating the distance between Start location  and end location
            distance = SphericalUtil.computeDistanceBetween(Start,End);

            // in below line we are displaying a toast, in our distance we are dividing it by 1000 to
            // make in km and formatting it to only 2 decimal places.

            Toast.makeText(this, "Distance is \n "
                            + String.format("%.2f", distance / 1000) + "KM",
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRoutingFailure(com.directions.route.RouteException var1) {
        snackBar(var1.toString());
    }

    private void snackBar(String msg){
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    //Routing call back functions.
    @Override
    public void onRoutingStart() {
        Toast.makeText(navigation_screen.this,"Finding Best Route...",Toast.LENGTH_SHORT).show();
    }

    public void onRoutingSuccess(ArrayList<com.directions.route.Route> var1, int var2) {
        if(polylines!=null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;

        polylines = new ArrayList<>();
        //List<LatLng> path = new ArrayList<>();

        //add route(s) to the map using polyline
        for (int i = 0; i <var1.size(); i++) {

            if(i==var2)
            {
                polyOptions.color(getResources().getColor(R.color.teal_200));
                polyOptions.width(10);
                polyOptions.addAll(var1.get(var2).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng=polyline.getPoints().get(0);
                int k=polyline.getPoints().size();
                polylineEndLatLng=polyline.getPoints().get(k-1);
                polylines.add(polyline);

            }
        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("I'm here");
        mMap.addMarker(startMarker);

        //Add Marker on route ending position
        /*MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Go Here");
        mMap.addMarker(endMarker)*/;

    }

    //If Route finding success..
     @Override
    public void onRoutingCancelled() {
        FindRoutes(start,end);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        FindRoutes(start,end);
    }


    private void loadMarkers(){
        //Loading function
        l = new Loading(findViewById(R.id.mainLay),findViewById(R.id.pb));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

        LoadFirebase();

        db.collection("Landmarks")
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            landmark mark = document.toObject(landmark.class);

                            LatLng ll = new LatLng(mark.getLat(), mark.getLng());
                            MarkerOptions marker = new MarkerOptions();
                            marker.position(ll);
                            marker.title(mark.getTitle());
                            mStorageRef.child(mark.getPhotoRef()).getBytes(1024 * 1024).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                                @Override
                                public void onComplete(@NonNull Task<byte[]> task) {
                                    if(task.isSuccessful()){
                                        ByteArrayInputStream is = new ByteArrayInputStream(task.getResult());
                                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                                        marker.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                                        mMap.addMarker(marker);
                                    }else{
                                        snackBar("Firebase Error: "+task.getException());
                                        mMap.addMarker(marker);
                                    }
                                }
                            });
//                            Log.i(document.getId(),"\""+loc.getTitle()+"\""+", "+loc.getLat()+", "+loc.getLng());
                        }
                    }else{
                        Log.e("Firebase Failed","Doc Error: ",task.getException());
                    }

                    //Ending the loading
                    l.done();
                });
    }
    //Used to pre load data to any firebase account that will be used
    private void LoadFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<landmark> landmarkList = LandmarksSamples.Landmark();

            for(landmark mark : landmarkList){
            db.collection("Landmarks").document(mark.getTitle()).set(mark);
        }
    }

    /*
    * Saves the user current location in the database
    *
    * it first checks if the user is logged in or not, if the user is we will use their email
    * as a reference and save their location
    * if they are not we will save their information with a random id and save it in the apps
    * shared preferences in order to have the anonymous users keys
    */
    private void CurrDbSave(double lat, double lng){
        String userPrefArg = "Loc";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance() ;


        userLocation curr = new userLocation();

        curr.setLat(lat);
        curr.setLng(lng);

        if(auth.getCurrentUser() != null){

            db.collection("Loc").document(auth.getCurrentUser().getEmail()).set(curr);

        }else{

            SharedPreferences sp = getSharedPreferences("location", Context.MODE_PRIVATE);

            String loc = sp.getString(userPrefArg,"0");

            if(loc.equals("0")||loc.length() < 2){
                db.collection(userPrefArg).add(curr).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("Loc", documentReference.getId());
                        editor.commit();
                    }
                });
            }
            else{
                db.collection("Loc").document(loc).set(curr);
            }

        }

    }

}
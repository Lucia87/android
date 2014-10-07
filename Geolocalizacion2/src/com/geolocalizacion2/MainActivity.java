package com.geolocalizacion2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.geolocalizacion2.HttpConnection;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class MainActivity extends FragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener,LocationListener {

	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	
	private static final LatLng Mi_casa = new LatLng(-34.910242, -57.944417);
	private static final LatLng Teatro_argentino = new LatLng(-34.920458,-57.9526553);
	private static final LatLng Casa_Esteban = new LatLng(-34.916618,-57.9408106);
	GoogleMap map;
	
	private LocationClient mLocationClient;
	Location mCurrentLocation;

	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}

		public void show(FragmentManager supportFragmentManager, String string) {
			// TODO Auto-generated method stub

		}
	}

	/*
	 * Handle results returned to the FragmentActivity by Google Play services
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK:
				/*
				 * Try the request again
				 */

				break;
			}

		}
	}

	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			System.out.println("servicio de google disponible");
			// Continue
			return true;
			// Google Play services was not available for some reason.
			// resultCode holds the error code.
		} else {
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
					resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				// Set the dialog in the DialogFragment
				errorFragment.setDialog(errorDialog);
				// Show the error dialog in the DialogFragment
				errorFragment.show(getSupportFragmentManager(),
						"Location Updates");
			}
			return false;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		this.servicesConnected();
		mLocationClient = new LocationClient(this, this, this);
		

	}

	@Override
	protected void onStart() {
		super.onStart();
		// Connect the client.
		mLocationClient.connect();
	}

	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
		mLocationClient.disconnect();
		super.onStop();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

		// Google Play services can resolve some errors it detects.
		// If the error has a resolution, try sending an Intent to
		// start a Google Play services activity that can resolve
		// error.

		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			showErrorDialog(connectionResult.getErrorCode());
		}
	}

	private void showErrorDialog(int errorCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle dataBundle) {
		// Display the connection status
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
//		mCurrentLocation = mLocationClient.getLastLocation();
//		System.out.println(mCurrentLocation.getLatitude()+"-"+mCurrentLocation.getLongitude());
		
		SupportMapFragment fragmentManager = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
	    map = fragmentManager.getMap();
	    
	    
 
	    
	    //Centra en la ubicacion que se le pasa dentro del mapa
		LatLng lp = new LatLng(-34.921379690174966,-57.954715202392556);
		
//		LatLng MiCasa= new LatLng(-34.910242, -57.944417);
//		LatLng Menacho= new LatLng(-34.9107015,-57.9432675);
//		
		
		MarkerOptions options = new MarkerOptions();
		options.position(Mi_casa);
		options.position(Teatro_argentino);
		map.addMarker(options);
		String url = getMapsApiDirectionsUrl();
		ReadTask downloadTask = new ReadTask();
		downloadTask.execute(url);

		
	    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(lp, 13);
	    map.animateCamera(cameraUpdate);	
	    
	    
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        
        //Acá le indico que habilite mi ubicacion actual.
	    map.setMyLocationEnabled(true);
	    
	    //Agrega un marcador en el mapa
	   // map.addMarker (new MarkerOptions().position(lp).title("La Plata, Bs. As, Argentina"));
	    addMarkers();
	    
	    
//	    PolylineOptions lineas = new PolylineOptions()
//
//        .add(new LatLng(-34.9106916,-57.943816))
//
//        .add(new LatLng(-34.911462,-57.9429356))
//
//        .add(new LatLng(-34.9106564,-57.9418808))
//
//        .add(new LatLng(-34.9097392,-57.942937))
//
//        .add(new LatLng(-34.9106916,-57.943816));
//
//
//
//	    lineas.width(8);
//
//	    lineas.color(Color.BLUE);
//
//
//
//	    map.addPolyline(lineas);
//	    
	}
	
//	---------------------------------------------------------------------------
	private String getMapsApiDirectionsUrl() {
		String waypoints = "waypoints=optimize:true|"
				+ Mi_casa.latitude + "," + Mi_casa.longitude
				+ "|" + "|" + Casa_Esteban.latitude + ","
				+ Casa_Esteban.longitude;

		String sensor = "sensor=false";
		String params = waypoints + "&" + sensor;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + params;
		return url;
	}
 
	
	private void addMarkers() {
		if (map != null) {
			map.addMarker(new MarkerOptions().position(Mi_casa)
					.title("First Point"));
			map.addMarker(new MarkerOptions().position(Casa_Esteban)
					.title("Second Point"));
			
		}
	}
	
	private class ReadTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... url) {
			String data = "";
			try {
				HttpConnection http = new HttpConnection();
				data = http.readUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			new ParserTask().execute(result);
		}
	}

	
	
	private class ParserTask extends
	AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
		String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				PathJSONParser parser = new PathJSONParser();
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
			ArrayList<LatLng> points = null;
			PolylineOptions polyLineOptions = null;

			// traversing through routes
			for (int i = 0; i < routes.size(); i++) {
				points = new ArrayList<LatLng>();
				polyLineOptions = new PolylineOptions();
				List<HashMap<String, String>> path = routes.get(i);
				
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				polyLineOptions.addAll(points);
				polyLineOptions.width(2);
				polyLineOptions.color(Color.BLUE);
			}

			map.addPolyline(polyLineOptions);
		}
	}
	
	
	
	
//	-----------------------------------------------------------------------

	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();

	}



	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	// int id = item.getItemId();
	// if (id == R.id.action_settings) {
	// return true;
	// }
	// return super.onOptionsItemSelected(item);
	// }
	
	
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}


}

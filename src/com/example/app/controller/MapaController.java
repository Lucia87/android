package com.example.app.controller;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.app.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapaController implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	private LocationClient mLocationClient;
	private ActionBarActivity activity;
	private GoogleMap map;

	public MapaController(ActionBarActivity activity) {
		this.activity = activity;
	}

	@Override
	public void onConnected(Bundle arg0) {
		Toast.makeText(this.activity, "Connected", Toast.LENGTH_SHORT).show();

		SupportMapFragment fragmentManager = (SupportMapFragment) this.activity
				.getSupportFragmentManager().findFragmentById(R.id.map);
		map = fragmentManager.getMap();

		// Centra en la ubicacion que se le pasa dentro del mapa
		LatLng lp = new LatLng(-34.921379690174966, -57.954715202392556);

		// MarkerOptions options = new MarkerOptions();
		// options.position(Mi_casa);
		// options.position(Teatro_argentino);
		// map.addMarker(options);
		// String url = getMapsApiDirectionsUrl();
		// ReadTask downloadTask = new ReadTask();
		// downloadTask.execute(url);

		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(lp, 13);
		map.animateCamera(cameraUpdate);

		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		// Acá le indico que habilite mi ubicacion actual.
		map.setMyLocationEnabled(true);

		// Agrega un marcador en el mapa
		// map.addMarker (new
		// MarkerOptions().position(lp).title("La Plata, Bs. As, Argentina"));
		// addMarkers();

	}

	@Override
	public void onDisconnected() {
		Toast.makeText(this.activity, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();

	}

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

		public void show(
				android.support.v4.app.FragmentManager fragmentManager,
				String string) {
			// TODO Auto-generated method stub

		}
	}

	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this.activity);
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
					resultCode, this.activity,
					CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				// Set the dialog in the DialogFragment
				errorFragment.setDialog(errorDialog);
				// Show the error dialog in the DialogFragment
				errorFragment.show(this.activity.getSupportFragmentManager(),
						"Location Updates");
			}
			return false;
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this.activity,
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
			this.activity.showDialog(connectionResult.getErrorCode());
		}

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public void connect() {
		mLocationClient.connect();
	}

	public void disconnect() {
		mLocationClient.disconnect();
	}

}

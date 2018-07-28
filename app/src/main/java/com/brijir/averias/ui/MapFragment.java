package com.brijir.averias.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brijir.averias.R;
import com.brijir.averias.bd.Fault;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener{

    List<Fault> listFaults;
    private GoogleMap mMap;
    private final static int PERM_CODE = 77;
    private final static int REQUEST_CODE = 88;
    private LatLng myPosition;
    MaterialDialog materialDialog;
    MapInterface currentContext;

    public void setListFaults(List<Fault> listFaults) {
        this.listFaults = listFaults;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMapAsync(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        currentContext = (MapInterface) context;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        PermisoUbicacion();

        for (int i=0; i < listFaults.size(); i++) {
            LatLng tempPosition = new LatLng(
                    Double.parseDouble(listFaults.get(i).getUbicacion().getLat()),
                    Double.parseDouble(listFaults.get(i).getUbicacion().getLon()));
            mMap.addMarker(new MarkerOptions()
                    .position(tempPosition)
                    .title(listFaults.get(i).getNombre()))
                    .setTag(listFaults.get(i).getId());
        }
        mMap.setOnInfoWindowClickListener(this);
    }

    private void PermisoUbicacion() {
        int state = ActivityCompat.checkSelfPermission((Activity)currentContext, Manifest.permission.ACCESS_FINE_LOCATION);

        if (state == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        else {
            ActivityCompat.requestPermissions((Activity)currentContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERM_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            PermisoUbicacion();
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        myPosition = latLng;

        materialDialog = new MaterialDialog.Builder((Activity)currentContext)
                .title(R.string.Title)
                .content(R.string.Message)
                .positiveText(R.string.Ok)
                .negativeText(R.string.Cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent newFaultFromMap = new Intent((Activity)currentContext, NewFaultActivity.class);
                        newFaultFromMap.putExtra("LatitudeFaultNew", myPosition.latitude);
                        newFaultFromMap.putExtra("LongitudeFaultNew", myPosition.longitude);
                        startActivity(newFaultFromMap);
                    }
                })
                .show();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        final String idEditMarker = marker.getTag().toString();
        materialDialog = new MaterialDialog.Builder((Activity)currentContext)
                .title(R.string.TitleEdit)
                .content(R.string.MessageEdit)
                .positiveText(R.string.OkEdit)
                .negativeText(R.string.CancelEdit)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent editFaultFromMap = new Intent((Activity)currentContext, EditFaultActivity.class);
                        editFaultFromMap.putExtra("IdFaultEdit", idEditMarker);
                        startActivity(editFaultFromMap);
                    }
                })
                .show();
    }

    public interface MapInterface {}
}
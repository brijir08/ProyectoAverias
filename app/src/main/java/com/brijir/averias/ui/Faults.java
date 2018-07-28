package com.brijir.averias.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.brijir.averias.R;

import butterknife.BindView;

public class Faults extends Fragment {

    @BindView(R.id.rv_ListFaults)
    RecyclerView rvListFaults;

    @BindView(R.id.btn_AddFault)
    Button btnAddFault;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_faults, container, false);
    }
}
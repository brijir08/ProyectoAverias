package com.brijir.averias.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.brijir.averias.R;
import com.brijir.averias.bd.Fault;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FaultFragment extends Fragment {

    @BindView(R.id.rv_ListFaults)
    RecyclerView rvListFaults;

    @BindView(R.id.btn_AddFault)
    Button btnAddFault;

    List<Fault> listFaults;
    private CustomAdapter mCustomAdapter;

    public void setListFaults(List<Fault> listFaults) {
        this.listFaults = listFaults;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Aqui debo setear el adapter y lo que deba hacer
        mCustomAdapter = new CustomAdapter(listFaults);
        rvListFaults.setAdapter(mCustomAdapter);
        final RecyclerView.LayoutManager manager = new LinearLayoutManager(view.getContext());
        rvListFaults.setLayoutManager(manager);
        mCustomAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_faults, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.btn_AddFault)
    public void addFault(View v){
        Intent intentNew = new Intent(v.getContext(), NewFaultActivity.class);
        v.getContext().startActivity(intentNew);
    }
}
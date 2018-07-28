package com.brijir.averias.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.brijir.averias.R;
import com.brijir.averias.bd.Fault;
import com.brijir.averias.services.FaultService;
import com.brijir.averias.services.MyServiceManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements MapFragment.MapInterface {
    @BindView(R.id.vp_Home)
    ViewPager viewPager;

    @BindView(R.id.HomeToolbar)
    Toolbar toolbar;

    @BindView(R.id.tl_Tabs)
    TabLayout tabLayout;

    ViewPagerAdapter pagerAdapter;

    List<Fault> listFaults;
    FaultFragment faultFragment;
    MapFragment mapFragment;

    @Override
    protected void onResume() {
        super.onResume();
        loadFaults();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.ctb_titleH);
        mTitle.setText("Gestión de Averías");

        tabLayout.setupWithViewPager(viewPager);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        faultFragment = new FaultFragment();
        mapFragment = new MapFragment();
    }

    private void loadFaults() {
        FaultService faultService = MyServiceManager.getServiceFault();
        faultService.getListFault().enqueue(new Callback<List<Fault>>() {
            @Override
            public void onResponse(Call<List<Fault>> call, Response<List<Fault>> response) {
                listFaults = response.body();
                faultFragment.setListFaults(listFaults);
                mapFragment.setListFaults(listFaults);
                pagerAdapter.AddFragment(faultFragment, "Lista Averías");
                pagerAdapter.AddFragment(mapFragment, "Mapa");
                viewPager.setAdapter(pagerAdapter);
            }

            @Override
            public void onFailure(Call<List<Fault>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error al consultar la lista de averías.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> titles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        public void AddFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}

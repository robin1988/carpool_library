package sample.carpool.delvelogic.poolmyridesample.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import carpool.delvelogic.library.api.CarpoolRequest;
import carpool.delvelogic.library.models.CarPool;
import sample.carpool.delvelogic.poolmyridesample.Adapter.CarpoolAdapter;
import sample.carpool.delvelogic.poolmyridesample.R;

public class LatestCarpoolActivity extends AppCompatActivity {
    List<CarPool> carPoolList;
    ArrayList<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        carPoolList = new ArrayList<CarPool>();

        final LinearLayout progressLayout = (LinearLayout) findViewById(R.id.progress_layout);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.carpool_list);
        final LinearLayout noCarpoolLayout = (LinearLayout) findViewById(R.id.no_carpool_layout);
        viewList = new ArrayList<View>();
        viewList.add(progressLayout);
        viewList.add(recyclerView);
        viewList.add(noCarpoolLayout);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        CarpoolRequest.fetchLatestCarpools(this, new CarpoolRequest.CarpoolInterface() {
            @Override
            public void onSuccess(ArrayList<CarPool> carpools) {
                carPoolList = carpools;
                recyclerView.setAdapter(new CarpoolAdapter(LatestCarpoolActivity.this, carPoolList));
                if (carPoolList.size() > 0)
                    checkVisibility(recyclerView);
                else
                    checkVisibility(noCarpoolLayout);
            }

            @Override
            public void onError(JSONObject jsonObject) {
                checkVisibility(noCarpoolLayout);
                Toast.makeText(getApplicationContext(), "Some Error Ocurred", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLocationError() {
                checkVisibility(noCarpoolLayout);
                Toast.makeText(getApplicationContext(), "Location Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void checkVisibility(View view) {
        for (View v : viewList) {
            int visibility = view == v ? View.VISIBLE : View.GONE;
            v.setVisibility(visibility);
        }
    }

}

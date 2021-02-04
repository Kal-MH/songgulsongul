package smu.capstone.paper;

import android.os.Build;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.SearchView;

import androidx.appcompat.widget.Toolbar;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class ComuActivity extends AppCompatActivity {

    private SearchView searchView;
    GridView gridView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_home_comu);

        Toolbar toolbar = findViewById(R.id.comu_toolbar);
        setSupportActionBar(toolbar);

        searchView = findViewById(R.id.comu_search);
        gridView = (GridView)findViewById(R.id.comu_grid);

        gridView.setAdapter(new ComuImageAdapter(this));

    }
}

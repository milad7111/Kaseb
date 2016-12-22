package mjkarbasian.moshtarimadar.Setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import mjkarbasian.moshtarimadar.DrawerActivity;
import mjkarbasian.moshtarimadar.R;

public class MySetting extends DrawerActivity {
    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager.beginTransaction().replace(R.id.container, new PreferenceHeader()).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }
}

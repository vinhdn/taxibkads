package bk.vinhdo.taxiads.activitis;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import bk.vinhdo.taxiads.MapsActivity;
import bk.vinhdo.taxiads.R;
import bk.vinhdo.taxiads.activitis.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main,true);
    }

    @Override
    public void setActionView() {
        setVisibleRightImage(true);
        setVisibleLeftImage(true);
        setBackgroundLeftImage(R.drawable.pink_ic_navigation_drawer);
        setBackgroundRightImage(R.drawable.topic_map_ic);
        setBackgroundTitleText("Search e.g Hanoi",android.R.color.transparent);
        final Drawable cd = getResources().getDrawable(R.drawable.ab_background_light);
        layout_actionbar.setBackgroundDrawable(cd);
        cd.setAlpha(0);
    }

    @Override
    protected void customHeaderView() {

    }

    @Override
    public void onLeftHeaderClick() {
        Intent i = new Intent(MainActivity.this, SlideMenuActivity.class);
        startActivity(i);
    }

    @Override
    public void onRightHeaderClick() {
        Intent i = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(i);
    }

    @Override
    protected void initModels(Bundle savedInstanceState) {

    }

    @Override
    protected void initViews() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }
}

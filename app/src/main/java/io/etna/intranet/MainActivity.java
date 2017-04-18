package io.etna.intranet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.nav_mur);

        //Preferences
        setCookie("eyJpZGVudGl0eSI6ImV5SnBaQ0k2Tnprek5Dd2liRzluYVc0aU9pSmlaV1J0YVc1ZmFpSXNJbXh2WjJGeklqcG1ZV3h6WlN3aVozSnZkWEJ6SWpwYkluTjBkV1JsYm5RaVhTd2liRzluYVc1ZlpHRjBaU0k2SWpJd01UY3RNRFF0TVRnZ01URTZNRGM2TlRnaWZRPT0iLCJzaWduYXR1cmUiOiJtNVBcL0RzdHdRSmxJN2t6SG1PWk9EemNZVTNhMmxtdUpRdnJFQ2JQdVBGMmtQRnVzUm80bVY0Q2pza2hnOGxGZndZelMraVFVT2RxaVMxVm5FaEhsY0lTY2tCWGZZVDlLUExCVHk2czFtZ3Y4UkhFbTc4TTBcL1AyU3hqaE1Rd2lRYVVJa2FLMmhSUDZGdkY2UTU2T0JJMWp1XC9qZ01CZkx0dDk1YWRVZGJIa2JtWTk4bVIxSUgyRGRiZXNCd09MREdJNHA2NFlMSStrbk8yb1J6bHRNVzdXMjlzb0tXbU4rNkM2Q25SQ3NRTXRPOCtZN3ExZ0c5aFwvQk5FUDVKbmRTMjY1YUZPTUdzRm4yd1J2dExONmtJUHJxU0tHaDJpY2RVQzJcLzN6ZG9XVkdreGNsMjl6MlY3NktIVld5eWlGUWZOYXZSMUlpdWMxcXdxNGxcLzB3QU9rdWFxZU9BeEN5V3NicVRad3FcLzB5Yk5COXQ5M2U5ano3eGwxK0tiS2ticFlrSExJTlp3K3dXT2k4U21hTkpENmM1NmZKVlp3MTZiMUJxTkV2dkRuRjR1VXMzZ1Y4UEtxanM4SDBqZ3hYb1hsTHRTb010VzYyUHlYRTJtSUtcL3hlUkVST2UweU9nSmhyYmtTVkd5ZHFuRnVuRGU3NnhrczNhcEY1NWZDWXBxUWNWZ2pYbEIrU2tzUlwvQjBQaDJCNFB0WTNINFZuRWdjMk53cEtXTjhmRnVoS055XC9iRGZwc1pqS2FcL2RrK09OYzREXC8yV2gxQms5NzBCdGM5XC8zeVZUS3lUditacnJPTzVCYUhLMlVXTTFtNjBiejVoTWhZVFIxNExQbWVmNnptYzNYd1dxWlFnMFVcL0VDZFVYN3ZMbGcxRDZkMjIyZTFsa05kQVIwUjY2VUxVcTlJPSJ9");
        /*
        setLogin("bedmin_j");
        setLoginId("79034");
        setPromo("Prep 2 - 2020");
        setPromoId("205");
        setMur("Prep 2020");
        setAlternant(true);*/
        Log.d("Cookie:", getCookie());
        Log.d("Login:", getLogin());
        Log.d("LoginId:", getLoginId());
        Log.d("Promo:", getPromo());
        Log.d("PromoId:", getPromoId());
        Log.d("Mur:", getMur());
        Log.d("Alternant:", getAlternant());



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_mur:
                fragment = new MurPromo();
                break;
            case R.id.nav_planning:
                fragment = new Planning();
                break;
            case R.id.nav_activites:
                fragment = new Activites();
                break;
            case R.id.nav_tickets:
                fragment = new Tickets();
                break;
            case R.id.nav_notes:
                fragment = new Notes();
                break;
            case R.id.nav_badges:
                fragment = new Badges();
                break;
            case R.id.nav_qrcode:
                fragment = new QRCode();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }

    public final static String COOKIE = "cookie";
    public final static String LOGIN = "login";
    public final static String LOGIN_ID = "login_id";
    public final static String PROMO = "promo";
    public final static String PROMO_ID = "promo id";
    public final static String MUR = "mur";
    public final static String ALTERNANT = "false";

    public void setLogin(String login) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LOGIN, login);
        editor.commit();
    }

    public String getLogin() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        String login = preferences.getString(LOGIN, "null");
        return login;
    }

    public void setLoginId(String login_id) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LOGIN_ID, login_id);
        editor.commit();
    }

    public String getLoginId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        String login_id = preferences.getString(LOGIN_ID, "null");
        return login_id;
    }

    public void setCookie(String cookie) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(COOKIE, cookie);
        editor.commit();
    }

    public String getCookie() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        String cookie = preferences.getString(COOKIE, "null");
        return cookie;
    }

    public void setPromo(String promo) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PROMO, promo);
        editor.commit();
    }

    public String getPromo() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        String promo = preferences.getString(PROMO, "null");
        return promo;
    }

    public void setPromoId(String promo_id) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PROMO_ID, promo_id);
        editor.commit();
    }

    public String getPromoId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        String promo_id = preferences.getString(PROMO_ID, "null");
        return promo_id;
    }

    public void setMur(String mur) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MUR, mur);
        editor.commit();
    }

    public String getMur() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        String mur = preferences.getString(MUR, "null");
        return mur;
    }

    public void setAlternant(Boolean alternant) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ALTERNANT, "false");
        editor.commit();
    }

    public String getAlternant() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        String alternant = preferences.getString(ALTERNANT, "null");
        return alternant;
    }
}

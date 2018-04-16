package br.pucminas.arthur.lddm.nothing;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @ViewById(R.id.nothing)
    public TextView text;

    @ViewById(R.id.last_seen)
    public TextView last_seen;

    @ViewById(R.id.first_access)
    public TextView first_access;

    private long lastSeen;
    private String method;
    private boolean firstrun;
    private boolean clicked;
    private int minutos, segundos;
    private Date firstAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null){
            firstrun = true;
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onPause(){
        lastSeen = System.currentTimeMillis();
        method = "onStop";
        super.onPause();
    }

    @Override
    protected void onStop(){
        lastSeen = System.currentTimeMillis();
        method = "onStop";
        super.onStop();
    }

    @Override
    protected void onResume(){
        if(!firstrun) {

                long timeOut = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastSeen);
                minutos = (int) timeOut / 60;
                segundos = (int) timeOut % 60;

            String text;
            if(method == "onStop")
                text = getString(R.string.last_seen2, minutos, segundos);

            else text = getString(R.string.last_seen, minutos, segundos);
            last_seen.setText(text);
            last_seen.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    @Override
    protected void onRestart(){

            long timeOut = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastSeen);
            minutos = (int) timeOut / 60;
            segundos = (int) timeOut % 60;

        String text;
        if(method == "onStop")
            text = getString(R.string.last_seen2, minutos, segundos);

        else text = getString(R.string.last_seen, minutos, segundos);
        last_seen.setText(text);
        last_seen.setVisibility(View.VISIBLE);
        super.onRestart();
    }

    @Override
    protected void onStart(){
        firstAccess = new Date();
        first_access.setText(getString(R.string.first_access, firstAccess.toString()));
        first_access.setVisibility(View.VISIBLE);
        super.onStart();
    }


    @Override
    @Click({R.id.flag_espana, R.id.flag_italia, R.id.flag_portugal, R.id.flag_uk})
    public void onClick(View view) {
        Log.i("Button", "Click");
        Locale locale;
        switch (view.getId()) {
            case R.id.flag_espana:
                locale = new Locale("es", "ES");;
                break;
            case R.id.flag_italia:

                locale = new Locale("it", "IT");
                break;
            case R.id.flag_portugal:
                locale = new Locale("pt", "BR");
                break;
            default:
                case R.id.flag_uk:
                locale = new Locale("en", "GB");
                break;

        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, null);
        int minutos = this.minutos;
        int segundos = this.segundos;
        String text = firstAccess.toString();
        recreate();

        first_access.setText(getString(R.string.first_access, text));
        first_access.setVisibility(View.VISIBLE);


    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstance){

        savedInstance.putLong("last_seen", lastSeen);
        savedInstance.putString("method", method);
        savedInstance.putBoolean("clicked", clicked);
        savedInstance.putInt("minutos", minutos);
        savedInstance.putInt("segundos", segundos);

        super.onSaveInstanceState(savedInstance);
    }

    @Override
    protected void onDestroy(){
        method = "onDestroy";
        lastSeen = System.currentTimeMillis();
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstance){



            lastSeen = savedInstance.getLong("last_seen");
            method = savedInstance.getString("method");
            clicked = savedInstance.getBoolean("clicked");
            String text;
            if (clicked) {
                minutos = savedInstance.getInt("minutos");
                segundos = savedInstance.getInt("segundos");
                text = getString(R.string.last_seen, minutos, segundos);
                clicked = false;
            } else {
                long timeOut = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastSeen);
                minutos = (int) timeOut / 60;
                segundos = (int) timeOut % 60;
                text = getString(R.string.last_seen, minutos, segundos);
            }

            last_seen.setText(text);
            last_seen.setVisibility(View.VISIBLE);

        super.onRestoreInstanceState(savedInstance);
    }
}

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

import java.util.Locale;
import java.util.concurrent.TimeUnit;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @ViewById(R.id.nothing)
    public TextView text;

    @ViewById(R.id.last_seen)
    public TextView last_seen;

    private long lastSeen;
    private String method;
    private boolean stopped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onPause(){
        lastSeen = System.currentTimeMillis();
        method = "onStop";
        stopped = true;
        super.onPause();
    }

    @Override
    protected void onStop(){
        lastSeen = System.currentTimeMillis();
        method = "onStop";
        stopped = true;
        super.onStop();
    }

    @Override
    protected void onResume(){
        if(stopped){
            long timeOut = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastSeen);
            int minutos = (int)timeOut/60;
            int segundos = (int)timeOut%60;
            String text = getString(R.string.last_seen, minutos, segundos, method);
            last_seen.setText(text);
            last_seen.setVisibility(View.VISIBLE);
        }

        super.onResume();
    }

    @Override
    protected void onRestart(){
        long timeOut = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastSeen);
        int minutos = (int)timeOut/60;
        int segundos = (int)timeOut%60;
        String text = getString(R.string.last_seen, minutos, segundos, method);
        last_seen.setText(text);
        last_seen.setVisibility(View.VISIBLE);
        super.onRestart();
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
        recreate();

    }
}

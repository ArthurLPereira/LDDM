package br.pucminas.arthur.lddm.nothing;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @ViewById(R.id.nothing)
    public TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Click({R.id.flag_espana, R.id.flag_italia, R.id.flag_portugal, R.id.flag_uk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.flag_espana:
                text.setText("Nada");
                break;
            case R.id.flag_italia:
                text.setText("Niente");
                break;
            case R.id.flag_portugal:
                text.setText("Nada");
                break;
            case R.id.flag_uk:
                text.setText("Nothing");
        }

    }
}

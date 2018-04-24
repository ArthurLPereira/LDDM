package br.pucminas.arthur.lddm.tp01;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.LayoutRes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import br.pucminas.arthur.lddm.tp01.adapters.RecyclerViewAdapter;

@EActivity(R.layout.activity_contact_creation)
public class ContactCreation extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    @ViewById(R.id.editText1)
    public EditText name;

    @ViewById(R.id.editText)
    public EditText phone;

    @ViewById(R.id.editText3)
    public EditText email;

    @ViewById(R.id.editText4)
    public EditText birthday;

    @ViewById(R.id.recyclerView1)
    public RecyclerView recyclerView;

    private DatePickerDialog datepicker;
    private int year, month, day;
    private Contact contato;
    public LinearLayoutManager linearLayoutManager;

    private List<MyLink> preferences;

    public class MyLink{
        public String title;
        public String url;

        public MyLink(String title, String url){
            this.title = title;
            this.url = url;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new ArrayList<>();
        setContentView(R.layout.activity_contact_creation);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    }


    @Click({R.id.button, R.id.datepick, R.id.preferences})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                sendForm();
                break;
            case R.id.datepick:
                pickDate();
                break;
            case R.id.preferences:
                newLink();
        }
    }

    private void pickDate() {
        datepicker = new DatePickerDialog(this, R.style.Theme_AppCompat_Light_Dialog, this, 2018, 01, 01);
        datepicker.show();
    }

    private void sendForm() {
        if(validFields()) {
            contato = new Contact(name.getText().toString(), email.getText().toString(), phone.getText().toString(), Calendar.getInstance());
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
            intent.putExtra(ContactsContract.Intents.Insert.NAME, contato.getName());
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, contato.getPhone());
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, contato.getEmail());
            if(preferences.size()>0) intent.putExtra(ContactsContract.Intents.Insert.NOTES, getNotes());
            startActivityForResult(intent, 0);
        }
    }


    private String getNotes(){
        String resp = "";
        for(MyLink link : preferences){
            resp = resp + link.title+ ": "+link.url+", \n";
        }
        return resp;
    }
    public void createEvent() {

        Date date = new Date(year-1900, month, day);
        Log.i("teste, ano", Integer.toString(year));
        Log.i("teste, ano", date.toString());
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date.getTime());
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, date.getTime());
        intent.putExtra(CalendarContract.Events.TITLE, "Aniversário de "+contato.getName());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int request, int result, Intent data){
        super.onActivityResult(request, result, data);

        switch (request){
            case 0:
                createEvent();
                break;
            case 1:
                sendWppMessage();
                break;
        }

    }

    public void sendWppMessage(){
        PackageManager pm = getPackageManager();
        try {
            String message = "Olá, "+name.getText().toString()+", salvei seu número na minha lista de contatos";
            Intent sendIntent =new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT,message);
            sendIntent.putExtra("jid", contato.getPhone() +"@s.whatsapp.net");
            sendIntent.setPackage(pm.getPackageInfo("com.whatsapp", 0).packageName);
            startActivity(sendIntent);
        }
        catch (Exception e){
            Log.e("exception",e.toString());
            Toast.makeText(this, "WhatsApp não instalado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        birthday.setText(Integer.toString(dayOfMonth)+"/"+Integer.toString(month)+"/"+Integer.toString(year));
        this.day = dayOfMonth;
        this.month = month;
        this.year = year;
    }

    private boolean validFields(){
        if(name.getText().toString().isEmpty() || name.getText().toString() == "") return false;
        else if(phone.getText().toString().isEmpty() || phone.getText().toString() == "") return false;
        else if(email.getText().toString().isEmpty() || email.getText().toString() == "") return false;
        else if(birthday.getText().toString().isEmpty() || birthday.getText().toString() == "") return false;
        else return true;
    }

    private void newLink(){


        final EditText txtUrl = new EditText(this);
        final EditText title = new EditText(this);

// Set the default text to a link of the Queen
        title.setHint("Titulo");
        txtUrl.setHint("http://www.example.com");
        LinearLayout layout = new LinearLayout(new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light_Dialog));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(title);
        layout.addView(txtUrl);
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light_Dialog))
                .setTitle("New Link")
                .setView(layout)
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        preferences.add(new MyLink(title.getText().toString(), txtUrl.getText().toString()));
                        recyclerView.setLayoutManager(new LinearLayoutManager(getParent()));
                        recyclerView.setAdapter(new RecyclerViewAdapter(preferences));
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
        Log.i("size", Integer.toString(preferences.size()));


    }
}


package br.pucminas.arthur.lddm.tp01;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
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
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.LayoutRes;

import java.util.Calendar;
import java.util.TimeZone;

@EActivity(R.layout.activity_contact_creation)
public class ContactCreation extends AppCompatActivity implements View.OnClickListener {

    @ViewById(R.id.editText1)
    public EditText name;

    @ViewById(R.id.editText)
    public EditText phone;

    @ViewById(R.id.editText3)
    public EditText email;

    @ViewById(R.id.editText4)
    public EditText birthday;

    private DatePickerDialog datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_creation);
    }


    @Click({R.id.button, R.id.datepick})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                sendForm();
                break;
            case R.id.datepick:
                pickDate();
                break;
        }
    }

    private void pickDate() {
        datepicker = new DatePickerDialog(this, R.style.Theme_AppCompat_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthday.setText(Integer.toString(dayOfMonth)+"/"+Integer.toString(month)+"/"+Integer.toString(year));
            }
        }, 2018, 01, 01);
        datepicker.show();
    }

    private void sendForm() {
        Contact contato = new Contact(name.getText().toString(), email.getText().toString(), phone.getText().toString(), Calendar.getInstance());
//        Intent intent = new Intent(Intent.ACTION_INSERT);
//        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
//
//        intent.putExtra(ContactsContract.Intents.Insert.NAME, contato.getName());
//        intent.putExtra(ContactsContract.Intents.Insert.PHONE, contato.getPhone());
//        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, contato.getEmail());
//        //intent.putExtra(ContactsContract.Intents.Insert., contato.getEmail());
//
//
//        startActivityForResult(intent, 0);
        sendWppMessage();

//        PackageManager pm=getPackageManager();
//        try {
//            intent = new Intent(Intent.ACTION_SEND);
//            intent.setType("text/plain");
//            String text = "Olá, " + name.getText().toString() + ", adicionei seu número a minha lista de contatos";
//            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
//            //Check if package exists or not. If not then code
//            //in catch block will be called
//            intent.setPackage("com.whatsapp");
//        } catch (PackageManager.NameNotFoundException e) {
//            Toast.makeText(getApplicationContext(), "WhatsApp não instalado.", Toast.LENGTH_LONG);
//        }

// provide an initial time

    }

    /*public void createEvent(){
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", begintime.getTimeInMillis());
        intent.putExtra("allDay", true);
        intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("endTime", endtime.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", "A Test Event from android app");
        startActivity(intent);
    }*/

    @Override
    protected void onActivityResult(int request, int result, Intent data){
        super.onActivityResult(request, result, data);
        sendWppMessage();
        /*if(request == 0){
            Intent intent = new Intent(Intent.ACTION_INSERT);
            //intent.setType(CalendarContract.Events.)
            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();

// provide an initial time
            long now = System.currentTimeMillis();
            values.put(CalendarContract.Events.DTSTART, now);
            values.put(CalendarContract.Events.DTEND, now + 3600000);
            values.put(CalendarContract.Events.TITLE, "");
            values.put(CalendarContract.Events.CALENDAR_ID, CalendarContract.Calendars.IS_PRIMARY);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

// create the "placeholder" event
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
            long eventId = Long.parseLong(uri.getLastPathSegment());

// launch the editor to edit the "placeholder" event
            intent.setData(ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId));
            startActivityForResult(intent, 1);
        }*/

    }

    public void sendWppMessage(){
        PackageManager pm = getPackageManager();
        try {
            String message = "Olá, "+name.getText().toString()+", salvei seu número na minha lista de contatos";
            Uri uri = Uri.parse("https://api.whatsapp.com/send?phone="+ phone.getText().toString()+"&text="+Uri.encode(message));
            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
            i.setPackage("com.whatsapp");
            i.putExtra(Intent.EXTRA_TEXT, "message");
            startActivity(i);
        }
        catch (Exception e){
            Toast.makeText(this, "WhatsApp não instalado", Toast.LENGTH_SHORT).show();
        }
    }

}

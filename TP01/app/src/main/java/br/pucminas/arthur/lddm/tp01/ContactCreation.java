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
import java.util.Date;
import java.util.TimeZone;

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

    private DatePickerDialog datepicker;
    private int year, month, day;

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
        datepicker = new DatePickerDialog(this, R.style.Theme_AppCompat_Light_Dialog, this, 2018, 01, 01);
        datepicker.show();
    }

    private void sendForm() {
        if(validFields()) {
            Contact contato = new Contact(name.getText().toString(), email.getText().toString(), phone.getText().toString(), Calendar.getInstance());
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
            intent.putExtra(ContactsContract.Intents.Insert.NAME, contato.getName());
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, contato.getPhone());
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, contato.getEmail());

            startActivityForResult(intent, 0);
        }
    }

    public void createEvent(){
        Date date = new Date(year, month, day);
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date.getTime());
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, date.getTime()+60*60*1000);
        intent.putExtra(CalendarContract.Events.TITLE, "A Test Event from android app");
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
}


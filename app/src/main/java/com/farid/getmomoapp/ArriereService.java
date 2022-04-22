package com.farid.getmomoapp;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ArriereService extends Service {
    private MediaPlayer mediaPlayer;
    ListView listView;
    private static final int PERMISSION_REQUEST_READ_CONTACTS = 100;
    ArrayList smsList;
    Handler handler;
    public ArriereService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate(){
        super.onCreate();
        //showContacts();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.little);
    }

    private void showContacts() {
        Uri inboxURI = Uri.parse("content://sms/inbox");
        smsList = new ArrayList();
        ContentResolver cr = getContentResolver();
        Date madate = Calendar.getInstance().getTime();

        DateFormat dateFormatToday = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormatToday.format(madate);

        Cursor c = cr.query(inboxURI, null, null, null, null);
        JSONArray array=new JSONArray();
        while (c.moveToNext()) {
            String Number = c.getString(c.getColumnIndexOrThrow("address")).toString();
            String Body = c.getString(c.getColumnIndexOrThrow("body")).toString();
            String leCasPrincipal = "Vous avez recu ";
            String leSecond = "un transfert de ";

            if (Number.contains("MTN MoMo") && Body.contains("ransaction") && Body.contains("solde")
                    && Body.contains(today)) {
                if (Body.contains(leCasPrincipal)){
                    if(Body.contains(leSecond)) {
                        String BodyRetenu = Body;
                        String montant,exp,exxp,date,datee,numero,reference,referencee = "";
                        String transac,transaction = "";
                        String intr = ":";
                        montant = BodyRetenu.substring(31,BodyRetenu.indexOf("F"));
                        exp = BodyRetenu.substring(40,BodyRetenu.indexOf("("));
                        exxp = exp.substring(exp.indexOf("e"));
                        String mexp = exxp.substring(2);
                        date = BodyRetenu.substring(59,BodyRetenu.indexOf(". Re"));
                        datee = date.substring(date.indexOf("2"));
                        String num = date.substring(date.indexOf("l"));
                        String no = num.substring(num.indexOf("2"));
                        String num2 = date.substring(date.indexOf("l"));
                        numero = datee.substring(datee.indexOf("2"),11);
                        reference = BodyRetenu.substring(BodyRetenu.indexOf("ce:"));
                        referencee = reference.substring(3,reference.indexOf(". N"));
                        String ref = referencee.substring(1);
                        transac = BodyRetenu.substring(BodyRetenu.indexOf("action"));
                        transaction = transac.substring(transac.indexOf(":"));
                        String tr = transaction.substring(transaction.indexOf(":"),11);
                        String no2 = no.substring(0,10);
                        String hour = no.substring(11,19);
                        String trr = tr.substring(2);
                        /*
                        smsList.add("Number: " + Number + "\n" + "" +"\n" +
                                "MONTANT:" + montant + "\n" + "" +"\n" +
                                "Expéditeur:" + mexp + "\n" + "" +"\n" +
                                "Numéro:" + numero + "\n" + "" +"\n" +
                                "Date:" + no2 + "\n" + "" +"\n" +
                                "Heure:" + hour + "\n" + "" +"\n" +
                                "Référence:" + ref + "\n" + "" +"\n" +
                                "Transaction ID:" + trr + "\n" + "" +"\n" +
                                "Body: " + Body + "\n" );
                        */
                        JSONObject obj=new JSONObject();
                        try {
                            obj.put("montant",montant);
                            obj.put("expediteur",mexp);
                            obj.put("numero",numero);
                            obj.put("date",no2);
                            obj.put("heure",hour);
                            obj.put("reference",ref);
                            obj.put("id_de_transaction",trr);
                            obj.put("corps_du_message",Body);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        array.put(obj);
                        System.out.println("premier "+array);
                    }
                    else {
                        String BodyRetenu = Body;
                        String montant1,exp2,exp22,date1,datee1,num,transaction,transac = "";
                        String intru = "e";
                        String intru2 = ":";
                        montant1 = BodyRetenu.substring(15,BodyRetenu.indexOf("d"));
                        exp2 = BodyRetenu.substring(27,BodyRetenu.indexOf("("));
                        date1 = BodyRetenu.substring(BodyRetenu.indexOf("(2"),BodyRetenu.indexOf("."));
                        datee1 = date1.substring(date1.indexOf(" 2"));
                        String numa = date1.substring(date1.indexOf("2"),date1.indexOf(") s"));
                        transac = BodyRetenu.substring(BodyRetenu.indexOf("ID:"));
                        transaction = transac.substring(transac.indexOf(":"),transac.indexOf("F"));
                        String exe = exp2.replace(intru, "");
                        String ex = exp2.contains(intru) ? exe : null;
                        String tra = transaction.replace(intru2,"");
                        String trans = transaction.contains(intru2) ? tra : null;
                        String dat = datee1.substring(1,11);
                        String hours = datee1.substring(12,20);
                        String tra2 = tra.substring(1);
                        String exe2 = exe.substring(1);
                        /*
                        smsList.add("Number: " + Number + "\n" + "" +"\n" +
                                "MONTANT:" + montant1 + "\n" + "" +"\n" +
                                "Expéditeur:" + exe + "\n" + "" +"\n" +
                                "Numéro:" + numa + "\n" + "" +"\n" +
                                "Date:" + dat + "\n" + "" +"\n" +
                                "heure:" + hours + "\n" + "" +"\n" +
                                "Transaction ID:" + tra2 + "\n" + "" +"\n" +
                                "Body: " + Body + "\n" );
                        */
                        JSONObject obj=new JSONObject();
                        try {
                            obj.put("montant",montant1);
                            obj.put("expediteur",exe);
                            obj.put("numero",numa);
                            obj.put("date",dat);
                            obj.put("heure",hours);
                            //obj.put("Référence: ",referencee);
                            obj.put("id_de_transaction",tra);
                            obj.put("corps_du_message",Body);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        array.put(obj);
                        System.out.println("second "+array);
                    }
                }
                else{

                }
            }
            else {

            }

        }
        String url = "http://192.168.1.109:3001/getMomoDataFromApp";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request_json = new JsonArrayRequest(Request.Method.POST, url, array,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Get Final response
                        //Toast.makeText(ReadMsg.this, ""+response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
            }
        });
        request_json.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        request_json.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request_json);
        request_json.setRetryPolicy(new DefaultRetryPolicy(3 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        c.close();
        //System.setProperty("http.keepAlive", "false");
        System.out.println("final "+array);
        //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, smsList);
        //listView.setAdapter(adapter);
        //listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
         handler = new Handler();
        Calendar todayy = Calendar.getInstance();
        todayy.set(Calendar.HOUR_OF_DAY,18);
        todayy.set(Calendar.MINUTE,10);
        todayy.set(Calendar.SECOND,00);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showContacts();
                        mediaPlayer.start();
                        System.out.println("Correct");
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task,todayy.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
        //timer.scheduleAtFixedRate(task,0,3000);
        //return START_NOT_STICKY;
        return START_REDELIVER_INTENT;
    }
    /*
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

     */
}
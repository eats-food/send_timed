package com.mad.send_timed.app;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class timed_send extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timed_send);

        new TimedSocketSend().execute();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timed_send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class TimedSocketSend extends AsyncTask<String, Integer, Integer> {

        // method that we run in the background
        protected Integer doInBackground(String... strings) {

            String name = "191.238.232.145";
            int port = 5000;
            Socket socket;
            try {

                // create new socket //
                socket = new Socket(name, port);

            }
            catch(IOException a) {
                return 0;
            }
            while(true) {

                // create JSONObject
                JSONObject json = new JSONObject();
                // try::catch put a message in JSONObject
                try {

                    json.put("A", "Hello, Josh!");

                } catch (JSONException a) {

                    return 2;
                }
                // try::catch sending JSON via OutputStreamWriter
                try {

                    // create OutputStreamWriter to read JSON string into
                    OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);

                    // Parse JSON to string
                    String json_string = json.toString();
                    // add padding, this is how Server obtains length of JSON String
                    String sho = String.format("%04d", json_string.length()) + String.format("%04d", 1) + json_string;
                    // put JSON string w/ padding into output stream
                    output.write(sho);
                    // flush and close to send message to server
                    output.flush();
                    output.close();

                } catch (IOException a) {

                    return 3;
                }
                try {
                    Thread.sleep(10000);

                } catch(InterruptedException a) {
                    return 4;
                }
            }

        }

        // a few toast messages based on the result of our background thread to let us know
        // what went wrong, if something went wrong.
        protected void onPostExecute(Integer result) {
            Context context = timed_send.this;
            if(result == 0) {

                Toast.makeText(context, "Bad JSocket Creation: IOException thrown", Toast.LENGTH_LONG).show();
            }
            if(result == 2) {

                Toast.makeText(context, "Bad JSON put: JSONException thrown", Toast.LENGTH_LONG).show();
            }
            else if(result == 3) {

                Toast.makeText(context, "Bad JSON sending: IOException thrown", Toast.LENGTH_LONG).show();
            }
            else if(result == 4) {

                Toast.makeText(context, "Bad Sleep: InterruptedException Thrown", Toast.LENGTH_LONG).show();
            }
        }

    }
}

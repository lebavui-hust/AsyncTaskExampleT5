package com.example.asynctaskexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView textView, textView2;
    SlowTask4 task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);
        textView2 = findViewById(R.id.text_view_2);

        findViewById(R.id.button_quick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("Quick work ... done");
            }
        });

        findViewById(R.id.button_slow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // new SlowTask1().execute();
                // new SlowTask2().execute(6);
                // new SlowTask3().execute(10);
                // new SlowTask4().execute(10);

//                task = new SlowTask4();
//                task.execute(20);

                // new GetTask().execute();
                new DownloadTask().execute();
            }
        });

        findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.cancel(true);
            }
        });

        findViewById(R.id.button_task_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SlowTask3().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 10);
            }
        });

        findViewById(R.id.button_task_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SlowTask32().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 20);
            }
        });

        String str = "[\n" +
                "  {\n" +
                "    \"userId\": 1,\n" +
                "    \"id\": 1,\n" +
                "    \"title\": \"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\n" +
                "    \"body\": \"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"userId\": 1,\n" +
                "    \"id\": 2,\n" +
                "    \"title\": \"qui est esse\",\n" +
                "    \"body\": \"est rerum tempore vitae\\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\\nqui aperiam non debitis possimus qui neque nisi nulla\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"userId\": 1,\n" +
                "    \"id\": 3,\n" +
                "    \"title\": \"ea molestias quasi exercitationem repellat qui ipsa sit aut\",\n" +
                "    \"body\": \"et iusto sed quo iure\\nvoluptatem occaecati omnis eligendi aut ad\\nvoluptatem doloribus vel accusantium quis pariatur\\nmolestiae porro eius odio et labore et velit aut\"\n" +
                "  }]";

        try {
            JSONArray jArr = new JSONArray(str);
            for (int i = 0; i < jArr.length(); i++) {
                JSONObject jObj = jArr.getJSONObject(i);
                int id = jObj.getInt("id");
                String title = jObj.getString("title");
                Log.v("TAG", id + " - " + title);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("int_value", 123);
            jsonObject.put("string_value", "Hello World");
            jsonObject.put("boolean_value", true);
            String s = jsonObject.toString();
            Log.v("TAG", s);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    class SlowTask1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            textView.setText("Slow work ... done");
        }
    }

    class SlowTask2 extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            textView.setText("Slow work ... started");
        }

        @Override
        protected Void doInBackground(Integer... params) {
            int n = params[0];
            try {
                Thread.sleep(n * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            textView.setText("Slow work ... done");
        }
    }

    class SlowTask3 extends AsyncTask<Integer, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            textView.setText("Slow work ... started");
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            try {
                int n = params[0];
                for (int i = 0; i < n; i++) {
                    Thread.sleep(1000);
                    publishProgress(i + 1, n);
                }
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            int max = values[1];

            float percent = progress * 100 / max;

            textView.setText(percent + "%");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result)
                textView.setText("Slow work ... done");
            else
                textView.setText("Slow work ... failed");
        }
    }

    class SlowTask32 extends AsyncTask<Integer, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            textView2.setText("Slow work ... started");
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            try {
                int n = params[0];
                for (int i = 0; i < n; i++) {
                    Thread.sleep(700);
                    publishProgress(i + 1, n);
                }
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            int max = values[1];

            float percent = progress * 100 / max;

            textView2.setText(percent + "%");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result)
                textView2.setText("Slow work ... done");
            else
                textView2.setText("Slow work ... failed");
        }
    }

    class SlowTask4 extends AsyncTask<Integer, Integer, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            textView.setText("Slow work ... started");

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Processing");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            try {
                int n = params[0];
                for (int i = 0; i < n; i++) {
                    Thread.sleep(1000);
                    publishProgress(i + 1, n);
                }
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            int max = values[1];

            dialog.setMax(max);
            dialog.setProgress(progress);

            float percent = progress * 100 / max;

            textView.setText(percent + "%");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            dialog.dismiss();
            if (result)
                textView.setText("Slow work ... done");
            else
                textView.setText("Slow work ... failed");
        }
    }

    class GetTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL url = new URL("https://api.apilayer.com/fixer/latest?apikey=n6UDHXoNTnQx3MV5mrqPR7irjlQlNmpy");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                Log.v("TAG", "response code: " + responseCode);

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                String result = "";

                while ((line = reader.readLine()) != null)
                    result = result + line + "\n";

                reader.close();

                Log.v("TAG", result);

                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    class DownloadTask extends AsyncTask<Void, Integer, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Downloading");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL url = new URL("https://lebavui.github.io/videos/ecard.mp4");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                Log.v("TAG", "response code: " + responseCode);

                InputStream is = conn.getInputStream();
                OutputStream os = openFileOutput("download.mp4", MODE_PRIVATE);

                int max = conn.getContentLength();

                byte[] buffer = new byte[2048];
                int len;
                int downloaded = 0;

                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                    downloaded += len;
                    publishProgress(downloaded, max);
                }

                os.flush();
                os.close();
                is.close();

                Log.v("TAG", "Download done");

                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Log.v("TAG", "Download failed");
            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.setProgress(values[0]);
            dialog.setMax(values[1]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
        }
    }
}
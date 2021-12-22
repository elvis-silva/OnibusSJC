package com.nigapps.onibus.sjc;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by elvis on 30/09/17.
 */

public class FetchData extends AsyncTask<Void, Void, Void> {
    private static final String TAG = FetchData.class.getSimpleName();
    String url = "https://drive.google.com/open?id=0B5ehgIRVVrCTV2RlbTV1LW9XVmc";
    String data ="";
    String dataParsed = "no data";
    String singleParsed ="";
    Document doc;

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            doc = Jsoup.parse("http://www.sjc.sp.gov.br/secretarias/mobilidade_urbana/horario-e-itinerario.aspx?acao=d&id_linha=1");
            /*URL url = new URL("https://drive.google.com/file/d/0B5ehgIRVVrCTV2RlbTV1LW9XVmc");//"https://drive.google.com/open?id=0B5ehgIRVVrCTV2RlbTV1LW9XVmc");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }
           Elements anchors = */
            dataParsed = String.valueOf(doc.select(".texto td").size());
            /*for (Element anchor : anchors) {
                dataParsed = anchor.text();
                Log.d(TAG, " In ====> " + dataParsed);
            }

            JSONArray JA = new JSONArray(data);
            for(int i =0 ;i <JA.length(); i++){
                JSONObject JO = (JSONObject) JA.get(i);
                singleParsed =  "Title:" + JO.get("title") + "\n"+
                        "Url:" + JO.get("url") + "\n"+
                        "Resume:" + JO.get("resume") + "\n"+
                        "Source:" + JO.get("source") + "\n";

                dataParsed = dataParsed + singleParsed +"\n" ;


            }

        } catch (MalformedURLException e) {
            e.printStackTrace();*/
        } catch (Exception e)
        {

            e.printStackTrace();// catch (IOException e) {
        }
         //   e.printStackTrace();
      //  } //catch (JSONException e) {
          //  e.printStackTrace();
        //}
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        //MainActivity.data.setText(this.dataParsed);
        Log.d(TAG, " Post ====> " + dataParsed);
    }
}

package com.maps.jay.mapsstudio.sync;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jay on 5/4/16.
 */
public class MapsAdapter extends AsyncTask<String, String, String> {

    public final String LOG_TAG = MapsAdapter.class.getSimpleName();

    public double longitude;
    public double latitude;

    @Override
    protected String doInBackground(String ... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String locationJsonString = null;
        this.postLocation("http://10.0.2.2:9090/location");

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String FORECAST_BASE_URL =
                    params[0];

            Uri builtUri = Uri.parse(FORECAST_BASE_URL);


            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            locationJsonString = buffer.toString();
            //latLong = getLocationDataFromJson(locationJsonString).toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        }
        catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return locationJsonString;
    }

    private void postLocation(String s) {

        String JsonResponse = null;
        String JsonDATA = "{\"longitude\":\"21\", \"latitude\":\"22\"}";

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(s);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            // is output buffer writer
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
//set headers and method
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(JsonDATA);
// json data
            writer.close();
            InputStream inputStream = urlConnection.getInputStream();
//input stream
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");
            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return;
            }
            JsonResponse = buffer.toString();
//response data
            Log.i(LOG_TAG,JsonResponse);
            //send to post execute
            return;




        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return;
    }

    @Override
    protected void onPostExecute(String locationJsonString) {
        /*try {
            Double [] latLong = getLocationDataFromJson(locationJsonString);
            longitude = latLong[0];
            latitude = latLong[1];
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        final String LONGITUDE = "longitude";
        final String LATITUDE = "latitude";

        try{
            Log.i("info",locationJsonString);
            JSONObject positionJson = new JSONObject(locationJsonString);
            //*this.setLongitude(positionJson.getDouble(LONGITUDE));
            //this.setLatitude(positionJson.getDouble(LATITUDE));*//*
            longitude = positionJson.getDouble(LONGITUDE);
            latitude = positionJson.getDouble(LATITUDE);
        }
        catch(JSONException e){
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    /*private Double [] getLocationDataFromJson(String locationJsonStr) throws JSONException{

        final String LONGITUDE = "longitude";
        final String LATITUDE = "latitude";

        try{
            JSONObject positionJson = new JSONObject(locationJsonStr);
            *//*this.setLongitude(positionJson.getDouble(LONGITUDE));
            this.setLatitude(positionJson.getDouble(LATITUDE));*//*
            longitude = positionJson.getDouble(LONGITUDE);
            latitude = positionJson.getDouble(LATITUDE);
        }
        catch(JSONException e){
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        Double [] latLong = {longitude,latitude};

        return latLong;
    }*/

}
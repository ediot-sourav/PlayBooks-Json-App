package com.magicmusic.playbooks;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.InvalidMarkException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.magicmusic.playbooks.PlayBooksAdapter.imageView;

public class QueryUtils {

//    static ImageView imagePhoto;
//    private static final String TAG_IMAGE = "mallThumbnail";

    private QueryUtils(){
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    return url;
    }
    private static String makeHttpRequest(URL url)throws IOException{
        String jsonResponse = "";
        if (url == null){
            return null;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
            Log.e("QueryUtils", "Error response code: " + urlConnection.getResponseCode());
        }
        }catch (IOException e){
            Log.e("QueryUtils", "Problem retrieving the earthquake JSON results.", e);
        }finally {
            if (urlConnection == null){
                urlConnection.disconnect();
            }
            if (inputStream == null){
                inputStream.close();
            }
        }
        return jsonResponse;

    }

    private static String readFromStream(InputStream inputStream)throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }
    private static List<PlayBooks> extractJson(String bookJson){
        if (TextUtils.isEmpty(bookJson)){
            return null;
        }
        List<PlayBooks> books = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(bookJson);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            for (int i = 0; i<jsonArray.length(); i++){
                JSONObject currentBook = jsonArray.getJSONObject(i);
                JSONObject properties = currentBook.getJSONObject("volumeInfo");
                String name = properties.getString("title");
                String bio = properties.getString("subtitle");
                String date = properties.getString("publishedDate");
                String language = properties.getString("language");
                String url = properties.getString("previewLink");
//                JSONObject jsonObj = currentBook.getJSONObject("imageLinks");
//                final String imageUrl  = currentBook.getString("smallThumbnail");
////
//                Picasso.get().load(currentBook.getString(imageUrl)).into(imageView);

                PlayBooks playBooks = new PlayBooks(name, bio, date, language, url);
                books.add(playBooks);
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return books;

    }
    public static List<PlayBooks> fetchBooks(String reqUrl){
        URL url = createUrl(reqUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<PlayBooks> playBooks = extractJson(jsonResponse);
        return playBooks;
    }
}

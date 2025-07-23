package com.example.library.services;

import com.example.library.request_dtos.GoogleBookDto;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleBooksService {

    @Value("${google.books.api.url}")
    private String apiUrl;

    @Value("${google.books.api.key}")
    private String apiKey;

    public GoogleBookDto fetchBookByIsbn(String isbn) throws JSONException {
        RestTemplate restTemplate = new RestTemplate();

        String url = apiUrl + "q=isbn:" + isbn + "&key=" + apiKey;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        GoogleBookDto googleBookDto = new GoogleBookDto();
        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject json = new JSONObject(response.getBody());
            JSONArray items = json.optJSONArray("items");
            if (items != null) {
                JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");


                String thumbnail = null;
                if (volumeInfo.has("imageLinks")) {
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                    thumbnail = imageLinks.optString("thumbnail");
                }
                String publisher = null;
                if (volumeInfo.has("publisher")) {
                    Object publisherObj = volumeInfo.opt("publisher");
                     publisher =  (String) publisherObj;


                }
                Integer averageRating = null;
                if (volumeInfo.has("averageRating")) {
                     averageRating = volumeInfo.optInt("averageRating",0);

                }
                Integer ratingsCount = null;
                if (volumeInfo.has("ratingsCount")) {
                    ratingsCount = volumeInfo.optInt("ratingsCount",0);

                }
                googleBookDto.setThumbnail(thumbnail);
                googleBookDto.setPublisher(publisher);
                googleBookDto.setAverageRating(averageRating);
                googleBookDto.setRatingsCount(ratingsCount);
                return googleBookDto;
            }
        }
    return null;

    }

}


package com.example.googlemaps2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class dataParser //gets place name and langitude and longtitude in an area and sends the data
{
        private HashMap<String, String> getSingleNearbyPlace(JSONObject googlePlaceJSON)
        {
            HashMap<String, String> googlePlaceMap = new HashMap<>(); //holds data
            String NameOfPlace = "-NA-";
            String vicinity = "-NA-";
            String latitude = "";
            String longitude = "";
            String reference = "";

            try
            {
                if (!googlePlaceJSON.isNull(("name")))
                {
                    NameOfPlace = googlePlaceJSON.getString("name");

                }
                if (!googlePlaceJSON.isNull(("vicinity")))
                {
                    NameOfPlace = googlePlaceJSON.getString("vicinity");

                }
                latitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat"); //if statement is fetching Data
                longitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");
                reference = googlePlaceJSON.getString("reference");

                googlePlaceMap.put("place_name", NameOfPlace);
                googlePlaceMap.put("vicinity", vicinity);
                googlePlaceMap.put("lat", latitude); //putting fetched data into hashmap
                googlePlaceMap.put("lng", longitude);
                googlePlaceMap.put("reference", reference);




            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return googlePlaceMap;
        }
        private List<HashMap<String, String>> getAllNearbyPlaces(JSONArray jsonArray)
        {
            int counter = jsonArray.length();

            List<HashMap<String, String>> NearbyPlacesList = new ArrayList<>();

           HashMap<String, String> NearbyPlaceMap = null; //Hashmap to store each place

            for (int i=0; i<counter; i++)
            {
                try {
                    NearbyPlaceMap = getSingleNearbyPlace((JSONObject)jsonArray.get(i));//fetch one place and add it to nearby places list

                    NearbyPlacesList.add(NearbyPlaceMap);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            return NearbyPlacesList;
        }

        public List<HashMap<String, String>> parse(String jSONdata)
        {
            JSONArray jsonArray = null;
            JSONObject jsonObject;

            try
             {
                jsonObject = new JSONObject(jSONdata);

                jsonArray = jsonObject.getJSONArray("results");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return getAllNearbyPlaces(jsonArray); //pass data through getAllNearbyPlaces


        }
}

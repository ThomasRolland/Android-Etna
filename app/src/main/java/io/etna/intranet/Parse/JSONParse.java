package io.etna.intranet.Parse;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import io.etna.intranet.Curl.NetworkService;

/**
 * Created by nextjoey on 20/04/2017.
 */

public class JSONParse {
    public static String parseActivites(JSONObject resobj)
    {
        Iterator<?> keys = resobj.keys();
        JSONArray Final_Array = new JSONArray();
        while(keys.hasNext())
        {
            JSONArray Cour_Array = new JSONArray();
            JSONObject Final_Object = new JSONObject();
            String key = (String)keys.next();
            try
            {
                if (resobj.get(key) instanceof JSONObject)
                {
                    Final_Object.put("key", key);
                    JSONObject xx = new JSONObject(resobj.get(key).toString());
                    JSONArray  project = xx.getJSONArray("project");
                    for(int i = 0; i < project .length(); i++)
                    {
                        JSONObject object3 = project.getJSONObject(i);
                        Final_Object.put("name", object3.getString("name"));
                        Final_Object.put("date", object3.getString("date_end"));
                    }
                    JSONArray cours = xx.getJSONArray("cours");
                    if (cours.length() != 0)
                    {
                        for(int i = 0; i < cours.length(); i++)
                        {
                            JSONObject cour_object = new JSONObject();
                            JSONObject object4 = cours.getJSONObject(i);
                            cour_object.put("name", object4.getString("name"));
                            cour_object.put("id", object4.getString("activity_id"));
                            Cour_Array.put(cour_object);
                        }
                        Final_Object.put("cour", Cour_Array);
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            Final_Array.put(Final_Object);
        }
        return String.valueOf(Final_Array);
    }

    public static String parseBadges(JSONArray resobj)
    {
        JSONArray Final_Array = new JSONArray();
        try
        {
            for(int i = 0; i < resobj.length(); i++)
            {
                JSONObject Final_Object = new JSONObject();
                JSONObject object3 = resobj.getJSONObject(i);
                Final_Object.put("name", object3.getString("name"));
                Final_Object.put("image", object3.getString("image"));
                Final_Array.put(Final_Object);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return String.valueOf(Final_Array);
    }

    public static String parseMurPromo(JSONObject resobj)
    {
        JSONArray Final_Array = new JSONArray();
        try
        {
            JSONArray  hits = resobj.getJSONArray("hits");
            for(int i = 0; i < hits.length(); i++)
            {
                JSONObject Final_Object = new JSONObject();
                JSONObject object3 = hits.getJSONObject(i);
                Final_Object.put("id", object3.getString("id"));
                Final_Object.put("title", object3.getString("title"));
                Final_Object.put("date", object3.getString("created_at").substring(0,10));
                Final_Object.put("message", object3.getJSONArray("messages").getJSONObject(0).getString("content"));
                JSONObject data_user = searchCall_user(object3.getJSONArray("messages").getJSONObject(0).getString("user"));
                Final_Object.put("id_user", data_user.getString("firstname") + " " + data_user.getString("lastname"));
                Final_Array.put(Final_Object);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return String.valueOf(Final_Array);
    }

    public static String parseMurPromoDetails(JSONObject resobj, String idPrincipal)
    {
        JSONArray Final_Array = new JSONArray();
        try
        {
            JSONArray  hits = resobj.getJSONArray("hits");

            for(int i = 0; i < hits.length(); i++)
            {
                JSONObject object3 = hits.getJSONObject(i);
                if (object3.getString("id").equals(idPrincipal)) {
                    JSONArray messages = object3.getJSONArray("messages");
                    for(int j = 1; j < messages.length(); j++) //On elimine le premier message
                    {
                        JSONObject Final_Object = new JSONObject();
                        JSONObject onemessage = messages.getJSONObject(j);
                        Log.d("Message : ", onemessage.toString());
                        Final_Object.put("createur", onemessage.getString("user"));
                        Final_Object.put("date", onemessage.getString("created_at").substring(0,10));
                        Final_Object.put("message", onemessage.getString("content"));
                        JSONObject data_user = searchCall_user(onemessage.getString("user"));
                        Final_Object.put("id_user", data_user.getString("firstname") + " " + data_user.getString("lastname"));
                        Final_Array.put(Final_Object);
                    }
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return String.valueOf(Final_Array);
    }

    public static String parseNotes(JSONArray resobj)
    {
        JSONArray Final_Array = new JSONArray();
        try
        {
            for(int i = 0; i < resobj.length(); i++)
            {
                JSONObject Final_Object = new JSONObject();
                JSONObject object3 = resobj.getJSONObject(i);
                Final_Object.put("UVNom", object3.getString("uv_name"));
                Final_Object.put("UVDescription", object3.getString("uv_long_name"));
                Final_Object.put("projet", object3.getString("activity_name"));
                Final_Object.put("commentaire", object3.getJSONObject("checklist").getJSONArray("comments").getJSONObject(0).getString("comment"));
                Final_Object.put("note", object3.getString("student_mark"));
                Final_Object.put("noteMin", object3.getString("minimal"));
                Final_Object.put("noteMax", object3.getString("maximal"));
                Final_Object.put("noteMoy", object3.getString("average"));
                Final_Array.put(Final_Object);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return String.valueOf(Final_Array);
    }

    public static String parsePlanning(JSONArray resobj)
    {
        JSONArray Final_Array = new JSONArray();
        try
        {
            for (int i = 0; i < resobj.length(); i++) {
                JSONObject Final_Object = new JSONObject();
                JSONObject object3 = resobj.getJSONObject(i);
                Final_Object.put("name", object3.getString("name"));
                Final_Object.put("location", object3.getString("location"));
                Final_Object.put("start", object3.getString("start"));
                Final_Object.put("end", object3.getString("end"));
                Final_Array.put(Final_Object);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return String.valueOf(Final_Array);
    }

    public static String parseTickets(JSONObject resobj)
    {
        JSONArray Final_Array = new JSONArray();
        try
        {
            JSONArray hits = resobj.getJSONArray("data");
            for(int i = 0; i < hits.length(); i++)
            {
                JSONObject Final_Object = new JSONObject();
                JSONObject object3 = hits.getJSONObject(i);
                if (object3.getString("closed_at") == "null") {
                    Final_Object.put("state", "Ouvert");
                }
                else {
                    Final_Object.put("state", "Fermé");
                }
                Final_Object.put("id", object3.getString("id"));
                Final_Object.put("title", object3.getString("title"));
                Final_Object.put("created_at", object3.getString("created_at"));
                Final_Object.put("updated_at", object3.getString("updated_at"));
                Final_Object.put("last_editor", object3.getJSONObject("last_edit").getString("login"));
                Final_Array.put(Final_Object);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return String.valueOf(Final_Array);
    }

    public static String parseTicketsDetails(JSONObject resobj)
    {
        JSONArray Final_Array = new JSONArray();
        try
        {
            JSONArray hits = resobj.getJSONObject("data").getJSONArray("messages");
            for(int i = 0; i < hits.length(); i++)
            {
                JSONObject Final_Object = new JSONObject();
                JSONObject object3 = hits.getJSONObject(i);
                Final_Object.put("message", object3.getString("content"));
                Final_Object.put("created_at", object3.getString("created_at"));
                Final_Object.put("author_login", object3.getJSONObject("author").getString("login"));
                Final_Object.put("author_mail", object3.getJSONObject("author").getString("email"));
                Final_Array.put(Final_Object);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return String.valueOf(Final_Array);
    }

    private static JSONObject searchCall_user(String id) throws JSONException {
        String[] path = {"api", "users", id};
        String[] get = {};
        String[] get_data = {};
        final String data = NetworkService.INSTANCE.search(get, get_data, "https://auth.etna-alternance.net/", path);
        return new JSONObject(data);
    }

}

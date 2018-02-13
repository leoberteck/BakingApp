package com.example.leonardo.bakingapp.api;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.leonardo.bakingapp.api.entity.Recipe;
import com.example.leonardo.bakingapp.util.SimpleIdlingResource;
import com.example.leonardo.bakingapp.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RecipeAPI {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final Gson gson = new GsonBuilder().create();
    private static final String recipeApiUri = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static List<Recipe> getAllRecipies(){
        List<Recipe> recipeList = new ArrayList<>();
        try{
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAccept(Collections.singletonList(new MediaType("application","json", Charset.forName("UTF-8"))));
            HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    recipeApiUri
                    , HttpMethod.GET
                    , requestEntity
                    , new ParameterizedTypeReference<String>() {}
            );
            if(responseEntity != null){
                String body = StringUtils.removeNonASCIIChars(responseEntity.getBody());
                Type listType = new TypeToken<ArrayList<Recipe>>(){}.getType();
                recipeList = gson.fromJson(body, listType);
            }
        } catch (Exception e) {}
        return recipeList;
    }

    public static void getAllRecipiesAsync(OnTaskFinishListener listener){
        getAllRecipiesAsync(listener, null);
    }

    public static void getAllRecipiesAsync(OnTaskFinishListener listener, SimpleIdlingResource idlingResource){
        LoadRecipesTask.run(listener, idlingResource);
    }

    public interface OnTaskFinishListener{
        void onTaskFinish(List<Recipe> recipeList);
    }

    private static class LoadRecipesTask extends AsyncTask<Void, Void, List<Recipe>>{
        @NonNull
        OnTaskFinishListener listener;
        @Nullable
        SimpleIdlingResource idlingResource;

        LoadRecipesTask(@NonNull OnTaskFinishListener listener, SimpleIdlingResource idlingResource) {
            this.listener = listener;
            this.idlingResource = idlingResource;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(idlingResource != null){
                idlingResource.setIdleState(false);
            }
        }

        @Override
        protected List<Recipe> doInBackground(Void... voids) {
            return RecipeAPI.getAllRecipies();
        }

        @Override
        protected void onPostExecute(List<Recipe> recipeList) {
            super.onPostExecute(recipeList);
            listener.onTaskFinish(recipeList);
            if(idlingResource != null){
                idlingResource.setIdleState(true);
            }
        }

        static void run(OnTaskFinishListener listener, SimpleIdlingResource idlingResource){
            new LoadRecipesTask(listener, idlingResource).execute();
        }
    }
}

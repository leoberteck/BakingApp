package com.example.leonardo.bakingapp.api;

import com.example.leonardo.bakingapp.api.entity.Recipe;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public final class RecipeAPI {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String recipeApiUri = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    static {
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
    }

    public static List<Recipe> getAllRecipies(){
        List<Recipe> recipeList = new ArrayList<>();
        ResponseEntity<List<Recipe>> responseEntity = restTemplate.exchange(
            recipeApiUri
            , HttpMethod.GET
            , null
            , new ParameterizedTypeReference<List<Recipe>>() {}
        );
        if(responseEntity != null){
            recipeList = responseEntity.getBody();
        }
        return recipeList;
    }
}

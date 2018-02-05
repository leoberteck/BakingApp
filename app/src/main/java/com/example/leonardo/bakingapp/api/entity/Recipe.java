package com.example.leonardo.bakingapp.api.entity;

import android.text.TextUtils;

import java.util.List;

public class Recipe {
    private int id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private int servings;
    private String image;

    public Recipe() {}

    public Recipe(int id, String name, List<Ingredient> ingredients, List<Step> steps, int servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return A String URL to load a thumbnail for the recipe
     */
    public String getDefaultThumbnailUri(){
        //If there is an image URL, than it has the preference
        if(!TextUtils.isEmpty(getImage())){
            return getImage();
        //If there is no Image, than takes a video URL from the first step that has one
        } else if(getSteps() != null && getSteps().size() > 0) {
            String url = null;
            for (Step step : getSteps()) {
                if(!TextUtils.isEmpty(step.getMediaURL())){
                    url = step.getMediaURL();
                    break;
                }
            }
            return url;
        //If there is no steps in the recipe, than returns null
        } else {
            return null;
        }
    }
}

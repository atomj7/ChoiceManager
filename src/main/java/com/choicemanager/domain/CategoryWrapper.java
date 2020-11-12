package com.choicemanager.domain;

import lombok.Data;

import java.util.ArrayList;

@Data
public class CategoryWrapper {

    private ArrayList<Category> categories;

    public CategoryWrapper() {
        categories = new ArrayList<>();
    }

}

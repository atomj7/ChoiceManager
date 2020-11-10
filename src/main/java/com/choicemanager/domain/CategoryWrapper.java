package com.choicemanager.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class CategoryWrapper {
    @Getter
    @Setter
    private ArrayList<Category> categories;

    public CategoryWrapper() {
        categories = new ArrayList<>();
    }
}

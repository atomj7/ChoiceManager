package com.choicemanager.domain;

import lombok.Data;

import java.util.ArrayList;

@Data
public class CategoryWrapper {

    //@ApiModelProperty(example = "[{\"id\":1,\"name\":\"General\",\"questions\":[{\"id\":1,\"description\":\"Who are your heroes? ... people that stand out and have performed things you would dream of doing. Name 3. Who are they?\",\"type\":\"special\"}]}]")
    private ArrayList<Category> categories;
    private long categoryNumber;

    public CategoryWrapper() {
        categories = new ArrayList<>();
    }

}

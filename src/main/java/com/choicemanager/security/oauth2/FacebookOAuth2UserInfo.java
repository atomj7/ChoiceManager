package com.choicemanager.security.oauth2;

import java.util.Map;

public class FacebookOAuth2UserInfo extends OAuth2UserInfo {

    private final String name;
    private final String surname;

    public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        String[] nameAndSurname = getUserNameAndSurname((String) attributes.get("name"));
        name = nameAndSurname[0];
        surname = nameAndSurname[1];
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        if(attributes.containsKey("picture")) {
            Map<String, Object> pictureObj = (Map<String, Object>) attributes.get("picture");
            if(pictureObj.containsKey("data")) {
                Map<String, Object>  dataObj = (Map<String, Object>) pictureObj.get("data");
                if(dataObj.containsKey("url")) {
                    return (String) dataObj.get("url");
                }
            }
        }
        return null;
    }
    private String[] getUserNameAndSurname(String nameAndSurname) {
        String delimiter = " ";
        return nameAndSurname.split(delimiter);
    }
}

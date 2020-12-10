package com.doctory_client.models;

import java.io.Serializable;

public class SingleAdviceModel implements Serializable {
    private int id;
    private int user_id;
    private int specialization_id;
    private int disease_id;
    private String title;
    private String image;
    private String contents;
    private String created_at;
    private String updated_at;
    private SpecializationModel specialization_fk;
    private DiseaseModel disease_fk;

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getSpecialization_id() {
        return specialization_id;
    }

    public int getDisease_id() {
        return disease_id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getContents() {
        return contents;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public SpecializationModel getSpecialization_fk() {
        return specialization_fk;
    }

    public DiseaseModel getDisease_fk() {
        return disease_fk;
    }
}


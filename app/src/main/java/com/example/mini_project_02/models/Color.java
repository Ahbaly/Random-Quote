package com.example.mini_project_02.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Color {
    String name;
    String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Color(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("name : %s   -    code : %s",getName(),getCode());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Color){
            return Objects.equals(getName(), ((Color) obj).getName());
        }

        return false;
    }
}

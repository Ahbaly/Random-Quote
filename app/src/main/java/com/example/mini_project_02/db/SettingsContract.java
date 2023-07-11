package com.example.mini_project_02.db;

public class SettingsContract {
    public static class Color {
        public static final String TABLE_NAME = "color";
        public static final String COLUMN_NAME_ID = "name";
        public static final String COLUMN_NAME_CODE = "code";
    }

    public static class Setting {
        public static final String TABLE_NAME = "setting";
        public static final String COLUMN_NAME_ID = "name";
        public static final String COLUMN_NAME_VALUE = "value";

    }
}

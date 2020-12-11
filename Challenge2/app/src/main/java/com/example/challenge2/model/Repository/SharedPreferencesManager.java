//package com.example.challenge2.model.Repository;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
//import com.example.challenge2.R;
//import com.example.challenge2.Utils;
//
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * Classe SharedPreferencesManager responsável por gerir os conteúdos armazenados nas Shared
// * Preferences
// */
//public abstract class SharedPreferencesManager {
//
//    /**
//     * Método para obter o String Set das Shared Preferences para uma key
//     *
//     * @param context contexto da aplicação
//     * @param key     chave do String Set
//     * @return coleção das strings armazenadas nas Shared Preferences para a key
//     */
//    public static Set<String> getSharedPreference(Context context, final String key) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),
//                Context.MODE_PRIVATE);
//        return sharedPreferences.getStringSet(key, Collections.emptySet());
//    }
//
//    /**
//     * Método para armazenar um par key-value nas Shared Preferences
//     *
//     * @param context contexto da aplicação
//     * @param key     chave do valor a armazenar
//     * @param value   valor a armazenar
//     */
//    public static void saveSharedPreference(Context context, String key, String value) {
//        Set<String> stringSet = new HashSet<>(getSharedPreference(context, key));
//        stringSet.add(value);
//
//        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name),
//                Context.MODE_PRIVATE).edit();
//        editor.putStringSet(key, stringSet);
//        editor.commit();
//    }
//
//    /**
//     * Método para atualizar o valor de uma chave
//     *
//     * @param context contexto da aplicação
//     * @param key     chave do valor a ser atualizado
//     * @param value   valor novo
//     */
//    public static void updateSharedPreference(Context context, String key, String value) {
//        Set<String> stringSet = new HashSet<>(getSharedPreference(context, key));
//        for (String title : stringSet)
//            if (title.contains(String.valueOf(Utils.getUUIDFromTitle(value)))) {
//                stringSet.remove(title);
//                stringSet.add(value);
//                break;
//            }
//        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name),
//                Context.MODE_PRIVATE).edit();
//        editor.putStringSet(key, stringSet);
//        editor.commit();
//    }
//
//    /**
//     * Método para remover um par key-value das Shared Preferences
//     *
//     * @param context contexto da aplicação
//     * @param key     chave a eliminar
//     * @param value   valor da chave a eliminar
//     */
//    public static void removeSharedPreference(Context context, String key, String value) {
//        Set<String> stringSet = new HashSet<>(getSharedPreference(context, key));
//        stringSet.remove(value);
//
//        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name),
//                Context.MODE_PRIVATE).edit();
//        editor.putStringSet(key, stringSet);
//        editor.commit();
//    }
//}
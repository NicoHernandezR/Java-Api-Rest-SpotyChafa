package com.api.spotychafa.repositories;

import java.util.HashMap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.spotychafa.models.SongModel;

@Repository
public interface IUSongRepository extends JpaRepository<SongModel, Long> {

  public static SongModel jsonLikeToSongModel(String sontJson) {
    HashMap<String, String> resultMap = parseJsonString(sontJson);
    SongModel song = new SongModel();
    song.setId(Long.parseLong(resultMap.get("id")));
    song.setTitle(resultMap.get("title"));
    song.setArtist(resultMap.get("artist"));
    song.setUrl(resultMap.get("url"));
    return song;
  }

  public static HashMap<String, String> parseJsonString(String jsonString) {
    HashMap<String, String> resultMap = new HashMap<>();

    // Elimina espacios en blanco y caracteres no deseados
    jsonString = jsonString.replaceAll("\\s+", "");

    // Elimina las llaves de apertura y cierre del JSON
    jsonString = jsonString.substring(1, jsonString.length() - 1);

    // Divide las parejas clave-valor utilizando la coma como separador
    String[] keyValuePairs = jsonString.split(",");

    // Itera sobre las parejas clave-valor y las agrega al HashMap
    for (String pair : keyValuePairs) {
        String[] entry = pair.split(":");
        String key = entry[0].replaceAll("\"", "");
        String value = entry[1].replaceAll("\"", "");
        resultMap.put(key, value);
    }

    return resultMap;
}

}

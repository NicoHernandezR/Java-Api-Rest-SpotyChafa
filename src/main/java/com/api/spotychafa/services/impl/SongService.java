package com.api.spotychafa.services.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.spotychafa.models.SongModel;
import com.api.spotychafa.repositories.IUSongRepository;

@Service
public class SongService {
  
  private long maxFileSize = 6 * 1024 * 1024;

  @Value("${app.imagenes.directorio}")
  String directorioImagenes;

  @Autowired
  IUSongRepository songRepository;

  public ArrayList<SongModel> getSongs() {
    return (ArrayList<SongModel>) songRepository.findAll();
  }

  public String saveSong(String songJson, MultipartFile file) {

    String errorMessage = null;
    Path path = null;

    try {

      long fileSize = file.getSize();

      if (fileSize > this.maxFileSize){
        return "File size must be less than or equl to 6MB";
      }

      String fileOriginalName = file.getOriginalFilename();
      String fileExtension = fileOriginalName.substring(fileOriginalName.lastIndexOf("."));

      if (!fileExtension.equals(".mp3")){
        return "Only MP3 files are allowed!";
      }

      byte[] bytes = file.getBytes();
      File folder = new File(directorioImagenes);
      if(!folder.exists()) {
        folder.mkdirs();
      }

      String newFileName = UUID.randomUUID().toString() + fileExtension;

      path = Paths.get(directorioImagenes + newFileName);
      Files.write(path, bytes);

      SongModel song = IUSongRepository.jsonLikeToSongModel(songJson);
      song.setUrl(newFileName);
      songRepository.save(song);
      

    } catch (Exception e) {

      errorMessage = "ERROR: Song wasnt upload D:"; 

    } finally {

        if (errorMessage != null) {
            try {
                Files.deleteIfExists(path);
            } catch (IOException ex) {
                errorMessage += " Also, failed to delete the MP3 file.";
            }
     }
    }

    if (errorMessage != null) {
      return errorMessage;
    }

    return "Song Upload succesfully";
  }

  public Boolean deleteSong(Long id) {

    Path path;

    try {

      Optional<SongModel> optionalSong = songRepository.findById(id);

      if (optionalSong.isPresent()) {
        SongModel song = optionalSong.get();
        String url = song.getUrl();
        path = Paths.get(directorioImagenes + url);
        Files.deleteIfExists(path);

      } else {
        return false;

      }

      songRepository.deleteById(id);
      return true;

    } catch (Exception e) {
      return false;
      
    }
  }

  public Boolean deleteAllSongs() {
    try {
        // Obtener todos los registros de la tabla SongModel
        List<SongModel> songs = songRepository.findAll();

        // Eliminar los archivos asociados y luego borrar todos los registros
        for (SongModel song : songs) {
            String url = song.getUrl();
            Path path = Paths.get(directorioImagenes + url);
            Files.deleteIfExists(path);
        }

        // Borrar todos los registros de la tabla SongModel
        songRepository.deleteAll();

        return true;
    } catch (Exception e) {
        return false;
    }
}


}

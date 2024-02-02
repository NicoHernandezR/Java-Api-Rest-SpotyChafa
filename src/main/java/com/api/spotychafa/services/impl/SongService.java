package com.api.spotychafa.services.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.spotychafa.models.SongModel;
import com.api.spotychafa.repositories.IUSongRepository;

@Service
public class SongService {
  
  private long maxFileSize = 6 * 1024 * 1024;

  @Autowired
  IUSongRepository songRepository;

  public ArrayList<SongModel> getSongs() {
    return (ArrayList<SongModel>) songRepository.findAll();
  }

  public String saveSong(String songJson, MultipartFile file) {

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
      File folder = new File("src/main/resources/songs");
      if(!folder.exists()) {
        folder.mkdirs();
      }

      String newFileName = UUID.randomUUID().toString() + fileExtension;

      Path path = Paths.get("src/main/resources/songs/" + newFileName);
      Files.write(path, bytes);

      

    } catch (Exception e) {
      return "ERROR: Song wasnt upload D:";
    }

    SongModel song = IUSongRepository.jsonLikeToSongModel(songJson);
    songRepository.save(song);

    return "Song Upload succesfully";
  }

  public Boolean deleteSong(Long id) {
    try {
      songRepository.deleteById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }


}

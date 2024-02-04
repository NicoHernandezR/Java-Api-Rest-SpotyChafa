package com.api.spotychafa.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.spotychafa.models.SongModel;
import com.api.spotychafa.services.impl.SongService;



@RestController
@RequestMapping("/song")
public class SongController {
  
  @Autowired
  private SongService songService;

  @GetMapping()
  public ArrayList<SongModel> getSongs() {
    return this.songService.getSongs();
  }

  @PostMapping()
  public ResponseEntity<String> saveSong(@RequestParam("song") String songJson, @RequestParam("file") MultipartFile file ) {
    return new ResponseEntity<>(this.songService.saveSong(songJson, file), HttpStatus.OK);
  }

  @DeleteMapping(path = "/{id}")
  public String deleteSongById(@PathVariable("id") Long id){
    boolean ok = this.songService.deleteSong(id);

    if(ok) {
      return "Song with Id" + id + " was deleted";
    }

    return "ERROR: Song with Id" + id + " wasnt deleted!!!!!!";
  }

  @DeleteMapping()
  public String deleteAllSongs(){
    boolean ok = this.songService.deleteAllSongs();

    if(ok) {
      return "All songs has been deleted";
    }

    return "ERROR D:";
  }
  
}

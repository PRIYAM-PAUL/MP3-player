package project.musicplayer;

import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.net.URL;
import java.util.*;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicPlayerController implements Initializable{
    @FXML
    private Label MP3PLAYER;
    @FXML
    private Button Play,Pause,Reset,Previous,Next;
    @FXML
    private Slider VolumeSlider;
    @FXML
    AnchorPane Pane;
    @FXML
    ComboBox<String> Speed;
    private Media media;
    private MediaPlayer mediaPlayer;
    @FXML
    ProgressBar MusicBar;
    private int speeds[]={0,25,50,75,100,125,150,175,200,100000};
    private boolean running;
    ArrayList<File> song;
    int SongIndex;
    Timer timer;
    TimerTask timerstask;
    Random shuffle;
    @FXML CheckBox AutoPlay,Shuffle;
    double end,current;
    public int Shuffle(){
        if(Shuffle.isSelected()) {
            shuffle = new Random();
            SongIndex =shuffle.nextInt(0, song.size() );
        }
        return SongIndex;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      song = new ArrayList<>();
      File directory = new File("E:\\music");
      File[] files=directory.listFiles();
      for(File file:files) {
          song.add(file);
          SongIndex=Shuffle();
      }
        for(int x:speeds) {
            Speed.getItems().add((x) + " %");
            Speed.setOnAction(this::Speed);
            VolumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    mediaPlayer.setVolume(VolumeSlider.getValue() * 0.01);
                }
            });
            while (true) {
                try {
                    media = new Media(song.get(SongIndex).toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    MP3PLAYER.setText(song.get(SongIndex).getName());
                    break;
                } catch (Exception e) {
                    SongIndex++;
                }
            }
        }
    }
    public void Play() {
        Speed(null);
        mediaPlayer.setVolume(VolumeSlider.getValue()*0.01);
        mediaPlayer.play();
        startMusic();
    }
    public void Pause() {
        mediaPlayer.pause();
        stopMusic();
    }
    public void Reset() {
        startMusic();
        mediaPlayer.seek(Duration.seconds(0));
        mediaPlayer.play();
    }
    public void Previous() {
        Shuffle();
        if(SongIndex>0){
        SongIndex--;
        mediaPlayer.stop();
        if(running){
            stopMusic();
        }
            while(true){
                try {
                    media = new Media(song.get(SongIndex).toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    MP3PLAYER.setText(song.get(SongIndex).getName());
                    this.Play();
                    break;
                } catch (Exception e) {
                    SongIndex--;
                }
            }
        }
        else {
          SongIndex=0;
                    mediaPlayer.stop();
            while(true){
                try {
                    if(running){
                        stopMusic();
                    }
                    media = new Media(song.get(SongIndex).toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    MP3PLAYER.setText(song.get(SongIndex).getName());
                    this.Play();
                    break;
                } catch (Exception e) {
                    SongIndex++;
                }
            }
        }
    }
    public void Next() {
        Shuffle();
        if(SongIndex<song.size()-1){
            SongIndex++;
            mediaPlayer.stop();
            if(running){
                stopMusic();
            }
            while(true){
                try {
                    media = new Media(song.get(SongIndex).toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    MP3PLAYER.setText(song.get(SongIndex).getName());
                    this.Play();
                    break;
                } catch (Exception e) {
                    SongIndex++;
                }
            }
        }
        else {
            SongIndex=0;
            mediaPlayer.stop();
            if(running){
                stopMusic();
            }
            while(true){
                try {
                    media = new Media(song.get(SongIndex).toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    MP3PLAYER.setText(song.get(SongIndex).getName());
                    this.Play();
                    break;
                } catch (Exception e) {
                    SongIndex++;
                }
            }
        }
    }
    public void Speed(ActionEvent event) {
        if (Speed.getValue() == null) {
            mediaPlayer.setRate(1);
        } else {
            mediaPlayer.setRate(0.01 * Integer.parseInt(Speed.getValue().substring(0, Speed.getValue().length() - 2)));
        }
    }
    public void startMusic(){
        running=true;
        timer= new Timer();
        timerstask = new TimerTask() {
            @Override
            public void run() {
                current =mediaPlayer.getCurrentTime().toSeconds();
                 end = media.getDuration().toSeconds();
                MusicBar.setProgress(current/end);
                if(current==end){
                    AutoPlay();
                }
//                setProgress();
            }
        };
        timer.scheduleAtFixedRate(timerstask,0,1000);
    }
    public void stopMusic(){
        running=false;
        timer.cancel();
}
public synchronized void AutoPlay(){
    if(AutoPlay.isSelected()){
            mediaPlayer.stop();
           SongIndex++;
            while (true) {
                try {
                    media = new Media(song.get(SongIndex).toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    MP3PLAYER.setText(song.get(SongIndex).getName());
                    break;
                } catch (Exception e) {
                    SongIndex++;
                }
            }
                Play();
        }
    }
public void setProgress(MouseEvent e){
        System.out.println(e.getX());
}
}
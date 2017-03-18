package com.sa.service.music;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.*;
public class MusicService {
    //²¥·Å·½·¨
    public void play(File file) throws FileNotFoundException, JavaLayerException {
    	MusicPlayer musicPlayer = new  MusicPlayer(file);
//            BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(file));
//            Player player = new Player(buffer);
            Thread thread = new Thread(musicPlayer);
            thread.start();
    }
    
    public static void main(String[] args) {
	/*	try {
//			(new File("d:\\What are words .mp3")).
//			System.out.println(.get);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}

package com.sa.service.music;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.player.Player;

public class MusicPlayer implements Runnable {
	File file = null;
	public MusicPlayer(File file){
		this.file = file;
	}
	
	@Override
	public void run() {
		BufferedInputStream buffer;
		try {
			buffer = new BufferedInputStream(new FileInputStream(file));
			Player player = new Player(buffer);
			player.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

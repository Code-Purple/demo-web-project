package edu.csupomona.cs480.songs;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import edu.csupomona.cs480.models.Song;
import edu.csupomona.cs480.models.SongNote;
import edu.csupomona.cs480.models.SongNoteType;

public class SongParser {
	
	public static List<Song> parseAll(File[] files){
		ArrayList<Song> songs = new ArrayList<Song>();
		for(File file: files){
			Song n = parseFile(file);
			if(n != null){
				songs.add(n);
			}else{
				System.out.println("SongParser: Error Parsing File " + file.getName());
			}
		}
		
		return songs;
	}
	
	public static Song parseFile(File file){
		try {
			List<String> lines = Files.readAllLines(file.toPath(), Charset.forName("US-ASCII"));
			
			Song song = new Song();
			
			song.notes = new ArrayList<SongNote>();
			int seq = 0;
			for(String line : lines){
				line = line.toLowerCase();
				
				char[] charArr = line.toCharArray();
				String[] components = line.split(" "); //spaces as ""
				
				//Determine type of line
				if(charArr[0] == '#'){
					//HEADER
					if(components[0].equals("#title"))
						song.name = components[1];
					
					else if(components[0].equals("#artist"))
						song.artist = components[1];
					
					else if(components[0].equals("#bpm"))
						song.BPM = Integer.parseInt(components[1]);
					
					else if(components[0].equals("#gap"))
						song.GAP = Integer.parseInt(components[1]);
					
					
				}else{
					//CONTENT
					SongNote note = new SongNote();
					SongNoteType type;
					if(charArr[0] == ':')
						type = SongNoteType.Regular;
					else if(charArr[0] == '*')
						type = SongNoteType.Golden;
					else if(charArr[0] == 'F')
						type=SongNoteType.Freestyle;
					else if(charArr[0] == '-')
						type = SongNoteType.LineBreak;
					else
						type = SongNoteType.Regular; //shouldn't happen
					
					note.songTypeID = type.ordinal();
					note.sequenceNum = seq++;
					
					if(type != SongNoteType.LineBreak){
						int colNumber = 0;
						int spaceCount = 0;
						
						for(int i = 1; i < components.length; ++i){
							if(components[i] == ""){
								spaceCount += 1;
							}else{
								colNumber += 1;
								
								if(colNumber == 2){
									note.startBeat = Integer.parseInt(components[i]);
								}else if(colNumber == 3){
									note.duration = Integer.parseInt(components[i]);
								}else if(colNumber == 4){
									note.pitch = Integer.parseInt(components[i]);
								}else if(colNumber == 5){
									note.text = "";
									
									for(int j = 0; j < spaceCount; ++j)
										note.text += " ";
									
									note.text += components[i];
								}
									
								spaceCount = 0;
							}
						}
					}else{
						note.duration = 0;
						note.pitch = 0;
					}
					
					
					song.notes.add(note);
					
				}
			}
			
			return song;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}

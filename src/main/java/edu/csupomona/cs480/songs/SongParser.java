package edu.csupomona.cs480.songs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;

import edu.csupomona.cs480.models.Song;
import edu.csupomona.cs480.models.SongNote;
import edu.csupomona.cs480.models.SongNoteType;

public class SongParser {
	
	public static List<Song> parseAll(List<InputStream> streams){
		ArrayList<Song> songs = new ArrayList<Song>();
		for(InputStream stream: streams){
			Song n = parseStream(stream);
			if(n != null){
				songs.add(n);
			}else{
				System.out.println("SongParser: Error Parsing Stream");
			}
		}
		
		return songs;
	}
	
	public static Song parseStream(InputStream stream) {
		String s;
		try {
			s = new String(IOUtils.toByteArray(stream));

			String[] lines = s.split("\n");
			return parseStringLines(lines);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static Song parseStringLines(List<String> lines){
		try {
			
			Song song = new Song();
			
			song.notes = new ArrayList<SongNote>();
			int seq = 0;
			for(String line : lines){
				line = line.toLowerCase();
				
				char[] charArr = line.toCharArray();
				String[] components = line.split(" "); //spaces as ""
				
				//Determine type of line
				if(charArr[0] == '#'){
					String[] headercomps = line.split(":");
					//System.out.println(headercomps[0] + " : " + headercomps[1]);
					//HEADER
					if(headercomps[0].equals("#title"))
						song.name = headercomps[1];
					
					else if(headercomps[0].equals("#artist"))
						song.artist = headercomps[1];
					
					else if(headercomps[0].equals("#bpm")){
						String[] comma = headercomps[1].split(",");
						song.BPM = Integer.parseInt(comma[0]);
					}
					
					else if(headercomps[0].equals("#gap"))
						song.GAP = Integer.parseInt(headercomps[1]);
					
					
				}else{
					//CONTENT
					SongNote note = new SongNote();
					note.text = "";
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
					
					note.noteTypeID = type.ordinal();
					note.sequenceNum = seq++;
					
					if(type != SongNoteType.LineBreak){
						int colNumber = 0;
						int spaceCount = 0;
						
						for(int i = 1; i < components.length; ++i){
							if(components[i].equals("") || components[i].equals(" ")){
								spaceCount += 1;
							}else{
								colNumber += 1;
								
								if(colNumber == 1){
									note.startBeat = Integer.parseInt(components[i]);
								}else if(colNumber == 2){
									note.duration = Integer.parseInt(components[i]);
								}else if(colNumber == 3){
									note.pitch = Integer.parseInt(components[i]);
								}else if(colNumber >= 4){
									
									for(int j = 0; j < spaceCount; ++j)
										note.text += " ";
									
									note.text += components[i];
								}
									
								spaceCount = 0;
							}
						}
						
						for(int i = 0; i < spaceCount; ++i)
							note.text += " ";
					}else{
						note.duration = 0;
						note.pitch = 0;
						note.text = "\n";
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
	
	public static Song parseStringLines(String[] lines){
		return parseStringLines(Arrays.asList(lines));
	}

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
			
			return parseStringLines(lines);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}

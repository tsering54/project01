/**
 * Creates an object of type FoothillTunesStore which parses a data file
 * in JSON format. Reads an input file that contains users selected actions.
 * Prints out a menu with available actions we can take.
 * Example selections: showing all the songs in our personal tunes library,
 *  adding new songs in our tunes library, making a playlist of a certain
 *  duration, etc. Outputs the estimated run time of "findSubset" method.
 *
 * @author Foothill College, Tsering Dolkar
 *
 * REMINDER: Include text cases in addition to those provided.
 *
 *
 * NOTE: Due to a few data points in the json file, such as:
 * ,
{
        "genre": "classic pop and rock",
        "artist_name": "Crosby_ Stills_ Nash and Young",
        "title": "Carry On",
        "duration": "0.80934"
},
  * Modify the constructor for class MillionSongDataSubset to save "duration" as follows:
        int duration = (int)Math.ceil(Double.parseDouble(currentJson.get("duration").toString()));
  *
  */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import cs1c.BubbleSort;
import cs1c.MillionSongDataSubset;
import cs1c.SongEntry;
import cs1c.TimeConverter;

/**
 * An object of a class stores and manages purchased tunes.
 */
public class MyTunes 
{
	private static final int QUIT = 0;
	private static final int HELP_MENU = 1;
	private static final int LIST_SONG_TITLES = 2;
	private static final int LIST_SONGS_BY_GENRE = 3;
	private static final int BUY_SONG_TITLE = 4;
	private static final int CREATE_PLAYLIST = 5;

	private static final boolean ENABLE_RANDOM_PURCHASE = false;
	
	private ArrayList<SongEntry> purchasedTunes;
	private FoothillTunesStore theStore;

    /**
     * Shows the titles of purchased tunes.
     */
	public void showLibrary()
	{
		System.out.println(purchasedTunes);
	}
	
    /**
     * Shows the menu of available acitons.
     */
	public static void printMenu()
	{
		System.out.println("\nMenu:");
		System.out.println("0. Quit");
		System.out.println("1. Output this menu");
		System.out.println("2. Show all song titles");
		System.out.println("3. Show all songs by genre ");
		System.out.println("4. Buy songs by title");
		System.out.println("5. Create a playlist");
		System.out.println("");
	}

	public MyTunes(FoothillTunesStore store)
	{
		theStore = store;
		purchasedTunes = new ArrayList<SongEntry>();
	}
	
	public static ArrayList<String> readTestFile(String path) throws FileNotFoundException
	{
		ArrayList<String> test = new ArrayList<String>();
		Scanner input = new Scanner(new File(path));
		
		while (input.hasNext()) 
		{
			test.add(input.nextLine());
		}
		
		input.close();
		return test;
	}
	
	public ArrayList<SongEntry> getPurchasedTunes()
	{
		return purchasedTunes;
	}
	
	public void addSongs(ArrayList<SongEntry> song)
	{
		for (int i  = 0; i < song.size(); i ++)
		{
			purchasedTunes.add(song.get(i));
		}
	}
	
	public ArrayList<SongEntry> makePlayList(int second)
	{
		ArrayList<ArrayList<SongEntry>> collection = new ArrayList<ArrayList<SongEntry>>();
		ArrayList<SongEntry> subset = new ArrayList<SongEntry>();
		int maxSum = 0;
		
		collection.add(new ArrayList<SongEntry>());
		
		for (int i = 0; i < purchasedTunes.size(); i ++)
		{
			int size = collection.size();
			for (int j = 0; j < size; j ++)
			{
				int sum = getSum(collection.get(j));
				
				if (sum + purchasedTunes.get(i).getDuration() <= second)
				{
					subset = clone(collection.get(j), purchasedTunes.get(i));
					collection.add(subset);					
				}
				if (sum + purchasedTunes.get(i).getDuration() == second)
					return subset;
			}
		}
		
		for (int k = 0; k < collection.size(); k ++)
		{
			if(getSum(collection.get(k)) > maxSum)
			{
				maxSum = getSum(collection.get(k));
				subset = collection.get(k);
				
			}
		}
		
		return subset;
	}
	
	public int getSum(ArrayList<SongEntry> list)
	{
		int sum = 0;
		for (int i = 0; i < list.size(); i ++)
		{
			sum += list.get(i).getDuration();
		}
		
		return sum;
	}
	
	public ArrayList<SongEntry> clone(ArrayList<SongEntry> list, SongEntry elem)
	{
		ArrayList<SongEntry> newLst = new ArrayList<SongEntry>();
		for (int i = 0; i < list.size(); i ++)
		{
			newLst.add(list.get(i));
		}
		newLst.add(elem);
		return newLst;
		
	}
	

	public static void main(String[] args) throws IOException, ParseException {
		final String jsonFilePath = "resources/music_genre_subset.json";
		final String tunesTestFilePath = "resources/test_tunes.txt";

		FoothillTunesStore store = new FoothillTunesStore(jsonFilePath);
		ArrayList<SongEntry> storeTitles = store.getTitles();
		System.out.println("Welcome! We have over " +  storeTitles.size() + " in FoothillTunes store!");

		ArrayList<String> tunesTestFile = MyTunes.readTestFile(tunesTestFilePath);

		MyTunes.printMenu();

		MyTunes personalTunes = new MyTunes(store);

		ArrayList<String>linesInFile = MyTunes.readTestFile(tunesTestFilePath);
		int selection = -1;
		long startTime, estimatedTime;
		

		for (int i = 0; i < linesInFile.size() && selection != QUIT; /*no need to increment here */) 
		{
			String line = linesInFile.get(i++);
			String [] tokens = line.split(" ");
			if (line.contains("selection"))
				selection = Integer.parseInt(tokens[1]);
			else
			{
				// invalid selection format
				System.out.println("WARNING: Invalid selection");
				continue;
			}
			
			System.out.println("\nselected option:" + selection);
			switch (selection) 
			{
			case QUIT:
				break;
			case HELP_MENU:
				MyTunes.printMenu();
				break;
			case LIST_SONG_TITLES:
				System.out.println("Number of titles in personal tunes = " + personalTunes.getPurchasedTunes().size());
				personalTunes.showLibrary();
				break;
			case LIST_SONGS_BY_GENRE:
				
				// capture start time
				startTime = System.nanoTime();

				// implement grouping songs by genre
				  store.groupSongsByGenre();

				// stop and calculate elapsed time
				estimatedTime = System.nanoTime() - startTime;

				// output the result
				store.printNumOfSongsInEachGenre();

				// report algorithm time
				System.out.println("\nAlgorithm Elapsed Time: "
						+ TimeConverter.convertTimeToString(estimatedTime) + "\n");
				break;
			case BUY_SONG_TITLE:
				String title = linesInFile.get(i++);
				System.out.println("selected song title: " + title);

				// capture start time
				startTime = System.nanoTime();

				// implement searching for songs by title
				ArrayList<SongEntry> searchResult = store.buySongByTitle(title);

				// stop and calculate elapsed time
				estimatedTime = System.nanoTime() - startTime;
				
				System.out.println("Found " + searchResult.size() + " two songs:");
				System.out.println(searchResult);

				personalTunes.addSongs(searchResult);
				
				// report algorithm time
				System.out.println("\nAlgorithm Elapsed Time: "
						+ TimeConverter.convertTimeToString(estimatedTime) + "\n");
				break;

			case CREATE_PLAYLIST:
				int numSongsToBuy =  Integer.parseInt(linesInFile.get(i++));
				System.out.println("selected number of songs to buy: " + numSongsToBuy);

				personalTunes.addSongs(store.getFirstNTitles(numSongsToBuy, ENABLE_RANDOM_PURCHASE));

				int lengthInMinutes =  Integer.parseInt(linesInFile.get(i++));
				int seconds = lengthInMinutes * 60;
				System.out.println("selected playlist length (in seconds): " + seconds);

				// capture start time
				startTime = System.nanoTime();
				
				// implement finding subset of songs that is closest to the requested length of play list
				// HINT: Use same approach as buying a subset of groceries within budget 
				ArrayList<SongEntry> myPlayList = personalTunes.makePlayList(seconds);

				// stop and calculate elapsed time
				estimatedTime = System.nanoTime() - startTime;
				
				// output the result
				int totalTime = 0;
				for (SongEntry song : myPlayList)
				{
					totalTime += song.getDuration();
				}
				System.out.println("length of play list (in seconds): " + totalTime);
				System.out.println("songs in play list: " + myPlayList);

				// report algorithm time
				System.out.println("\nAlgorithm Elapsed Time: "
						+ TimeConverter.convertTimeToString(estimatedTime) + "\n");
				break;
			default:
				System.out.println("ERROR : invalid selection.");
				MyTunes.printMenu();
				break;
			} // switch
		}
	}
	
}

class FoothillTunesStore
{
	private ArrayList<SongEntry> tunes;
	private ArrayList<String> genres;
	private MillionSongDataSubset dataSet;
	private ArrayList<Integer> genresNumber;
	
	public FoothillTunesStore(String file) throws IOException, ParseException
	{
		tunes = new ArrayList<SongEntry>();
		genres = new ArrayList<String>();
		
		JSONParser jsonParser = new JSONParser();
		FileReader fileReader = new FileReader(file);
		JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);
		JSONArray allSongs = (JSONArray) jsonObject.get("songs");

		System.out.println("Parsing JSON file...");
		dataSet = new MillionSongDataSubset(allSongs);
		System.out.println("Completed parsing JSON file.");
		
		for (int i = 0; i < dataSet.getArrayOfSongs().length; i ++)
		{
			tunes.add(dataSet.getArrayOfSongs()[i]);
		}
		
	}
	
	
	public ArrayList<SongEntry> getTitles()
	{
		return tunes;
	}
	
	public void groupSongsByGenre()
	{		
		SongEntry.setSortType(SongEntry.SORT_BY_GENRE);
		BubbleSort.sortArray(dataSet.getArrayOfSongs());
		
		tunes = new ArrayList<SongEntry>();
		
		for (int i = 0; i < dataSet.getArrayOfSongs().length; i ++)
		{
			tunes.add(dataSet.getArrayOfSongs()[i]);		
		}	
		
	}
	
	private void countByGenres()
	{
		genres = new ArrayList<String>();
		genresNumber = new ArrayList<Integer>(); 
		
		genres.add(tunes.get(0).getGenre());
		int count = 1;
		for (int i = 1; i < tunes.size(); i ++)
		{
			if(tunes.get(i).getGenre().equals(genres.get(genres.size() - 1)) )
			{
				count ++;	
			}
			else
			{
				genres.add(tunes.get(i).getGenre());
				genresNumber.add(count);
				count = 1;
				
			}
		}
		genresNumber.add(count);
	}
	
	
	public void printNumOfSongsInEachGenre()
	{
		countByGenres();
		for (int i = 0; i < genres.size(); i ++)
		{
			System.out.println(genres.get(i) + ": " + genresNumber.get(i));
		}
	}
	
	public ArrayList<SongEntry> buySongByTitle(String title)
	{
		
		ArrayList<SongEntry> newTunes = new ArrayList<SongEntry>();
		for (int i  = 0; i < tunes.size(); i ++)
		{
			if (tunes.get(i).getTitle().equals(title))
			{
				newTunes.add(tunes.get(i));
			}
		}
		return newTunes;
	}
	
	public ArrayList<SongEntry> getFirstNTitles(int num, boolean random)
	{
		ArrayList<SongEntry> newTunes = new ArrayList<SongEntry>();
		for(int i = 0; i < num; i ++)
		{
			newTunes.add(tunes.get(i));
		}
		
		return newTunes;
	}
}




/**
 * Reads an input file that contains the prices (whole numbers) of the different items.
 * Then stores and outputs a list of items we can buy
 * given the condition of how much money you have in your wallet.
 * We're at a cash only store. So, no checks or credit purchases!
 *
 * @author Foothill College, [YOUR NAME HERE]
 *
 * REMINDER: Include text cases in addition to those provided.
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import cs1c.BubbleSort;
import cs1c.TimeConverter;

public class ShoppingBag 
{
	private ArrayList<Integer> priceOfGroceries;
	
	public static void main(String[] args) throws FileNotFoundException
	{
		final String FILENAME = "resources/groceries.txt";	 // Directory path for Mac OS X
		//final String FILENAME = "resources\groceries.txt"; // Directory path for Windows OS (i.e. Operating System)

		ShoppingBag bag = new ShoppingBag(FILENAME);
		ArrayList<Integer> shoppingList = bag.getPriceOfGroceries();
		System.out.println("Groceries wanted:");
		System.out.println(shoppingList);
		
		
		System.out.println("Enter how much cash you have:");
		Scanner keyboard = new Scanner(System.in);
		int budget = Integer.parseInt(keyboard.next());
		
		long startTime, estimatedTime;
		
		// capture start time
		startTime = System.nanoTime();

		
		// implement finding subset of groceries that is closest to meeting budget
		// NOTE: In this part, you only need to keep track of the price of each item,
		// 		 and not the name of the item you are buying.
		ArrayList<Integer> purchases = bag.findSubset(budget);
		
		
		// stop and calculate elapsed time
		estimatedTime = System.nanoTime() - startTime;
		
		// output the result
		System.out.println("Purchased grocery prices are:");
		System.out.println(purchases);
		
		
		// report algorithm time
		System.out.println("\nAlgorithm Elapsed Time: "
		        + TimeConverter.convertTimeToString(estimatedTime) + ", "
		        + " seconds.\n");
	}
	
	public ShoppingBag(String file) throws FileNotFoundException
	{
		priceOfGroceries = new ArrayList<Integer>();
		Scanner input = new Scanner(new File(file));
		
		while (input.hasNext()) 
		{
			input.next();
			int i = Integer.parseInt(input.next());
			priceOfGroceries.add(i);
		}
		
	}
	
	public ArrayList getPriceOfGroceries()
	{
		return priceOfGroceries;
		
	}
	
	public ArrayList findSubset(int budget)
	{
		ArrayList<ArrayList<Integer>> collection = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> subset = new ArrayList<Integer>();
		int maxSum = 0;
		
		collection.add(new ArrayList<Integer>());
		
		for (int i = 0; i < priceOfGroceries.size(); i ++)
		{
			int size = collection.size();
			for (int j = 0; j < size; j ++)
			{
				int sum = getSum(collection.get(j));
				
				if (sum + priceOfGroceries.get(i) <= budget)
				{
					subset = clone(collection.get(j), priceOfGroceries.get(i));
					collection.add(subset);					
				}
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
	
	public int getSum(ArrayList<Integer> list)
	{
		int sum = 0;
		for (int i = 0; i < list.size(); i ++)
		{
			sum += list.get(i);
		}
		
		return sum;
	}
	
	public ArrayList<Integer> clone(ArrayList<Integer> list, int elem)
	{
		ArrayList<Integer> newLst = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i ++)
		{
			newLst.add(list.get(i));
		}
		newLst.add(elem);
		return newLst;
		
	}
}

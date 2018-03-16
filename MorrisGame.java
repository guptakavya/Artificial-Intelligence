import java.util.*;
import java.io.*;

/*
 	This class serves as my collection of largely static functions which perform vital operations on the 
 	MorrisPositionList object which allows move generation, static evaluations, and other utilities 
*/
public class MorrisGame
{
	/* Opening game stage evaluation suggested by handout */
	public static int statEstOpening(MorrisPositionList board)
	{
		return (board.getNumPieces(posType.W) - board.getNumPieces(posType.B));
	}

	/* MidgameEndgame evaluation suggested by handout */
	public static int statEstMidgameEndgame(MorrisPositionList board)
	{
		int bPieces = board.getNumPieces(posType.B);
		int wPieces = board.getNumPieces(posType.W);
		List<MorrisPositionList> l = generateMovesMidgameEndgame(board);
		int numBMoves = l.size();
		if (bPieces <= 2)
		{
			return 10000;
		}
		else if (wPieces <= 2)
		{
			return -10000;
		}
		else if (bPieces == 0)
		{
			return 10000;
		}
		else
		{
			return 1000*(wPieces - bPieces) - numBMoves;
		}
	}
	
	/* Here I improve the static evaluation by taking into account how many potential mills W has */
	public static int statEstOpeningImproved(MorrisPositionList board)
	{
		return (board.getNumPieces(posType.W) + numPotentialMills(posType.W, board) - board.getNumPieces(posType.B));
	}
	
	/* Here I improve the static evaluation by taking into account how many potential mills W has */
	public static int statEstMidgameEndgameImproved(MorrisPositionList board)
	{
		int bPieces = board.getNumPieces(posType.B);
		int wPieces = board.getNumPieces(posType.W);
		int numPotMill = numPotentialMills(posType.W, board);
		List<MorrisPositionList> l = generateMovesMidgameEndgame(board);
		int numBMoves = l.size();
		if (bPieces <= 2)
		{
			return 10000;
		}
		else if (wPieces <= 2)
		{
			return -10000;
		}
		else if (bPieces == 0)
		{
			return 10000;
		}
		else
		{
			return 1000*(wPieces - bPieces + numPotMill) - numBMoves;
		}
	}
	
	/* Returns the number of positions that pos could take which would result in a mill */
	public static int numPotentialMills(posType pos, MorrisPositionList board)
	{
		int counter = 0;
		for (int i = 0; i< board.size(); i++)
		{
			posType bPos = board.get(i);
			if (bPos == posType.X)
			{
				if (checkAllMills(i, bPos, board))
				{
					counter++;
				}
			}
		}
		return counter;
	}
	
	/*
		Generates all opening stage moves for white from given board
	*/
	public static List<MorrisPositionList> generateMovesOpening(MorrisPositionList board)
	{
		/* Return the list produced by GenerateAdd applied to the board */
		return generateAdd(board);
	}

	/*
		Generates all opening stage moves for black from given board 
	*/
	public static List<MorrisPositionList> generateMovesOpeningBlack(MorrisPositionList board)
	{
		MorrisPositionList tempb = board.getFlipBoard();
		List<MorrisPositionList> moves = generateMovesOpening(tempb);
		for (int i = 0; i < moves.size(); i++)
		{
			MorrisPositionList b = moves.get(i);
			moves.set(i, b.getFlipBoard());
		}
		return moves;
	}

	/*
		Generates a list of all endgame midgame moves for white from given board
	*/
	public static List<MorrisPositionList> generateMovesMidgameEndgame(MorrisPositionList board)
	{
		if (board.getNumPieces(posType.W) == 3)
		{
			return generateHopping(board);
		}
		else
		{
			return generateMove(board);
		}
	}

	
	/*
		Generates a list of all endgame midgame moves for black from given board
	*/
	public static List<MorrisPositionList> generateMovesMidgameEndgameBlack(MorrisPositionList board)
	{
		MorrisPositionList tempb = board.getFlipBoard();
		List<MorrisPositionList> moves = generateMovesMidgameEndgame(tempb);
		ArrayList<MorrisPositionList> out = new ArrayList<MorrisPositionList>();
		for (int i = 0; i < moves.size(); i++)
		{
			MorrisPositionList b = moves.get(i);
			out.add(b.getFlipBoard());
		}
		return out;
	}

	/*
		Generates list of all possible additions for white from given board
	*/
	public static List<MorrisPositionList> generateAdd(MorrisPositionList board)
	{
		ArrayList<MorrisPositionList> l = new ArrayList<MorrisPositionList>();
		for (int i = 0; i < board.size(); i++)
		{
			if (board.get(i) == posType.X)
			{
				MorrisPositionList b = board.getCopy();
				b.set(i, posType.W);
				if (closeMill(i, b))
				{
					int s = l.size();
					l = generateRemove(b, l);
				}
				else
				{
					l.add(b);
				}
			}
		}
		return l;
	}

	/*
		Generates all possible moves for white from the given board when white can hop(numWhite = 2)
	*/
	public static List<MorrisPositionList> generateHopping(MorrisPositionList board)
	{
		ArrayList<MorrisPositionList> l = new ArrayList<MorrisPositionList>();
		for (int i = 0; i < board.size(); i++)
		{
			if (board.get(i) == posType.W)
			{
				for (int j = 0; j < board.size(); j++)
				{
					if (board.get(j) == posType.X)
					{
						MorrisPositionList b = board.getCopy();
						b.set(i, posType.X);
						b.set(j, posType.W);
						if (closeMill(j, b))
						{
							generateRemove(b, l);
						}
						else
						{
							l.add(b);
						}
					}
				}
			}
		}
		return l;
	}


	/*
		Generates all possible moves for white from the given board(both additions and removals)
	*/
	public static List<MorrisPositionList> generateMove(MorrisPositionList board)
	{
		ArrayList<MorrisPositionList> l = new ArrayList<MorrisPositionList>();
		for (int i = 0; i < board.size(); i++)
		{
			if (board.get(i) == posType.W)
			{
				List<Integer> n = neighbors(i);
				for (int j : n)
				{
					if (board.get(j) == posType.X)
					{
						MorrisPositionList b = board.getCopy();
						b.set(i, posType.X);
						b.set(j, posType.W);
						if (closeMill(j, b))
						{
							l = generateRemove(b, l);
						}
						else
						{
							l.add(b);
						}
					}
				}
			}
		}
		return l;
	}

	/*
	 * Generates all possible removals that white could perform from the given board(occurs when white gets a mill)
	*/
	public static ArrayList<MorrisPositionList> generateRemove(MorrisPositionList board, ArrayList<MorrisPositionList> l)
	{
		for (int i = 0; i < board.size(); i++)
		{
			if (board.get(i) == posType.B)
			{
				if (!closeMill(i, board))
				{
					MorrisPositionList b = board.getCopy();
					b.set(i, posType.X);
					l.add(b);
				}
			}
		}
		return l;
	}

	/*
		Stores the neighbors of each vertex j
	*/
	public static List<Integer> neighbors(int j)
	{
		switch(j)
		{
			case 0:
				return Arrays.asList(1, 3, 8);
			case 1:
				return Arrays.asList(0, 2, 4);
			case 2:
				return Arrays.asList(1, 5, 13);
			case 3:
				return Arrays.asList(0, 4, 6, 9);
			case 4:
				return Arrays.asList(1, 3, 5);
			case 5:
				return Arrays.asList(2, 4, 7, 12);
			case 6:
				return Arrays.asList(3, 7, 10);
			case 7:
				return Arrays.asList(5, 6, 11);
			case 8:
				return Arrays.asList(0, 9, 20);
			case 9:
				return Arrays.asList(3, 8, 10, 17);
			case 10:
				return Arrays.asList(6, 9, 14);
			case 11:
				return Arrays.asList(7, 12, 16);
			case 12:
				return Arrays.asList(5, 11, 13, 19);
			case 13:
				return Arrays.asList(2, 12, 22);
			case 14:
				return Arrays.asList(10, 15, 17);
			case 15:
				return Arrays.asList(14, 16, 18);
			case 16:
				return Arrays.asList(11, 15, 19);
			case 17:
				return Arrays.asList(9, 14, 18, 20);
			case 18:
				return Arrays.asList(15, 17, 19, 21);
			case 19:
				return Arrays.asList(12, 16, 18, 22);
			case 20:
				return Arrays.asList(8, 17, 21);
			case 21:
				return Arrays.asList(18, 20, 22);
			case 22:
				return Arrays.asList(13, 19, 21);
			default:
				return (new ArrayList<Integer>());
		}
	}

	/*
		Determines if j closes a mill(ie gets three connected nodes in a row)
	*/
	public static boolean closeMill(int j, MorrisPositionList b)
	{
		posType C = b.get(j);
		if (C == posType.X)
		{
			return false;
		}
		else
		{
			return checkAllMills(j, C, b);
		}
	}
	
	/*
		Checks all possible mills to see if C at j would fill a mill
	*/
	public static boolean checkAllMills(int j, posType C, MorrisPositionList b)
	{
		/*
			The following logic is based on the following list of mills:
			0	1	2		0	3	6		0	8 	20
			11	12	13		14	15	16		15	18	21
			16	19	22		17	18	19		2 	5	7
			20	17	14		20	21	22		22	13	2
			3	4	5		3	9	17		5	12	19
			6	10 	14		7	11	16		8	9	10
		 */
		switch(j)
		{
			case 0:
				return (checkMill(b, C, 1, 2) || checkMill(b, C, 8, 20) || checkMill(b, C, 3, 6));
			case 1:
				return (checkMill(b, C, 0, 2));
			case 2:
				return (checkMill(b, C, 0, 1) || checkMill(b, C, 5, 7) || checkMill(b, C, 13, 22));
			case 3:
				return (checkMill(b, C, 0, 6) || checkMill(b, C, 4, 5) || checkMill(b, C, 9, 17));
			case 4:
				return (checkMill(b, C, 3, 5));
			case 5:
				return (checkMill(b, C, 3, 4) || checkMill(b, C, 2, 7) || checkMill(b, C, 12, 19));
			case 6:
				return (checkMill(b, C, 0, 3) || checkMill(b, C, 10, 14));
			case 7:
				return (checkMill(b, C, 2, 5) || checkMill(b, C, 11, 16));
			case 8:
				return (checkMill(b, C, 0, 20) || checkMill(b, C, 9, 10));
			case 9:
				return (checkMill(b, C, 8, 10) || checkMill(b, C, 3, 17));
			case 10:
				return (checkMill(b, C, 8, 9) || checkMill(b, C, 6, 14));
			case 11:
				return (checkMill(b, C, 7, 16) || checkMill(b, C, 12, 13));
			case 12:
				return (checkMill(b, C, 11, 13) || checkMill(b, C, 5, 19));
			case 13:
				return (checkMill(b, C, 11, 12) || checkMill(b, C, 2, 22));
			case 14:
				return (checkMill(b, C, 17, 20) || checkMill(b, C, 15, 16) || checkMill(b, C, 6, 10));
			case 15:
				return (checkMill(b, C, 14, 16) || checkMill(b, C, 18, 21));
			case 16:
				return (checkMill(b, C, 14, 15) || checkMill(b, C, 19, 22) || checkMill(b, C, 7, 11));
			case 17:
				return (checkMill(b, C, 3, 9) || checkMill(b, C, 14, 20) || checkMill(b, C, 18, 19));
			case 18:
				return (checkMill(b, C, 17, 19) || checkMill(b, C, 15, 21));
			case 19:
				return (checkMill(b, C, 17, 18) || checkMill(b, C, 16, 22) || checkMill(b, C, 5, 12));
			case 20:
				return (checkMill(b, C, 0, 8) || checkMill(b, C, 14, 17) || checkMill(b, C, 21, 22));
			case 21:
				return (checkMill(b, C, 20, 22) || checkMill(b, C, 15, 18));
			case 22:
				return (checkMill(b, C, 2, 13) || checkMill(b, C, 16, 19) || checkMill(b, C, 20, 21));
			default:
				return false;
		}
	}
	
	/*
 		Helper function for above
	 */
	private static boolean checkMill(MorrisPositionList b, posType C, int v1, int v2)
	{
		return (b.get(v1) == C && b.get(v2) == C);
	}

		/*
		Reads the board config from the specified file
	*/
	public static List<Character> getBoardConfig(String fName)
	{
		String line = null;
		
		try
		{
			FileReader fileR = new FileReader(fName);
			BufferedReader buffR = new BufferedReader(fileR);
			line = buffR.readLine();
			ArrayList<Character> out = new ArrayList<Character>();
			for (char a : line.toCharArray())
			{
				out.add(a);
			}
			buffR.close();
			return out;
		}
		catch(FileNotFoundException ex)
		{
			System.out.println( "Unable to open file '" + fName + "'");
		}
		catch(IOException ex)
		{
			System.out.println("Error reading file '" + fName + "'");
		}
		return null;
	}
	
	/*
		Writes the optimal move to the specified file 
	*/
	public static void writeOutput(outputObj out, String fName)
	{
		try {
			// Assume default encoding.
			FileWriter fileWriter = new FileWriter(fName);

			// Always wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// Note that write() does not automatically
			// append a newline character.
			bufferedWriter.write(out.toString());

			// Always close files.
			bufferedWriter.close();
		}
		catch(IOException ex) {
			System.out.println("Error writing to file '" + fName + "'");
		}
	}
}

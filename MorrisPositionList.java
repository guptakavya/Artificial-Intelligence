import java.util.*;

/* Class which is used to represent a board state/move option */
public class MorrisPositionList
{
	/* Fundamentally all these functions augment the main functionality of this List of posTypes*/
	public List<posType> posList;

	/* Default constructor, all X's*/
	public MorrisPositionList()
	{
		posList = Arrays.asList(posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X);
	}

	/* Allows user to specify what the board locations should be */
	public MorrisPositionList(List<Character> inputBoard)
	{
		posList = new ArrayList<posType>();
		for (char c : inputBoard)
		{
			posType pos = (c == 'W') ? posType.W : ((c == 'B') ? posType.B : posType.X);
			posList.add(pos);
		}
	}

	public List<Character> getCharList()
	{
		ArrayList<Character> out = new ArrayList<Character>();
		for (posType pos : posList)
		{
			out.add(pos.val);
		}
		return out;
	}

	public char[] getCharArr()
	{
		char[] out = new char[posList.size()];
		for (int i = 0; i < posList.size(); i++)
		{
			out[i] = posList.get(i).val;
		}
		return out;
	}

	public MorrisPositionList getCopy()
	{
		List<Character> boardState = getCharList();
		return (new MorrisPositionList(boardState));
	}

	/* Return the inverse of the current board(useful for generating black moves)*/
	public MorrisPositionList getFlipBoard()
	{
		MorrisPositionList out = new MorrisPositionList();
		for (int i = 0; i < posList.size(); i++)
		{
			posType val = posList.get(i);
			if (val == posType.B)
			{
				out.set(i, posType.W);
			}
			else if (val == posType. W)
			{
				out.set(i, posType.B);
			}
			else
			{
				out.set(i, posType.X);
			}
		}
		return out;
	}

	public int getNumPieces(posType piecePos)
	{
		int counter = 0;
		for (posType pos : posList)
		{
			if (pos == piecePos)
			{
				counter++;
			}
		}
		return counter;
	}

	/*
		Some List functionality wrappers for convenience
	*/
	public posType get(int i)
	{
		return posList.get(i);
	}

	public int size()
	{
		return posList.size();
	}

	public void add(posType val)
	{
		posList.add(val);
	}

	public void set(int i, posType val)
	{
		posList.set(i, val);
	}

	public String toString()
	{
		return (new String(getCharArr()));
	}
}
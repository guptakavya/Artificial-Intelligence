import java.util.*;

public class MiniMaxOpening
{
	public static void main(String[] args)
	{
		/* Reads in the parameters */
		String inputFileName = args[0];
		String outputFileName = args[1];
		int depth = Integer.parseInt(args[2]);

		/* Makes initial board object(reads from specified file) */
		MorrisPositionList initBoard = new MorrisPositionList(MorrisGame.getBoardConfig(inputFileName));

		/* Performs the search */
		outputObj algOut = MiniMax(depth, true, initBoard);

		/* Writes the output to the specified file */
		MorrisGame.writeOutput(algOut, outputFileName);
	}

	/*
		Minimax Procedure for finding optimal move
	*/
	public static outputObj MiniMax(int depth, boolean isWhite, MorrisPositionList board)
	{
		outputObj out = new outputObj();

		/* Means that we are at a terminal node */
		if (depth == 0)
		{
			out.val = MorrisGame.statEstOpening(board);
			out.numNodes++;
			return out;
		}

		outputObj in = new outputObj();
		List<MorrisPositionList> nextMoves = (isWhite) ? MorrisGame.generateMovesOpening(board) : MorrisGame.generateMovesOpeningBlack(board);
		out.val = (isWhite) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		for (MorrisPositionList b : nextMoves)
		{
			if (isWhite)
			{
				in = MiniMax(depth - 1, false, b);
				out.numNodes += in.numNodes;
				if (in.val > out.val)
				{
					out.val = in.val;
					out.b = b;
				}
			}
			else
			{
				in = MiniMax(depth - 1, true, b);
				out.numNodes += in.numNodes;
				out.numNodes++;
				if (in.val < out.val)
				{
					out.val = in.val;
					out.b = b;
				}
			}
		}
		return out;
	}
}
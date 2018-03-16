import java.util.*;

public class ABGame
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
		outputObj algOut = ABMiniMax(depth, true, initBoard, Integer.MIN_VALUE, Integer.MAX_VALUE);

		/* Writes the output to the specified file */
		MorrisGame.writeOutput(algOut, outputFileName);
	}
	
	/*
		Minimax Procedure for finding optimal move with Alpha-Beta Pruning
	*/
	public static outputObj ABMiniMax(int depth, boolean isWhite, MorrisPositionList board, int alpha, int beta)
	{
		outputObj out = new outputObj();

		/* Means that we are at a terminal node */
		if (depth == 0)
		{
			out.val = MorrisGame.statEstMidgameEndgame(board);
			out.numNodes++;
			return out;
		}

		List<MorrisPositionList> nextMoves;
		outputObj in = new outputObj();
		nextMoves = (isWhite) ? MorrisGame.generateMovesMidgameEndgame(board) : MorrisGame.generateMovesMidgameEndgameBlack(board);
		for (MorrisPositionList b : nextMoves)
		{
			if (isWhite)
			{
				in = ABMiniMax(depth - 1, false, b, alpha, beta);
				out.numNodes += in.numNodes;
				out.numNodes++;
				if (in.val > alpha)
				{
					alpha = in.val;
					out.b = b;
				}
			}
			else
			{
				in = ABMiniMax(depth - 1, true, b, alpha, beta);
				out.numNodes += in.numNodes;
				if (in.val < beta)
				{
					beta = in.val;
					out.b = b;
				}
			}
			if (alpha >= beta)
			{
				break;
			}
		}
		
		out.val = (isWhite) ? alpha : beta;
		return out;
	}
}
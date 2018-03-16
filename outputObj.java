/*
	Return object used for convenience
*/
public class outputObj
{
	public int val, numNodes;
	public MorrisPositionList b;
	public String toString()
	{
		return 	"BoardPosition:\t\t\t" + b + "\n" +
				"Positions Evaluated:\t" + numNodes + "\n" + 
				"MINIMAX estimate:\t\t" + val;
	}
}
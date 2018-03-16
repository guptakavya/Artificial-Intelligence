/* Simple enum for our possible node markers */
public enum posType
{
	X ('X'),
	B ('B'),
	W ('W');

	public final char val;
	posType(char val)
	{
		this.val = val;
	}
}
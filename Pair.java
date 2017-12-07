/**
 * This class is a subclass of the Hand class, and are used to model a hand of Pair. 
 * It overrides getTopCard,isValid and getType method that it inherits from Hand class.
 * @author sudhakarshah
 *
 */
public class Pair extends Hand {
	
	/**
	 * Constructor for Pair type hand. Calls the constructor of Hand class.
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of card played by the player
	 */
	public Pair(CardGamePlayer player, CardList cards){
		super(player,cards);
		
	}
	
	
	/* (non-Javadoc)
	 * Checks whether the hand is a Pair
	 * 
	 * @see Hand#isValid()
	 */
	public boolean isValid(){
		if (size()==2)
		{
			if (getCard(0).getRank()==getCard(1).getRank())
			{
				return true;
			}

		}
		return false;
		
	}
	
	/* (non-Javadoc)
	 * returns type of string
	 * @see Hand#getType()
	 */
	public String getType(){
		return "Pair";
	}
	

}

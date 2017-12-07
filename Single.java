/**
 * This class is a subclass of the Hand class, and are used to model a hand of Single. 
 * It overrides getTopCard,isValid and getType method that it inherits from Hand class.
 * @author sudhakarshah
 *
 */
public class Single extends Hand {
	
	
	/**
	 * Constructor for Single type hand. Calls the constructor of Hand class.
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of card played by the player
	 */
	public Single(CardGamePlayer player, CardList cards){
		super(player,cards);
		
	}

	
	/* (non-Javadoc)
	 * Checks whether the hand is a Single
	 * 
	 * @see Hand#isValid()
	 */
	public boolean isValid(){
		if (size()==1)
			return true;
		return false;
	}
	
	/* (non-Javadoc)
	 * returns type of string
	 * @see Hand#getType()
	 */
	public String getType(){
		return "Single";
	}
}

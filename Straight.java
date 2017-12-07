/**
 * This class is a subclass of the Hand class, and are used to model a hand of Straight. 
 * It overrides getTopCard,isValid and getType method that it inherits from Hand class.
 * @author sudhakarshah
 *
 */
public class Straight extends Hand {
	
	
	/**
	 * Constructor for Straight type hand. Calls the constructor of Hand class.
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of card played by the player
	 */
	public Straight(CardGamePlayer player, CardList cards){
		super(player,cards);
		
	}
	
	/* (non-Javadoc)
	 * Checks whether the hand is a Straight
	 * 
	 * @see Hand#isValid()
	 */
	public boolean isValid(){
		
		if (size()==5)
		{
			sort();
			//five consecutive rank 
			for (int i=1;i<5;i++)
			{
				int localRank=manageRank(getCard(i).getRank());
				//checking if the ranks are consecutive
				if (localRank-1!=manageRank(getCard(i-1).getRank()))
					return false; 	
			}
			return true;
		}
		return false;
		
	}
	
	/* (non-Javadoc)
	 * returns type of string
	 * @see Hand#getType()
	 */
	public String getType(){
		return "Straight";
	}

	

}

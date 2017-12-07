/**
 * This class is a subclass of the Hand class, and are used to model a hand of Quad. 
 * It overrides getTopCard,isValid and getType method that it inherits from Hand class.
 * @author sudhakarshah
 *
 */
public class Quad extends Hand{
	
	
	/**
	 * Constructor for Quad type hand. Calls the constructor of Hand class.
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of card played by the player
	 */
	public Quad(CardGamePlayer player, CardList cards){
		super(player,cards);
		
	}
	
	/* (non-Javadoc)
	 * Returns the top card of the hand
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard(){
		
		//The card in the quadruple with the highest 
		//suit in a quad is referred to as the top card of this quad.
		sort();
		//if quadruple has higher rank than the single
		if (getCard(0).getRank() != getCard(1).getRank())
			return getCard(4);
		// if quadruple has lower rank than single
		else
			return getCard(3);
	}
	
	/* (non-Javadoc)
	 * Checks whether the hand is a Quad
	 * 
	 * @see Hand#isValid()
	 */
	public boolean isValid(){
		if (size()==5)
		{
			int arr[]= new int[13]; // creating an array of ranks
			for (int i=0;i<5;i++)
			{
				arr[getCard(i).getRank()]++;
			}
			//This hand consists of five cards, with four having the same rank.
			
			for (int j=0;j<13;j++)
			{
				if (arr[j]==4)
					return true;
			}
			return false;
			
		}
		return false;
		
	}
	
	/* (non-Javadoc)
	 * returns type of string
	 * @see Hand#getType()
	 */
	public String getType(){
		return "Quad";
	}

}

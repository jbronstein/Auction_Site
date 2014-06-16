package camelinaction;

public class AddictedBidderStrat implements Strategy{

	@Override
	public int calcBid(int price) {
		int bidVal = 0;
		int currentAmount = price/2;
		bidVal = ((price + currentAmount)/2);
		if (bidVal > currentAmount){
			bidVal = currentAmount - 14;
		}
		
		return bidVal;
	}
}

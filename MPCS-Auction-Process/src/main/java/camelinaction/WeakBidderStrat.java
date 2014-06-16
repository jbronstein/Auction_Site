package camelinaction;

public class WeakBidderStrat implements Strategy{

	@Override
	public int calcBid(int price) {
		int bidVal = price + 5;		
		return bidVal;
	}

}

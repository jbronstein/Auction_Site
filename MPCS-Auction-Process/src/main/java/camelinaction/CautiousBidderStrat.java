package camelinaction;

public class CautiousBidderStrat implements Strategy{

	@Override
	public int calcBid(int price) {
		int currentVal = price/2;
		int bidVal = price + (currentVal/4);
		return bidVal;
	}
}

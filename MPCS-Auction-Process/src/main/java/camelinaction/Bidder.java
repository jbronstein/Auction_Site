package camelinaction;

//Bidder Class to keep track of Bidder Details, include Strategy here
public class Bidder {
	private int mAmount;
	private String mName;
	private int mRank;
	private Strategy mStrategy;
	
	public Bidder(String name, int amount, int rank, Strategy s){
		this.mName = name;
		this.mAmount = amount;
		this.mRank = rank;
		this.mStrategy = s;
	}

	//Get Strategy Bid result
	public int getBid(int price){	
		return mStrategy.calcBid(price);
	}
	
	public String getName(){
		return mName;
	}
	
	public int getAmount(){
		return mAmount;
	}
	
	//1,2,3
	public int getRank(){
		return mRank;
	}
	
	//Update the bidder's cash after winning an Auction
	public void updateAmount(int bidVal){
		mAmount = mAmount - bidVal;
	}
	
}

package camelinaction;

import java.util.ArrayList;

//BidderGroup Singleton for the Main Process Method
public class BidderGroup {

	private static BidderGroup instance = null;
	
	private ArrayList <Bidder> mBidGroup;
	
	protected BidderGroup(){}
	
	//Make Instance Thread Safe
	public static BidderGroup getInstance(){
		if (instance == null){
			synchronized(BidderGroup.class){
				if(instance == null){
					instance = new BidderGroup();
				}
			}
		}
		
		return instance;
	}
	
	public void getBidderList(ArrayList <Bidder> bidders){
		mBidGroup = bidders; 
	}
	
	
	
	
	
	
}

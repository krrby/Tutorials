import java.util.Date;
import java.util.ArrayList;


public class Block {
	
		public String hash;
		public String previousHash;
		public String merkleRoot;
		public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		
		private long timeStamp; //Unix timestamp
		private int nonce;
		
		//Block Constructor
		public Block(String previousHash) {
				
				this.previousHash = previousHash;
				this.timeStamp = new Date().getTime();
				
				//Making sure we do this after we set other values.
				this.hash = calulateHash();
				
		}
		
		//We must calculate the hash from all parts of the block
		public String calulateHash() {
			
				String calculatedhash = StringUtil.applySha256(
									previousHash +
									Long.toString(timeStamp) +
									Integer.toString(nonce) +
									merkleRoot
									);
				
				return calculatedhash;
					
		}
		
		public void mineBlock(int difficulty) {
			
				merkleRoot = StringUtil.getMerkleRoot(transactions);
				//Creates a difficulty * "0"
				String target = StringUtil.getDificultyString(difficulty); //Create a string with difficulty * "0" 
				while(!hash.substring( 0, difficulty).equals(target)) {
					
					nonce ++;
					hash = calulateHash();
				}
				System.out.println("Block Mined!!! : " + hash);
			}
		
		//Add transactions to this block
		public boolean addTransaction(Transaction transaction) {
			//process transaction and check if valid, unless block is genesis block then ignore.
			if(transaction == null) return false;		
			if((previousHash != "0")) {
				if((transaction.processTransaction() != true)) {
					System.out.println("Transaction failed to process. Discarded.");
					return false;
				}
			}
			transactions.add(transaction);
			System.out.println("Transaction Successfully added to Block");
			return true;
		}
		
		
		

}


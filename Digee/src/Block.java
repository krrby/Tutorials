import java.util.ArrayList;
import java.util.Date;

public class Block {

		/* The 'hash' will be the hash for this current block.
		 * the 'previous hash will be used to hash the next block.
		 * Our 'transaction arraylist is an error of all our transactions' will be a simple message.
		 * The 'timestamp' will be the UNIX one*/
		public String hash;
		public String previousHash;
		public String merkleRoot;
		public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); 
		private long timeStamp;
		private int nonce;
		
		/*Block Constructor - creating the one block of the chain
		 *The string hash will hold the digital sig.
		 *previous has will hold the last block hash
		 *Data will hold the data we want to store.
		 **/
		public Block(String previousHash) {
				

				this.previousHash = previousHash;
				this.timeStamp = new Date().getTime();
				
				this.hash = calculateHash();//Making sure we do this after set all the other values
				
		}
		
		/*We must calculate the has from all parts of the block
		 * we dont want it to be tampered with so we include the
		 * previousHah, the data and the timestamp
		 */
		public String calculateHash() {
				String calculateHash = StringUtil.applySha256(
								previousHash +
								Long.toString(timeStamp) +
								Integer.toString(nonce) +
								merkleRoot
								);
				return calculateHash;
		}
		
		/*This method takes an integer called difficulty, this is
		 * the number of 0's they must solve for. Low difficulty
		 * like 1 or 2 can be solve nearly instantly on most 
		 * most computers*/
		public void mineBlock(int difficulty) {
			merkleRoot = StringUtil.getMerkleRoot(transactions);
			String target = StringUtil.getDificultyString(difficulty); //Create a string with difficulty * "0" 
			while(!hash.substring( 0, difficulty).equals(target)) {
				nonce ++;
				hash = calculateHash();
			}
			System.out.println("Block Mined!!! : " + hash);
		}
		
		//Add transactions to this block
		public boolean addTransaction(Transaction transaction) {
			//process transaction and check if valid, unless block is genesis block then ignore.
			if(transaction == null) return false;		
			if((!"0".equals(previousHash))) {
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
	


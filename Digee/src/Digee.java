import java.util.ArrayList;
import com.google.gson.GsonBuilder;
import java.util.Base64;
import java.util.HashMap;
import java.security.Security;


public class Digee {
		
		//adding the blockchain to the arraylist
		public static ArrayList<Block> blockchain = new ArrayList<Block>();
		public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();
		
		public static float minimumTransaction;
		
		//adding the difficulty
		public static int difficulty = 5;
		
		//Wallets
		public static Wallet walletA;
		public static Wallet walletB;
		
		public static Transaction genesisTransaction;
		
		
		public static void main(String[] args) {
			
				//Setup Bouncy castle as a Security Provider
				Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
				
				//Creates the Wallet
				walletA = new Wallet();
				walletB = new Wallet();
				Wallet coinbase = new Wallet();
				
				//create genesis transaction, which sends 100 NoobCoin to walletA: 
				genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
				genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction	
				genesisTransaction.transactionId = "0"; //manually set the transaction id
				genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
				UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
				
				System.out.println("Creating and Mining Genesis block... ");
				Block genesis = new Block("0");
				genesis.addTransaction(genesisTransaction);
				addBlock(genesis);
				
				//testing
				Block block1 = new Block(genesis.hash);
				System.out.println("\nWalletA's balance is: " + walletA.getBalance());
				System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
				block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
				addBlock(block1);
				System.out.println("\nWalletA's balance is: " + walletA.getBalance());
				System.out.println("WalletB's balance is: " + walletB.getBalance());
				
				Block block2 = new Block(block1.hash);
				System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
				block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
				addBlock(block2);
				System.out.println("\nWalletA's balance is: " + walletA.getBalance());
				System.out.println("WalletB's balance is: " + walletB.getBalance());
				
				Block block3 = new Block(block2.hash);
				System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
				block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20));
				System.out.println("\nWalletA's balance is: " + walletA.getBalance());
				System.out.println("WalletB's balance is: " + walletB.getBalance());
				
				isChainValid();
				
				/*Test Public and private keys
				System.out.println("Private and public keys: ");
				System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
				System.out.println(StringUtil.getStringFromKey(walletA.publicKey));
				
				//Create a test transaction from WalletA to WalletB				
				Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
				transaction.generateSignature(walletA.privateKey);
				
				//Verify the signature works and verify it from the public key
				System.out.println("Is Signature verified");
				System.out.println(transaction.verifySignature());
				
				/*Testing the block chain
				 * 
				blockchain.add(new Block("Hi, Im the first block!", "0"));
				System.out.println("Trying to mine block 1..");
				blockchain.get(0).mineBlock(difficulty);
				
				blockchain.add(new Block("Yo im the second block",blockchain.get(blockchain.size()-1).hash));
				System.out.println("Trying to mine block 2..");
				blockchain.get(1).mineBlock(difficulty);
				
				blockchain.add(new Block("Yo im the third block",blockchain.get(blockchain.size()-1).hash));
				System.out.println("Trying to mine block 3..");
				blockchain.get(2).mineBlock(difficulty);
				
				System.out.println("Blockchain is Valid: " + isChainValid());
				
				String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);					
				System.out.println("\nThe Block chain: ");
				System.out.println(blockchainJson);*/
			
		}
		
		//checking if the blockchain is valid.
		public static Boolean isChainValid() {
			
			Block currentBlock;
			Block previousBlock;
			
			//Loop through blockchain to check hashes:
			for(int i = 1; i < blockchain.size(); i++) {
				
				currentBlock = blockchain.get(i);
				previousBlock = blockchain.get(i-1);
				
				if(!currentBlock.hash.contentEquals(currentBlock.calulateHash()) ) {
					
						System.out.println("Current hashes are not equal");
						return false;
						
				}
				
				//Compare previous hash and registered previous hash
				if(!previousBlock.hash.contentEquals(currentBlock.previousHash) ) {
					
						System.out.println("Previous hashes are not equal");
						return false;
				}
			}
		
			return true;
	}
		
		public static void addBlock(Block newBlock) {
			newBlock.mineBlock(difficulty);
			blockchain.add(newBlock);
		}
}
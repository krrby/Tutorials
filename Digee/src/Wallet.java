import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;


public class Wallet {

		public PrivateKey privateKey;
		public PublicKey publicKey;
		
		public Wallet() {
			
				generateKeyPair();
		}
		
		public void generateKeyPair() {
			
				try {
							KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
							SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
							ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
							
							//Initialize the key generator and generate a keypair
							keyGen.initialize(ecSpec, random);
							KeyPair keyPair = keyGen.generateKeyPair();
							
							//Set the public and private keys from the KeyPair.
							privateKey = keyPair.getPrivate();
							publicKey = keyPair.getPublic();
				
				}catch(Exception e) {
					
						throw new RuntimeException(e);
				}
		}
		
		//returns balance and stores the UTXO's owned by this wallet in this.UTOXos
		public float getBalance() {
			
			float total = 0;	
	        for (Map.Entry<String, TransactionOutput> item: Digee.UTXOs.entrySet()){
	        	
	        	TransactionOutput UTXO = item.getValue();
	            if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
	            	
	            	Digee.UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
	            	total += UTXO.value ; 
	            }
	        }  
			return total;
		}
		
		//Generates and returns a new transaction from this wallet.
		public Transaction sendFunds(PublicKey _recipient,float value ) {
			
			if(getBalance() < value) { //gather balance and check funds.
				
				System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
				return null;
			}

			ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		    
			float total = 0;
			for (Map.Entry<String, TransactionOutput> item: Digee.UTXOs.entrySet()){
				TransactionOutput UTXO = item.getValue();
				total += UTXO.value;
				inputs.add(new TransactionInput(UTXO.id));
				if(total > value) break;
			}
			
			Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
			newTransaction.generateSignature(privateKey);
			
			for(TransactionInput input: inputs){
					Digee.UTXOs.remove(input.transactionOutputId);
			}
			return newTransaction;
		}
		
		
}

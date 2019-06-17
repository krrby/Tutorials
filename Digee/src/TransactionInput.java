
public class TransactionInput {
		//Reference to TransactionOutputs -> transactionOutputs -> transactionId
		public String transactionOutputId;
		//Contains the unspent transaction input
		public TransactionOutput UTXO;
		
		public TransactionInput(String transactionOutputId) {
				this.transactionOutputId = transactionOutputId;
			
		}

}

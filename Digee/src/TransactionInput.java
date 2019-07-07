/*Following bitcoins convention and calls unspent
 * transaction outputs: UTXO's*/
public class TransactionInput {

			/*Reference to TransactionOutputs -> transactionId*/
			public String transactionOutputId;
			/*Contains the unspent transaction output*/
			public TransactionOutput UTXO;
			
			public TransactionInput(String transactionOutputId) {
					
					this.transactionOutputId = transactionOutputId;
			}
}

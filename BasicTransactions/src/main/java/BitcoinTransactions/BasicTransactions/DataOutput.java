package BitcoinTransactions.BasicTransactions;

import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.script.ScriptOpCodes;

/**
 * @author Giorgos Topalidis
 */

// This class simulates a DataOutput Transaction, which
// is an unspendable Transaction. The characteristic op_code of this
// transaction is the OP_RETURN.

public class DataOutput {

	private String name;
	private String scriptDescription;

	private org.bitcoinj.core.Transaction transaction;
	ScriptBuilder scriptbuilder;

	public DataOutput() {

		name = "Data Output (OP_RETURN)";
		scriptDescription = "The null data scripts"
				+ " begin with the OP_RETURN opcode."
				+ " They allow the creator of the transaction"
				+ " to include some arbitrary data"
				+ " in the block chain in exchange"
				+ " for paying a transaction fee."
				+ " The output is unspendable.";

		transaction = new org.bitcoinj.core.Transaction(MainNetParams.get());
		scriptbuilder = new ScriptBuilder();

	}

	public void createOpReturnTransaction(org.bitcoinj.core.Transaction tx,
			ScriptBuilder sb) {
		System.out
				.println("**********************************************************************************************************");
		System.out
				.println("This function defines a message (myData) which is going to be wraped in my script\n"
						+ "but first we are going to calculate the SHA256 digest of this message,\n"
						+ "which is going to be 32 bytes. We have to calculate the digest of the message,\n"
						+ "because otherwise the size of our message it's going to exceed the 80 bytes limit of OP_RETURN opcode.");
		System.out
				.println("**********************************************************************************************************");

		String myData = "This program is a part of my thesis!!!";

		byte[] sha256 = org.apache.commons.codec.digest.DigestUtils
				.sha256(myData);
		Script myScript = sb.op(ScriptOpCodes.OP_RETURN).data(sha256).build();
		TransactionOutput transactionOutput = tx.addOutput(
				org.bitcoinj.core.Transaction.MIN_NONDUST_OUTPUT, myScript);

		System.out.println("My script is :" + myScript);
		System.out.println("My transaction output is : " + transactionOutput);
		System.out
				.println("The size of my message after hashing with SHA256 is : "
						+ sha256.length + " bytes.");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScriptDescription() {
		return scriptDescription;
	}

	public void setScriptDescription(String scriptDescription) {
		this.scriptDescription = scriptDescription;
	}

	public org.bitcoinj.core.Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(org.bitcoinj.core.Transaction transaction) {
		this.transaction = transaction;
	}

	public ScriptBuilder getScriptbuilder() {
		return scriptbuilder;
	}

	public void setScriptbuilder(ScriptBuilder scriptbuilder) {
		this.scriptbuilder = scriptbuilder;
	}

}

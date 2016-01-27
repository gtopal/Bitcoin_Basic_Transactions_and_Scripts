package BitcoinTransactions.BasicTransactions;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;

import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.ECKey.ECDSASignature;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bouncycastle.util.encoders.Hex;
import org.spongycastle.crypto.digests.RIPEMD160Digest;

/**
 * @author Giorgos Topalidis
 */

// This class simulates the creation and the validation of a PayToScriptHash
// Transaction. This kind of transactions is more complicated than the
// PayToPubKeyHash Transaction.
// This kind of Transaction gives the ability to the Receiver of Bitcoins to
// demand more complicated script structures in order to maintain
// security.

public class PayToScriptHash extends Transaction {

	private String name;
	private String scriptDescription;
	private final Double fee = 0.00000005; // 0.00000005 BT
	private Sender sender;
	private Receiver receiver;
	private Miner miner;

	private Company company;
	ScriptBuilder scriptbuilder;

	private Map<ECKey, Sha256Hash> map = new HashMap<ECKey, Sha256Hash>();
	private Map<ECKey, Sha256Hash> map2 = new HashMap<ECKey, Sha256Hash>();
	ArrayList<Sha256Hash> signedMessageList = new ArrayList<Sha256Hash>();



	public PayToScriptHash(Sender sender, Receiver receiver, Miner miner)
			throws AddressFormatException {
		super(sender, receiver, miner);

		name = "Pay to ScriptHash";
		scriptDescription = "P2SH transactions were created with the motivation of moving"
				+ " the responsibility for supplying the conditions to redeem a transaction"
				+ " from the sender of the funds to the redeemer."
				+ " They allow the sender to fund an arbitrary transaction,"
				+ " no matter how complicated, using a 20-byte hash.";
		company = new Company();
		scriptbuilder = new ScriptBuilder();

	}

	// This function returns the reddem script

	public Script getMultiSigOutputScript(Company myCompany) {
		ArrayList<Receiver> receiverList = myCompany.getrList();
		ArrayList<ECKey> pubkeys = new ArrayList<ECKey>();

		for (Receiver currentReceiver : receiverList) {
			ECKey currentKey = currentReceiver.getReceiverWalletList().get(0)
					.getClientKey();
			pubkeys.add(currentKey);

		}
		Script redeemScript = ScriptBuilder.createRedeemScript(2, pubkeys);
		return redeemScript;
	}

	// This function returns the <20-byte hash of redeem script> of the redeem
	// script

	public String getTheHashOfTheScript(Script myScript) throws IOException {
		myScript = getMultiSigOutputScript(company);
		byte[] bytesFromScript = myScript.getProgram();

		byte[] sha256 = org.apache.commons.codec.digest.DigestUtils
				.sha256(bytesFromScript);

		byte[] myResult = RIPEMD160(sha256);
		String result = new String(myResult);
		return result;

	}

	// This function estimates the RIPEMD160 hash

	public byte[] RIPEMD160(byte[] myBytes) throws IOException {

		RIPEMD160Digest d = new RIPEMD160Digest();
		d.update(myBytes, 0, myBytes.length);
		byte[] o = new byte[d.getDigestSize()];
		d.doFinal(o, 0);
		return Hex.encode(o);

	}

	// This function creates the ScriptPubKey from the redeem script.

	public Script createScriptPubKeyFromRedeemScript(Script redeemScript) {
		Script scriptPubKey = ScriptBuilder
				.createP2SHOutputScript(redeemScript);
		return scriptPubKey;
	}

	// This function creates the ScriptSig script

	public Script createScriptSig(Script scriptPubKey, Script redeemScript) {

		// scriptPubKey = createScriptPubKeyFromRedeemScript(redeemScript);

		ArrayList<TransactionSignature> signatures = createTransactionSignatures(company);

		Script scriptSig = ScriptBuilder.createP2SHMultiSigInputScript(
				signatures, redeemScript); // maybe it needs redeem as input
											// script
		return scriptSig;

	}

	// This function creates the TransactionSignatures from the
	// ECKey.ECDSASignatures

	public ArrayList<TransactionSignature> createTransactionSignatures(
			Company company) {

		ArrayList<Receiver> receiverList = company.getrList();
		ArrayList<ECKey> keyList = new ArrayList<ECKey>();

		for (Receiver currentReceiver : receiverList) {
			ECKey currentKey = currentReceiver.getReceiverWalletList().get(0)
					.getClientKey();
			keyList.add(currentKey);

		}

		ArrayList<TransactionSignature> transactionSignatureList = new ArrayList<TransactionSignature>();

		for (ECKey currenKey : keyList) {
			Sha256Hash input = map.get(currenKey);

			ECDSASignature signature = currenKey.sign(input);
			BigInteger R = signature.r;
			BigInteger S = signature.s;
			TransactionSignature currentTransactionSignature = new TransactionSignature(
					R, S);
			transactionSignatureList.add(currentTransactionSignature);

		}

		return transactionSignatureList;
	}

	public void transactionProcedure() throws IOException,
			NoSuchAlgorithmException {

		if (scriptPubKey(receiver, sender, miner)) {
			if (scriptSig(company)) {
				if (bitcoinTransaction(company, sender, miner)) {
					System.out
							.println("********************************************************");
					System.out
							.println("FINALLY THE P2SH TRANSACTION FINISHED SUCCESSFULLY");
					System.out
							.println("********************************************************\n");

				} else {
					System.out
							.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					System.out
							.println("FINALLY THE P2SH TRANSACTION FINISHED UNSUCCESSFULLY");
					System.out
							.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
				}
			}

		}

	}

	public boolean scriptPubKey(Receiver receiver, Sender sender, Miner miner)
			throws IOException {
		System.out
				.println("----------------------------------------------------------------------------------------------------------------------------------------------");
		System.out
				.println("In this function we compare the result of OP_HASH160 opcode with the "
						+ " <20 byte hash of redeem script> data in order to see if they are equal");
		System.out
				.println("----------------------------------------------------------------------------------------------------------------------------------------------\n");

		Company c;
		c = getCompany();
		Script companyRedeemScript = getMultiSigOutputScript(c); //redeem script
		String hashOfRedeemScriptEstimatingByCompany = getTheHashOfTheScript(companyRedeemScript); //hash of the redeem script

		sender = super.getSender();
		String hashOfRedeemScriptOfSender = sender
				.setHashOfRedeemScript(hashOfRedeemScriptEstimatingByCompany);

		if (hashOfRedeemScriptEstimatingByCompany
				.equals(hashOfRedeemScriptOfSender)) {
			System.out
					.println("************************************************************");
			System.out
					.println("The OP_EQUAL opcode of ScriptPubKey executed SUCCESSFULLY");
			System.out
					.println("************************************************************");
			return true;

		} else {
			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out
					.println("The OP_EQUAL opcode of ScriptPubKey executed UNSUCCESSFULLY");
			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			return false;
		}

	}

	public boolean scriptSig(Company company) throws NoSuchAlgorithmException,
			IOException {

		System.out.println("--------------------------------------------");
		System.out.println("This function is going to implement\n"
				+ " the signature validation over the\n "
				+ "public keys of the receivers.\n"
				+ "In our example we have to validate\n"
				+ " that 2 out of 4 public keys\n"
				+ " match with the 2 out of 4 signatures\n"
				+ "in order the company to unlock\n" + " the transaction and\n"
				+ " to redeem finally the BT");
		System.out.println("--------------------------------------------");

		ArrayList<ECDSASignature> signatureList = new ArrayList<ECDSASignature>();
		ArrayList<ECKey> keyList = new ArrayList<ECKey>();
		int matches = 0;

		for (Receiver currentReceiver : company.getrList()) {
			Wallet currentWallet = currentReceiver.getReceiverWalletList().get(
					0);
			ECKey currentKey = currentWallet.getClientKey();
			keyList.add(currentKey);

			Sha256Hash sha256OfCurrentReceiver = currentReceiver.getMyMessage()
					.getSha256OfMessage();
			signedMessageList.add(sha256OfCurrentReceiver); //here i add my signed messages in the list in order to compare afterwards these messages with the new ECKeys 


			// mapping the currentKey with the sha256OfCurrentReceiver
			map.put(currentKey, sha256OfCurrentReceiver);

			ECDSASignature receiverSig = currentWallet.getClientKey().sign(
					sha256OfCurrentReceiver);
			signatureList.add(receiverSig);

		}

		System.out.println("**********************************************");
		System.out.println("These 2 for loops compare\n "
				+ "the first signature against each public key\n"
				+ " until it finds an ECDSA match.\n "
				+ "Starting with the subsequent public key,\n"
				+ " it compares the second signature\n "
				+ "against each remaining public key\n"
				+ " until it finds an ECDSA match.\n"
				+ " The process is repeated until\n "
				+ "all signatures have been checked\n"
				+ " or not enough public keys remain\n"
				+ " to produce a successful result.");
		System.out.println("**********************************************\n");

		System.out.println("The size of the signature list is : "
				+ signatureList.size());
		System.out.println("The size of the EC keys is : " + keyList.size()
				+ "\n");

		// Iterator itr = keyList.iterator();
		ListIterator<ECKey> itr = keyList.listIterator();

		int counter = 1;
		int j = 0;

		// these list are the same
		// System.out.println(keyList);
		// System.out.println(getECKeys(company));

		for (ECDSASignature currentSignature : signatureList) {
			System.out.println("Now i am doing the : " + counter
					+ " check of signatures in my signature list.\n");

			// here i have to declare again my list, because after iteration the
			// list ends up empty!!!
			// keyList=getECKeys(company);

			if (counter > 1) {

				// GOOD SCENARIO
				ArrayList<ECKey> tempList = getECKeys(company);// refill the new
																// empty list
																// with the past
																// keys

				// BAD SCENARIO
				// ArrayList<ECKey> tempList =
			//	 getAllChangedECKeys(company);//refill the new empty list with
				// the new modified keys

				for (int k = 0; k < tempList.size(); k++) {
					keyList.add(tempList.get(k));
				}
				itr = keyList.listIterator(); // declare a "new" iterator for
												// the "new" list of keys
				
				
//				for (int k = 0; k < tempList.size(); k++) {
//				map2.put(tempList.get(k), signedMessageList.get(k)); //map2 matches the previous messages from the first map to the new changed ECKeys
//				
//			}

			}

			for (int i = keyList.size(); i > -1; i--) {

				ECKey currentKey = keyList.get(j);
				Sha256Hash input = null;

				 input = map.get(currentKey); // this is the reason
														// why i created the
														// map..in order in the
														// for of keyList to
														// have direct access to
														// the signed message
														// of the current
														// receiver
														// corresponding to this
														// ECKey.
				 
				 
				 
				 
//					if(counter==1){
//				 input = map.get(currentKey);
//				}  else
//				{
//					 input = map2.get(currentKey);
//                                                          
//				}

				if (ECKey.verify(input.getBytes(), currentSignature,
						currentKey.getPubKey())) {
					System.out
							.println("************************************************************************************************************");
					System.out.println("The match was SUCCESSFUL!!!");
					matches = matches + 1;
					System.out
							.println("You can remove the current key from the list"
									+ " in order not to be matched again in the next iteration.");
					System.out.println("The matches now are : " + matches);
					System.out
							.println("************************************************************************************************************\n");

					if (itr.hasNext()) {
						itr.next();
						itr.remove(); // Use itr.remove() method
						System.out
								.println("--------------------------------------");
						System.out
								.println("The size of the ECKey list is NOW : "
										+ keyList.size());
						// System.out.println(keyList);
						System.out
								.println("--------------------------------------\n");

						// break;

					}

				} else {
					System.out
							.println("************************************************************************************************************");
					System.out.println("The match was UNSUCCESSFUL!!!");
					System.out
							.println("You can remove the current key from the list"
									+ " in order not to be matched again in the next iteration.");
					System.out
							.println("************************************************************************************************************\n");

					if (itr.hasNext()) {
						itr.next();
						itr.remove(); // Use itr.remove() method
						System.out
								.println("--------------------------------------");

						System.out
								.println("The size of the ECKey list is NOW : "
										+ keyList.size());
						// System.out.println(keyList);
						System.out
								.println("--------------------------------------\n");

						// break;

					}

				}

				// in order the i index to follow the current size of the list
				// (4-->3-->2-->1-->0)

				i = keyList.size();
				// always check the first key of the current list,keyList.get(0)

				// System.out.println(j+"\n");

			}

			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out
					.println("it is obvious that the size of my key list after extracting all the keys will be : "
							+ keyList);
			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");

			counter++;

		}

		if (matches >= 2) {
			System.out
					.println("*******************************************************FINALLY*********************************************************************");
			System.out.println("At least 2 out of 4 members(receivers) "
					+ "of this company have in their possession"
					+ " valid signatures according to their public keys!!!");
			System.out
					.println("***********************************************************************************************************************************\n");

			return true;
		} else {
			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!UNFORTUNATELY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out
					.println("The number of keys which validate the signatures for this transaction is not suficient");
			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
			return false;
		}
	}

	public boolean bitcoinTransaction(Company company, Sender sender,
			Miner miner) {

		System.out
				.println("////////////////////////////////////////////////////////////////////////////////");
		System.out
				.println("/This procedure will estimate the balance of the Sender and Company(containing of 4 receivers) Wallet/\n"
						+ " /as long as the fee which is going to be given to the miner/\n "
						+ "/due to his effort mining the Block "
						+ "which contains this specific transaction!!!/");
		System.out
				.println("////////////////////////////////////////////////////////////////////////////////\n");

		sender = super.getSender();
		miner = super.getMiner();
		this.company = company;

		// Initialize the wallet of the Sender with an amount of Bitcoins
		System.out
				.println("********Initialize the wallet of the Sender with an amount of Bitcoins********\n");
		Scanner console = new Scanner(System.in);
		String senderAmountOfBT = console.nextLine();
		Double sAmount = Double.parseDouble(senderAmountOfBT);
		sender.getSenderWalletList().get(0).setBalance(sAmount); // i initialize
																	// it from
																	// the
																	// start(IT
																	// IS NOT
																	// 1BT AS I
																	// DID WITH
																	// THE
																	// MEMBERS
																	// OF THE
																	// COMPANY)
		System.out
				.println("Your answer for initializing the Sender's wallet was : "
						+ sAmount + " BT\n");

		System.out
				.println("********Decide an ammount of Bitcoins to send to the company********\n");
		Scanner console2 = new Scanner(System.in);
		String companyAmountOfBT = console2.nextLine();
		Double cAmount = Double.parseDouble(companyAmountOfBT);
		double total = cAmount
				+ company.estimateTotalBalance(company.getrList());
		company.setBalance(total);

		System.out
				.println("Your answer for sending an amount of BT to company was : "
						+ cAmount + " BT\n");

		if (sAmount - cAmount >= fee) {
			System.out
					.println("***********************************************");
			System.out
					.println("********The transfer of BT is valid!!!********");
			System.out
					.println("***********************************************\n");

			// MINER
			System.out
					.println("--------The fee of the transaction is fixed for every transaction to  : "
							+ fee + " BT--------\n");
			miner.setBalance(fee);
			System.out
					.println("----------------------------------------------------");
			System.out.println("-----------The miner now has : "
					+ miner.getBalance() + " BT-----------");
			System.out
					.println("----------------------------------------------------\n");

			// SENDER
			Double senderOutcome = sAmount - (cAmount + fee);
			sender.getSenderWalletList().get(0).setBalance(senderOutcome);
			System.out
					.println("-----------------------------------------------------");
			System.out.println("--------The sender now has : "
					+ sender.getSenderWalletList().get(0).getBalance()
					+ " BT--------");
			System.out
					.println("-----------------------------------------------------\n");

			// COMPANY
			// Double receiverOutcome = sAmount - cAmount;
			// r.getReceiverWalletList().get(0).setBalance(receiverOutcome);
			System.out
					.println("-----------------------------------------------------");
			System.out.println("------------The company now has : "
					+ company.getBalance() + " BT------------");
			System.out
					.println("-----------------------------------------------------\n");

			return true;

		} else {
			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out
					.println("!!!!!!!!The transfer of BT is not valid!!!!!!!!");
			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");

			return false;
		}

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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public ScriptBuilder getScriptbuilder() {
		return scriptbuilder;
	}

	public void setScriptbuilder(ScriptBuilder scriptbuilder) {
		this.scriptbuilder = scriptbuilder;
	}

	public Double getFee() {
		return fee;
	}

	public ArrayList<ECKey> getECKeys(Company company) {

		// i have now all the keys from the receivers(members) of the company!
		ArrayList<ECKey> myList = company.getAllKeys(company.getrList());
		return myList;

	}

	public ArrayList<ECKey> getAllChangedECKeys(Company company) {

		// i have now all the new keys from the receivers(members) of the
		// company in order to check the validity of the signatures!
		ArrayList<ECKey> myList = company.changeAllKeys(company.getrList());
		return myList;

	}

}

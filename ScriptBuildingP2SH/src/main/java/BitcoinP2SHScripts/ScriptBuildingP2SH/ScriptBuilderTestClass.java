package BitcoinP2SHScripts.ScriptBuildingP2SH;

import java.awt.List;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.ECKey.ECDSASignature;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bouncycastle.util.encoders.Hex;
import org.spongycastle.crypto.digests.RIPEMD160Digest;

/**
 * @author Giorgos Topalidis
 */

// This class simulates the building of a P2SH script.

public class ScriptBuilderTestClass {

	ScriptBuilder scriptbuilder;
	ECKey key1;
	ECKey key2;
	ECKey key3;
	ArrayList<ECKey> myList;
	Message message=new Message(); // this will be the message which is going to be signed (it takes the place of a signed signature)

	public ScriptBuilderTestClass() throws AddressFormatException {
		scriptbuilder = new ScriptBuilder();
		key1 = new ECKey();
		key2 = new ECKey();
		key3 = new ECKey();
		myList = new ArrayList<ECKey>();
		myList.add(key1);
		myList.add(key2);
		myList.add(key3);

	}

	public Script getMultiSigOutputScript(ArrayList<ECKey> list) {
		int i;
		ArrayList<ECKey> currentList = new ArrayList<ECKey>();
		for (i = 0; i < list.size(); i++) {
			currentList.add(list.get(i));
		}

		Script redeemScript = ScriptBuilder.createRedeemScript(2, currentList);
		return redeemScript;
	}

	public String getTheHashOfTheScript(Script myScript) throws IOException {
		myScript = getMultiSigOutputScript(myList);
		byte[] bytesFromScript = myScript.getProgram();

		byte[] sha256 = org.apache.commons.codec.digest.DigestUtils
				.sha256(bytesFromScript);

		byte[] myResult = RIPEMD160(sha256);
		String result = new String(myResult);
		return result;

	}

	public byte[] RIPEMD160(byte[] myBytes) throws IOException {

		RIPEMD160Digest d = new RIPEMD160Digest();
		d.update(myBytes, 0, myBytes.length);
		byte[] o = new byte[d.getDigestSize()];
		d.doFinal(o, 0);
		return Hex.encode(o);

	}

	public ScriptBuilder getScriptbuilder() {
		return scriptbuilder;
	}

	public void setScriptbuilder(ScriptBuilder scriptbuilder) {
		this.scriptbuilder = scriptbuilder;
	}

	public ArrayList<ECKey> getMyList() {
		return myList;
	}

	public void setMyList(ArrayList<ECKey> myList) {
		this.myList = myList;
	}

	public Script createScriptPubKeyFromRedeemScript(Script redeemScript) {
		Script scriptPubKey = ScriptBuilder
				.createP2SHOutputScript(redeemScript);
		return scriptPubKey;
	}

	public ArrayList<TransactionSignature> createTransactionSignatures(
			ArrayList<ECKey> list) {

		int i;
		ArrayList<ECKey> currentList = new ArrayList<ECKey>();
		for (i = 0; i < list.size(); i++) {
			currentList.add(list.get(i));
		}

		ArrayList<TransactionSignature> transactionSignatureList = new ArrayList<TransactionSignature>();

		for (ECKey currentKey : currentList) {
			
			
		//	Sha256Hash input = Sha256Hash.wrap(currenKey.getPrivKeyBytes()); this is wrong..it is not valid to sign the bytes of the private key
			Sha256Hash input =	message.getSha256OfMessage();
			
			ECDSASignature signature = currentKey.sign(input);
			BigInteger R = signature.r;
			BigInteger S = signature.s;
			TransactionSignature currentTransactionSignature = new TransactionSignature(
					R, S);
			transactionSignatureList.add(currentTransactionSignature);

		}

		return transactionSignatureList;
	}

	public Script createScriptSig(Script scriptPubKey, Script redeemScript) {

		// scriptPubKey = createScriptPubKeyFromRedeemScript(redeemScript);

		ArrayList<TransactionSignature> signatures = createTransactionSignatures(myList);

		Script scriptSig = ScriptBuilder.createP2SHMultiSigInputScript(
				signatures, redeemScript); // it needs redeem as input script
											 
		return scriptSig;

	}

	public static void main(String[] args) throws AddressFormatException,
			IOException {

		ScriptBuilderTestClass myScriptBuilder = new ScriptBuilderTestClass();
		Script myRedeemScript = myScriptBuilder
				.getMultiSigOutputScript(myScriptBuilder.getMyList());
		System.out.println("This is the structure of my redeem script : "
				+ myRedeemScript+ "\n");
		System.out.println("This is the <20-byte hash of redeem script> : "
				+ myScriptBuilder.getTheHashOfTheScript(myRedeemScript)+ "\n");		
		Script myScriptPubKey = myScriptBuilder
				.createScriptPubKeyFromRedeemScript(myRedeemScript);
		System.out.println("This is the structure of scriptPubKey : "
				+ myScriptPubKey+ "\n");
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("In order the procedure to end up successfully,\n"
				+ " at least 2 out 0f 3 signers must sign the same message\n"
				+ "the message simulates the transaction\n"
				+ "but the purpose of this program is to just show off the structure of a P2SH\n"
				+ " script and not the validation of it.");
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");

		Script scriptSig = myScriptBuilder.createScriptSig(myScriptPubKey,
				myRedeemScript);
		System.out.println("This is the structure of ScriptSig : " + scriptSig);

	}

}

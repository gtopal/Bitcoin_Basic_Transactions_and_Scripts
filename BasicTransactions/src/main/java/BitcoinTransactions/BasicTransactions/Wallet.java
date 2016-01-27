package BitcoinTransactions.BasicTransactions;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.params.MainNetParams;
import org.bouncycastle.util.encoders.Hex;
import org.spongycastle.crypto.digests.RIPEMD160Digest;

/**
 * @author Giorgos Topalidis
 */

// This class simulates a wallet in order the transactions between two parts to
// be realistic.

public class Wallet {

	private ECKey clientKey;
	private Double balance;
	Base58 b58 = new Base58();
	String bitcoinAddressWithoutChecksum; // without checksum
	String bitcoinAddress;
	byte[] hash160;

	public Wallet() throws AddressFormatException {

		clientKey = new ECKey();
		balance = 0.0; // 0 BT
		bitcoinAddressWithoutChecksum = Base58
				.encode(clientKey.getPubKeyHash()); // the Base58 encoded
													// address but without
													// checksum 
		bitcoinAddress = clientKey.toAddress(MainNetParams.get()).toString(); // the
																				// common
																				// Bitcoin
																				// Address
																				// starting
																				// with
																				// 1
																				// at
																				// PayToPubKeyHash
																				// transaction

		// the getHash160() function returns the RIPEMD160SHA256(K) value
		hash160 = new Address(MainNetParams.get(), getBitcoinAddress())
				.getHash160();

	}

	// // This function takes the Pubkey as Hex number and returns initially the
	// // Sha256 string value of this Hex.
	// // After that, takes this Sha256 String and creates the RIMEMD160 hash
	// which
	// // is the public Key Hash(20 bytes).
	//
	public String testFunction(String myHexnumber) throws IOException {
		// System.out
		// .println("************************************************************");
		String sha256hex2 = org.apache.commons.codec.digest.DigestUtils
				.sha256Hex(myHexnumber);
		System.out.println("My public key after SHA-256 hash :" + sha256hex2);
		byte[] temp3 = RIPEMD160(sha256hex2);
		String op_hashPubKey2 = new String(temp3);
		System.out
				.println("My public key first with SHA-256 and then with RIPEMD-160 hashed : "
						+ op_hashPubKey2);
		// System.out
		// .println("************************************************************");
		return op_hashPubKey2;

	}

	public byte[] RIPEMD160(String myString) throws IOException {

		byte[] r = myString.getBytes("US-ASCII");
		RIPEMD160Digest d = new RIPEMD160Digest();
		d.update(r, 0, r.length);
		byte[] o = new byte[d.getDigestSize()];
		d.doFinal(o, 0);
		return Hex.encode(o);

	}

	public ECKey getClientKey() {
		return clientKey;
	}

	public void setClientKey(ECKey clientKey) {
		this.clientKey = clientKey;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getBitcoinAddressWithoutChecksum() {
		return bitcoinAddressWithoutChecksum;
	}

	public void setBitcoinAddressWithoutChecksum(
			String bitcoinAddressWithoutChecksum) {
		this.bitcoinAddressWithoutChecksum = bitcoinAddressWithoutChecksum;
	}

	public String getBitcoinAddress() {
		return bitcoinAddress;
	}

	public void setBitcoinAddress(String bitcoinAddress) {
		this.bitcoinAddress = bitcoinAddress;
	}

	public byte[] getHash160() {
		return hash160;
	}

	public void setHash160(byte[] hash160) {
		this.hash160 = hash160;
	}

	@Override
	public String toString() {
		return "Wallet [clientKey=" + clientKey + ", balance=" + balance
				+ ", bitcoinAddressWithoutChecksum="
				+ bitcoinAddressWithoutChecksum + ", bitcoinAddress="
				+ bitcoinAddress + ", hash160=" + Arrays.toString(hash160)
				+ "]";
	}

}

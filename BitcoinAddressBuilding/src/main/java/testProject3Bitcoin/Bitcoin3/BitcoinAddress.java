package testProject3Bitcoin.Bitcoin3;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Wallet;
import com.google.bitcoin.params.MainNetParams;



/**
 * @author Giorgos Topalidis
 */

// This class simulates the creation of Bitcoin Address as its appears on a
// Bitcoin wallet.

public class BitcoinAddress {

	public static void main(String[] args) {
		
	
		ECKey myKey = new ECKey();
		String addr = myKey.toAddress(MainNetParams.get()).toString();
		System.out.println(addr);
		System.out.println(myKey);
		System.out.println("The length of my public key is : " +myKey.getPubKey().length+ " bytes");  //33 bytes length

	}

}

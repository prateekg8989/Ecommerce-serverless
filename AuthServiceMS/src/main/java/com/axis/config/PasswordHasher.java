package com.axis.config;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.stereotype.Component;

@Component
public class PasswordHasher {
	private byte[] getSalt() throws NoSuchAlgorithmException
	{
	    //Always use a SecureRandom generator
	    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
	    //Create array for salt
	    byte[] salt = new byte[16];
	    //Get a random salt
	    sr.nextBytes(salt);
	    //return salt
	    return salt;
	}
	
	
	public String[] makeHashAndSalt(String passwordToHash) throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] salt = getSalt();
		KeySpec spec = new PBEKeySpec(passwordToHash.toCharArray(), salt, 2000, 512);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		String hash = new String(toHex(factory.generateSecret(spec).getEncoded()));
		String saltInString = new String(toHex(salt));
		String strArr[] = new String[2];
		strArr[1] = saltInString;
		strArr[0] = hash;
		return strArr;
	}
	
	public boolean validatePassword(String password, String storedSalt, String storedHashString) throws NoSuchAlgorithmException, InvalidKeySpecException  {
		byte[] salt = fromHex(storedSalt);
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 2000, 512);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] newHash = factory.generateSecret(spec).getEncoded();
		byte[] storedHash = fromHex(storedHashString);
		int diff = newHash.length ^ storedHash.length;
		if(diff == 1) {
			return false;
		} else {
			for(int i = 0; i < newHash.length && i < storedHash.length; i++)
	        {
	            diff |= storedHash[i] ^ newHash[i];
	        }
			return diff == 0;
		}
		
	}
	
	private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f' };
	
	public static char[] toHex(byte[] bytes) {
	    final int nBytes = bytes.length;
	    char[] result = new char[2 * nBytes];         //  1 hex contains two chars
	                                                  //  hex = [0-f][0-f], e.g 0f or ff
	    int j = 0;
	    for (byte aByte : bytes) {                    // loop byte by byte
	                                                  // 0xF0 = FFFF 0000
	      result[j++] = HEX[(0xF0 & aByte) >>> 4];    // get the top 4 bits, first half hex char
	                                                  // 0x0F = 0000 FFFF
	      result[j++] = HEX[(0x0F & aByte)];          // get the bottom 4 bits, second half hex char
	                                                  // combine first and second half, we get a complete hex
	    }
	    return result;
	  }
	
	private static byte[] fromHex(String hex)
    {
		int len = hex.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len-1; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
	                             + Character.digit(hex.charAt(i+1), 16));
	    }
	    return data;
    }

}

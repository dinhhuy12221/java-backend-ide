package Server.socket;

import javax.crypto.*;

import org.apache.commons.lang3.SerializationUtils;

import Object.CodeResult;

import java.security.*;

public class encryption {
	private SecretKey secretKey;
	private PublicKey publicKey;
	private KeyPair keyPair;
	private byte[] encryptedKey;
	private Key decryptedKey;

	public encryption() {
		try {
			// Generate a RSA key pair
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
			
			// // Generate a random AES key
			// KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			// keyGenerator.init(256);
			// secretKey = keyGenerator.generateKey();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public KeyPair getKeyPair() {
        return keyPair;
    }

    public Key getDecryptedKey() {
		return decryptedKey;
	}

	public void setDecryptedKey(Key decryptedKey) {
		this.decryptedKey = decryptedKey;
	}

	public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }
	
	public SecretKey getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(SecretKey secretKey) {
		this.secretKey = secretKey;
	}

	public byte[] getEncryptedKey() {
		return encryptedKey;
	}

	public void setEncryptedKey(byte[] encryptedKey) {
		this.encryptedKey = encryptedKey;
	}

	public byte[] encryptData(CodeResult codeResult) {
		try {
			Cipher aesCipher = Cipher.getInstance("AES");
			aesCipher.init(Cipher.ENCRYPT_MODE, decryptedKey);
			byte[] encryptedData = aesCipher.doFinal(SerializationUtils.serialize(codeResult));
			return encryptedData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public byte[] encryptData(String str) {
		try {
			Cipher aesCipher = Cipher.getInstance("AES");
			aesCipher.init(Cipher.ENCRYPT_MODE, decryptedKey);
			byte[] encryptedData = aesCipher.doFinal(SerializationUtils.serialize(str));
			return encryptedData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object decryptData(byte[] encryptedData) {
		try {
			Cipher aesCipher = Cipher.getInstance("AES");
			aesCipher.init(Cipher.DECRYPT_MODE, decryptedKey);
			byte[] decryptedData = aesCipher.doFinal(encryptedData);
			Object object = SerializationUtils.deserialize(decryptedData);
			
			return object;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void decryptKey() {
		try {
			Cipher rsaCipher = Cipher.getInstance("RSA");
			rsaCipher.init(Cipher.UNWRAP_MODE, keyPair.getPrivate());
			decryptedKey = rsaCipher.unwrap(encryptedKey, "AES", Cipher.SECRET_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

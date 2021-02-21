package fr.isen.levreau.smartshirtapp

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import fr.isen.levreau.smartshirtapp.bdd.DatabaseValue
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


class Crypto {
    @RequiresApi(Build.VERSION_CODES.M)
    fun cipherCoordinates(Coordinates: DatabaseValue): DatabaseValue {
        val key = retrieveKey()
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val x1 = cipher.doFinal(Coordinates.x1.toByteArray()).toString()
        val y1 = cipher.doFinal(Coordinates.y1.toByteArray()).toString()
        val z1 = cipher.doFinal(Coordinates.z1.toByteArray()).toString()
        return DatabaseValue(x1, y1, z1)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun decipherCoordinates(Coordinates: DatabaseValue): DatabaseValue {
        val key = retrieveKey()
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(128, cipher.iv)
        cipher.init(Cipher.DECRYPT_MODE, key, spec)
        val x1 = cipher.doFinal(Coordinates.x1.toByteArray()).toString()
        val y1 = cipher.doFinal(Coordinates.y1.toByteArray()).toString()
        val z1 = cipher.doFinal(Coordinates.z1.toByteArray()).toString()
        return DatabaseValue(x1, y1, z1)
    }



    @RequiresApi(Build.VERSION_CODES.M)
    private fun generateSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
        )
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    "EncryptionKey",

                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
//                    .setUserAuthenticationRequired(true)
//                    .setUserAuthenticationParameters(300,0)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            )
        }
        return keyGenerator.generateKey()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun retrieveKey(): SecretKey {
        val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        if (keyStore.getKey("EncryptionKey", null) == null){
            return generateSecretKey()
        }
        return keyStore.getKey("EncryptionKey", null) as SecretKey
    }

}

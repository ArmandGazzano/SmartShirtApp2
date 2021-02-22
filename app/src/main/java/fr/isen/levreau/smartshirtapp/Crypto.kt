package fr.isen.levreau.smartshirtapp

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.annotation.RequiresApi
import fr.isen.levreau.smartshirtapp.bdd.DatabaseValue
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec


class Crypto {
    @RequiresApi(Build.VERSION_CODES.M)
    fun cipherCoordinates(Coordinates: DatabaseValue): DatabaseValue {
        val key = retrieveKey()

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv_x = cipher.iv.copyOf()
        Log.i("crypto", "iv_x_cipher: $iv_x" )
        Log.i("crypto", iv_x.size.toString())
        Log.i("crypto", String(iv_x,Charsets.UTF_8).length.toString())
        val x1 = String (cipher.doFinal(Coordinates.x1.toByteArray()))
        val x2 = x1 + String(iv_x,Charsets.UTF_8)

        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv_y = cipher.iv.copyOf()
        val y1 = String.format("%02X",cipher.doFinal(Coordinates.y1.toByteArray()),Charsets.UTF_8)
        val y2 = y1 + String.format("%02X",(iv_y),Charsets.UTF_8)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv_z = cipher.iv.copyOf()

        val z1 = String.format("%02X",cipher.doFinal(Coordinates.z1.toByteArray()))
        val z2 = z1 + String.format("%02X",iv_z)
        Log.i("crypto", "===========================")
        Log.i("crypto", "===========================")
        Log.i("crypto", "===========================")
        return DatabaseValue(x2, y2, z2)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun decipherCoordinates(Coordinates: DatabaseValue): DatabaseValue {
        val key = retrieveKey()
        Log.i("crypto", "Test10")
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        Log.i("crypto", Coordinates.x1.length.toString())
        val iv_x = Coordinates.x1.substring(Coordinates.x1.length-12,Coordinates.x1.length)
        Log.i("crypto", "Test65")
        val x1 = Coordinates.x1.substring(0,Coordinates.x1.length-12)
        Log.i("crypto", "iv_x_decipher: $iv_x")
        Log.i("crypto", iv_x.length.toString())
        Log.i("crypto", iv_x.toByteArray().size.toString())
        Log.i("crypto", iv_x.toByteArray().toString())

        Log.i("crypto", x1)
        Log.i("crypto", x1.length.toString())
        Log.i("crypto", "===========================")
        val spec_x = GCMParameterSpec(128, iv_x.toByteArray())
        Log.i("crypto", "Test12")
        cipher.init(Cipher.DECRYPT_MODE, key, spec_x)
        Log.i("crypto", "Test1")
        val x2 = String(cipher.doFinal(x1.toByteArray()))
        Log.i("crypto", "Test2")





        val iv_y = Coordinates.y1.substring(Coordinates.y1.length-12,Coordinates.y1.length)
        val y1 = Coordinates.y1.substring(0,Coordinates.y1.length-12)
        val spec_y = GCMParameterSpec(128, iv_y.toByteArray())
        Log.i("crypto", "Test12")
        cipher.init(Cipher.DECRYPT_MODE, key, spec_y)
        val y2 = String(cipher.doFinal(y1.toByteArray()))
        Log.i("crypto", "Test3")

        val iv_z = Coordinates.y1.substring(Coordinates.z1.length-12,Coordinates.z1.length)
        val z1 = Coordinates.y1.substring(0,Coordinates.z1.length-12)
        val spec_z = GCMParameterSpec(128, iv_z.toByteArray())
        cipher.init(Cipher.DECRYPT_MODE, key, spec_z)
        val z2 = String(cipher.doFinal(z1.toByteArray()))

        return DatabaseValue(x2, y2, z2)
    }



    @RequiresApi(Build.VERSION_CODES.M)
    private fun generateSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
        )
        Log.i("crypto", Build.VERSION.SDK_INT.toString())
        Log.i("crypto", Build.VERSION_CODES.R.toString())
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    "EncryptionKey",
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            )
        return keyGenerator.generateKey()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun retrieveKey(): SecretKey {
        val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        if (Collections.list(keyStore.aliases()).toString() == "[]"){
            return generateSecretKey()
        }
        return keyStore.getKey("EncryptionKey", null) as SecretKey
    }

    @Throws(KeyStoreException::class)
    private fun getAllAliasesInTheKeystore(): ArrayList<String?>? {
        val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        return Collections.list(keyStore.aliases())
    }

}

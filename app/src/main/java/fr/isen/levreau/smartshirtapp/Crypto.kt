package fr.isen.levreau.smartshirtapp

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.annotation.RequiresApi
import fr.isen.levreau.smartshirtapp.bdd.DatabaseValue
import java.security.KeyStore
import java.util.*
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
        val iv_x = cipher.iv.copyOf()
        val x1 = cipher.doFinal(Coordinates.x1.toByteArray())
        val x2 = x1 + iv_x
        val x3 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(x2)
        } else {
            TODO("VERSION.SDK_INT < O")
        }


        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv_y = cipher.iv.copyOf()
        val y1 = cipher.doFinal(Coordinates.y1.toByteArray())
        val y2: ByteArray = y1 + iv_y
        val y3 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(y2)
        } else {
            TODO("VERSION.SDK_INT < O")
        }


        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv_z = cipher.iv.copyOf()
        val z1 = cipher.doFinal(Coordinates.z1.toByteArray())
        val z2 = z1 + iv_z
        val z3 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(z2)
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        return DatabaseValue(x3, y3, z3,x3, y3, z3,x3, y3, z3)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun decipherCoordinates(Coordinates: DatabaseValue): DatabaseValue {
        val key = retrieveKey()
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        val x1 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getDecoder().decode(Coordinates.x1)
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        val iv_x = x1.copyOfRange(x1.size - 12, x1.size)
        val x2 = x1.copyOfRange(0, x1.size - 12)
        val spec_x = GCMParameterSpec(128, iv_x)
        cipher.init(Cipher.DECRYPT_MODE, key, spec_x)
        val x3 = cipher.doFinal(x2).toString(charset("UTF-8"))
        Log.i("crypto", "100dqzqzdqsdz : $x3")


        val y1 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getDecoder().decode(Coordinates.y1)
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        val iv_y = y1.copyOfRange(y1.size - 12, y1.size)
        val y2 = y1.copyOfRange(0, y1.size - 12)
        val spec_y = GCMParameterSpec(128, iv_y)
        cipher.init(Cipher.DECRYPT_MODE, key, spec_y)
        val y3 = cipher.doFinal(y2).toString(charset("UTF-8"))
        Log.i("crypto", "0 : $y3")


        val z1 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getDecoder().decode(Coordinates.z1)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val iv_z = z1.copyOfRange(z1.size - 12, z1.size)
        val z2 = z1.copyOfRange(0, z1.size - 12)
        val spec_z = GCMParameterSpec(128, iv_z)
        cipher.init(Cipher.DECRYPT_MODE, key, spec_z)
        val z3 = cipher.doFinal(z2).toString(charset("UTF-8"))
        Log.i("crypto", "-100 : $z3")


        return DatabaseValue(x3, y3, z3,x3, y3, z3,x3, y3, z3)
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
        if (Collections.list(keyStore.aliases()).toString() == "[]") {
            return generateSecretKey()
        }
        return keyStore.getKey("EncryptionKey", null) as SecretKey
    }
}
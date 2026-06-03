package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository

import android.content.Context
import android.net.Uri
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remote.SupabaseClientProvider
import io.github.jan.supabase.storage.storage
import java.util.UUID

class ReceiptStorageRepository {

    private val supabase = SupabaseClientProvider.client
    private val bucketName = "receipts"

    suspend fun uploadReceipt(
        context: Context,
        userId: String,
        imageUri: Uri
    ): String {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
                ?: throw Exception("Could not open selected image")

            val imageBytes = inputStream.use { it.readBytes() }

            val fileName = "${UUID.randomUUID()}.jpg"
            val filePath = "$userId/$fileName"

            supabase.storage
                .from(bucketName)
                .upload(
                    path = filePath,
                    data = imageBytes
                ) {
                    upsert = true
                }

            supabase.storage
                .from(bucketName)
                .publicUrl(filePath)

        } catch (e: Exception) {
            throw Exception("Receipt upload failed: ${e.message}")
        }
    }
}
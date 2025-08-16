package com.example.demo

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/api/s3")
class S3UploadController(
    private val s3MultipartUploadService: S3MultipartUploadService
) {

    @PostMapping("/upload")
    fun uploadFile(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("bucketName") bucketName: String,
        @RequestParam("key", required = false) key: String?
    ): CompletableFuture<ResponseEntity<Map<String, String>>> {
        
        if (file.isEmpty) {
            return CompletableFuture.completedFuture(
                ResponseEntity.badRequest().body(mapOf("error" to "File is empty"))
            )
        }

        val fileName = key ?: file.originalFilename ?: "uploaded-file"
        val tempFile = createTempFile(file)

        return s3MultipartUploadService.uploadFile(bucketName, fileName, tempFile)
            .thenApply { result ->
                Files.deleteIfExists(tempFile)
                ResponseEntity.ok(mapOf(
                    "message" to result,
                    "bucket" to bucketName,
                    "key" to fileName,
                    "size" to file.size.toString()
                ))
            }
            .exceptionally { throwable ->
                Files.deleteIfExists(tempFile)
                ResponseEntity.internalServerError().body(mapOf(
                    "error" to "Upload failed: ${throwable.message}"
                ))
            }
    }

    @PostMapping("/upload-with-progress")
    fun uploadFileWithProgress(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("bucketName") bucketName: String,
        @RequestParam("key", required = false) key: String?
    ): CompletableFuture<ResponseEntity<Map<String, String>>> {
        
        if (file.isEmpty) {
            return CompletableFuture.completedFuture(
                ResponseEntity.badRequest().body(mapOf("error" to "File is empty"))
            )
        }

        val fileName = key ?: file.originalFilename ?: "uploaded-file"
        val tempFile = createTempFile(file)

        return s3MultipartUploadService.uploadWithProgressTracking(
            bucketName, 
            fileName, 
            tempFile
        ) { transferred, total ->
            val percentage = if (total > 0) (transferred * 100 / total) else 0
            println("Upload progress: $percentage% ($transferred/$total bytes)")
        }.thenApply { result ->
            Files.deleteIfExists(tempFile)
            ResponseEntity.ok(mapOf(
                "message" to result,
                "bucket" to bucketName,
                "key" to fileName,
                "size" to file.size.toString()
            ))
        }.exceptionally { throwable ->
            Files.deleteIfExists(tempFile)
            ResponseEntity.internalServerError().body(mapOf(
                "error" to "Upload failed: ${throwable.message}"
            ))
        }
    }

    private fun createTempFile(file: MultipartFile): Path {
        val tempFile = Files.createTempFile("s3-upload-", "-${file.originalFilename}")
        Files.copy(file.inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING)
        return tempFile
    }
}
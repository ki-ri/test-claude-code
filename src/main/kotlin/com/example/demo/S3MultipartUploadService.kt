package com.example.demo

import org.springframework.stereotype.Service
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.transfer.s3.S3TransferManager
import software.amazon.awssdk.transfer.s3.model.FileUpload
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

@Service
class S3MultipartUploadService {

    private val s3AsyncClient: S3AsyncClient
    private val transferManager: S3TransferManager

    init {
        s3AsyncClient = S3AsyncClient.crtBuilder()
            .credentialsProvider(DefaultCredentialsProvider.create())
            .region(Region.AP_NORTHEAST_1)
            .targetThroughputInGbps(20.0)
            .minimumPartSizeInBytes(8 * 1024 * 1024) // 8MB
            .build()

        transferManager = S3TransferManager.builder()
            .s3Client(s3AsyncClient)
            .build()
    }

    fun uploadFile(bucketName: String, key: String, filePath: Path): CompletableFuture<String> {
        val uploadRequest = UploadFileRequest.builder()
            .putObjectRequest { builder ->
                builder.bucket(bucketName)
                builder.key(key)
            }
            .source(filePath)
            .build()

        val fileUpload: FileUpload = transferManager.uploadFile(uploadRequest)

        return fileUpload.completionFuture().thenApply { 
            "File uploaded successfully to s3://$bucketName/$key"
        }
    }

    fun uploadWithProgressTracking(
        bucketName: String, 
        key: String, 
        filePath: Path,
        progressCallback: (Long, Long) -> Unit = { _, _ -> }
    ): CompletableFuture<String> {
        val uploadRequest = UploadFileRequest.builder()
            .putObjectRequest { builder ->
                builder.bucket(bucketName)
                builder.key(key)
            }
            .source(filePath)
            .build()

        val fileUpload: FileUpload = transferManager.uploadFile(uploadRequest)

        // Monitor progress in a separate thread
        Thread {
            try {
                while (!fileUpload.completionFuture().isDone) {
                    val progress = fileUpload.progress().snapshot()
                    progressCallback(progress.transferredBytes(), progress.totalBytes().orElse(0))
                    Thread.sleep(1000) // Update every second
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }.start()

        return fileUpload.completionFuture().thenApply { 
            "File uploaded successfully to s3://$bucketName/$key"
        }
    }

    fun close() {
        transferManager.close()
        s3AsyncClient.close()
    }
}
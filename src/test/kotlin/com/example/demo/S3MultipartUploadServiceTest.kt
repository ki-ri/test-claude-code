package com.example.demo

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.springframework.boot.test.context.SpringBootTest
import java.nio.file.Files
import java.nio.file.Path
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertNotNull

@SpringBootTest
class S3MultipartUploadServiceTest {

    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `can create upload request for large file`() {
        val testFile = tempDir.resolve("test-large-file.txt")
        
        val content = "Test content for multipart upload".repeat(1000000) // ~30MB
        Files.write(testFile, content.toByteArray())
        
        assertTrue(Files.exists(testFile))
        assertTrue(Files.size(testFile) > 8 * 1024 * 1024) // Greater than 8MB minimum part size
    }
}
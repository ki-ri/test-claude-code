package com.example.demo

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region

@Configuration
@ConfigurationProperties(prefix = "aws")
class AwsConfiguration {

    @Bean
    fun dotenv(): Dotenv {
        return try {
            Dotenv.configure()
                .directory(".")
                .filename(".env")
                .ignoreIfMissing()
                .load()
        } catch (e: Exception) {
            Dotenv.configure()
                .ignoreIfMissing()
                .load()
        }
    }

    @Bean
    fun awsCredentialsProvider(dotenv: Dotenv): AwsCredentialsProvider {
        val accessKeyId = dotenv["AWS_ACCESS_KEY_ID"] 
            ?: System.getenv("AWS_ACCESS_KEY_ID")
            
        val secretAccessKey = dotenv["AWS_SECRET_ACCESS_KEY"] 
            ?: System.getenv("AWS_SECRET_ACCESS_KEY")

        return if (accessKeyId != null && secretAccessKey != null) {
            val credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey)
            StaticCredentialsProvider.create(credentials)
        } else {
            // Fallback to default credential chain for testing/development
            software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider.create()
        }
    }

    @Bean
    fun awsRegion(dotenv: Dotenv): Region {
        val regionString = dotenv["AWS_REGION"] 
            ?: System.getenv("AWS_REGION")
            ?: "ap-northeast-1"
            
        return Region.of(regionString)
    }
}
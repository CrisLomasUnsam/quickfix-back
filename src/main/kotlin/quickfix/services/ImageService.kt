package quickfix.services

import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile
import quickfix.utils.AVATAR_FILE_NAME
import quickfix.utils.CERTIFICATE_FILE_NAME
import quickfix.utils.IMAGES_UPLOADER_SERVER_URL
import quickfix.utils.VALID_IMG_EXTENSIONS
import quickfix.utils.exceptions.ImageException
import java.io.File

@Service
class ImageService {

    private fun verifyExtension(extension : String?) {
        val isValidExtension = extension != null && extension in VALID_IMG_EXTENSIONS
        if(!isValidExtension)
            throw ImageException("Ha habido un error al subir la imagen. Verifique que la extensión sea alguna de las siguientes: ${VALID_IMG_EXTENSIONS.joinToString(", ")}.")
    }

    fun uploadProfileImage(userId: Long, file: MultipartFile) {
        val extension = file.originalFilename?.substringAfterLast('.')
        verifyExtension(extension)

        val renamedFile = File.createTempFile(AVATAR_FILE_NAME,"$userId.$extension",)
        file.transferTo(renamedFile)

        val request = MultipartBodyBuilder().apply { part("image", renamedFile) }.build()
        RestTemplate().postForEntity(IMAGES_UPLOADER_SERVER_URL, request, String::class.java)
    }

    fun uploadCertificate(certificateId: Long, file: MultipartFile) {
        val extension = file.originalFilename?.substringAfterLast('.')
        verifyExtension(extension)

        val renamedFile = File.createTempFile(CERTIFICATE_FILE_NAME,"$certificateId.$extension",)
        file.transferTo(renamedFile)

        val request = MultipartBodyBuilder().apply { part("image", renamedFile) }.build()
        RestTemplate().postForEntity(IMAGES_UPLOADER_SERVER_URL, request, String::class.java)
    }

}
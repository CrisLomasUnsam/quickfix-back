package quickfix.services

import org.springframework.core.io.ByteArrayResource
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile
import quickfix.utils.*
import quickfix.utils.exceptions.ImageException

@Service
class ImageService {

    fun uploadProfileImage(userId: Long, avatar: MultipartFile) {

        validateFile(avatar)
        val renamedAvatar = object : ByteArrayResource(avatar.bytes) {
            override fun getFilename(): String = "$AVATAR_FILE_NAME$userId.jpg"
        }
        sendImageToUploadServer(renamedAvatar)
    }

    fun uploadCertificate(certificateId: Long, certificate: MultipartFile) {

        validateFile(certificate)
        val renamedCertificate = object : ByteArrayResource(certificate.bytes) {
            override fun getFilename(): String = "$CERTIFICATE_FILE_NAME$certificateId.jpg"
        }
        sendImageToUploadServer(renamedCertificate)
    }

    private fun validateFile(file: MultipartFile) {
        validateFileSize(file.size)
        validateExtension(file.originalFilename?.substringAfterLast("."))
    }

    private fun validateFileSize(fileSize: Long) {
        if(fileSize > MAX_FILE_SIZE)
            throw ImageException("El tamaño del archivo supera el tamaño máximo permitido.")
    }

    private fun validateExtension(extension : String?) {
        val isValidExtension = extension != null && extension in VALID_IMG_EXTENSIONS
        if(!isValidExtension)
            throw ImageException("Ha habido un error al subir la imagen. Verifique que la extensión sea alguna de las siguientes: ${VALID_IMG_EXTENSIONS.joinToString(", ")}.")
    }

    private fun sendImageToUploadServer(image : ByteArrayResource) {
        val body = LinkedMultiValueMap<String, Any>().apply{
            add("image", image)
        }
        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA

        val request = HttpEntity(body, headers)
        RestTemplate().postForEntity(IMAGES_UPLOADER_SERVER_URL, request, String::class.java)
    }

}
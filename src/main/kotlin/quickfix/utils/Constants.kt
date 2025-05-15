package quickfix.utils

const val FRONTEND_URL = "http://localhost:5173"

const val IMAGES_VIEWER_SERVER_URL = "http://localhost:9090"
const val AVATAR_FILE_NAME = "avatar-"
const val CERTIFICATE_FILE_NAME = "certificate-"
const val AVATAR_URL_BASE = "$IMAGES_VIEWER_SERVER_URL/$AVATAR_FILE_NAME" // + userId
const val CERTIFICATE_URL_BASE = "$IMAGES_VIEWER_SERVER_URL/$CERTIFICATE_FILE_NAME" // + certificateId
val VALID_IMG_EXTENSIONS = listOf("jpg", "jpeg", "png")

const val MAXIMUM_DEBT: Double = 1000.0
const val COMISSION = 0.04

const val PAGE_SIZE: Int = 4
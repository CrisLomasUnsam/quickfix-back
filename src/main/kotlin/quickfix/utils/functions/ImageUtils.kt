package quickfix.utils.functions

import quickfix.utils.AVATAR_FILE_NAME
import quickfix.utils.CERTIFICATE_FILE_NAME
import quickfix.utils.IMAGES_VIEWER_SERVER_URL

fun getAvatarUrl(userId: Long) : String =
    "$IMAGES_VIEWER_SERVER_URL/$AVATAR_FILE_NAME$userId.jpg"

fun getCertificateUrl(certificateId: Long) : String =
    "$IMAGES_VIEWER_SERVER_URL/$CERTIFICATE_FILE_NAME$certificateId.jpg"
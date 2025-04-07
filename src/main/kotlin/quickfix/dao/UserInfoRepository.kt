package quickfix.dao

import org.springframework.stereotype.Component
import quickfix.models.UserInfo

@Component
class UserInfoRepository: Repository<UserInfo>()
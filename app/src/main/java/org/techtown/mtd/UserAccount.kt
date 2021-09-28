package org.techtown.mtd

//로그인 정보를 받아올 클래스
class UserAccount(
    var idToken:String? = null, //Firebase Uid(고유 변호)
    var emailId: String? = null,
    var password: String? = null
)
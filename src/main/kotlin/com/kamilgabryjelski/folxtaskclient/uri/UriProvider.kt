package com.kamilgabryjelski.folxtaskclient.uri

class UriProvider {
    fun createUri() = UriConstants.HOSTURI + UriConstants.CREATE

    fun readAllUri() = UriConstants.HOSTURI + UriConstants.READALL
    fun readByIDUri(id: Long)  = UriConstants.HOSTURI + UriConstants.READBYID + "?id=$id"
    fun readByNameUri(name: String) = UriConstants.HOSTURI + UriConstants.READBYNAME + "?name=$name"

    fun updateUri() = UriConstants.HOSTURI + UriConstants.UPDATE

    fun deleteByIDUri(id: Long) = UriConstants.HOSTURI + UriConstants.DELETEBYID + "?id=$id"
    fun deleteByNameUri(name: String) = UriConstants.HOSTURI + UriConstants.DELETEBYNAME + "?name=$name"
}

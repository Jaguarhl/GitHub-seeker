package com.kartsev.dmitry.githubseeker.presenter.vo

import android.os.Parcel
import android.os.Parcelable

class RepositoryVO : Parcelable {
    var repoName: String = ""
    var repoLanguage: String = ""
    var authorName: String = ""
    var avatarUrl: String = ""

    constructor(repoName: String, repoLanguage: String, authorName: String, avatarUrl: String) {
        this.repoName = repoName
        this.repoLanguage = repoLanguage
        this.authorName = authorName
        this.avatarUrl = avatarUrl
    }

    override fun describeContents(): Int {
        return 0
    }

    protected constructor(`in`: Parcel) {
        repoName = `in`.readString()
        repoLanguage = `in`.readString()
        authorName = `in`.readString()
        avatarUrl = `in`.readString()
    }

    constructor()

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(repoName)
        dest.writeString(repoLanguage)
        dest.writeString(authorName)
        dest.writeString(avatarUrl)
    }

    companion object {

        val CREATOR: Parcelable.Creator<RepositoryVO> = object : Parcelable.Creator<RepositoryVO> {
            override fun createFromParcel(`in`: Parcel): RepositoryVO {
                return RepositoryVO(`in`)
            }

            override fun newArray(size: Int): Array<RepositoryVO?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun toString(): String {
        return "RepositoryVO{" +
                "repoName='" + repoName + '\''.toString() +
                ", repoLanguage='" + repoLanguage + '\''.toString() +
                ", authorName='" + authorName + '\''.toString() +
                ", avatarUrl='" + avatarUrl + '\''.toString() +
                '}'.toString()
    }
}

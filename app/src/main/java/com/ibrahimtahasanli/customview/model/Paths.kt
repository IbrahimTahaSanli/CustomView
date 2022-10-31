package com.ibrahimtahasanli.customview.model

enum class Paths(val URL: String) {
    HomePath("http://10.0.2.2:1324"),
    ViewsPath( Paths.HomePath.URL + "/view"),
    ViewPath(ViewsPath.URL + "/")
}